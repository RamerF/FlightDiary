package org.ramer.diary.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.ramer.diary.domain.Topic;
import org.ramer.diary.domain.User;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件操作工具类，包含常用的静态方法：
 * <strong>
 * 1.删除文件
 * 2.保存文件
 * <strong>
 * @author ramer
 *
 */
public class FileUtils {
  /**
   * 删除分享图片.
   *
   * @param topic 图片对应的分享
   * @param session the session
   * @param chn 是否中文
   * @return 删除成功返回true
   */
  public static boolean deleteFile(Topic topic, HttpSession session, boolean chn) {
    User user = (User) session.getAttribute("user");
    String username = user.getName();
    String alias = user.getAlias();
    String picture = topic.getPicture();
    String pictureName = picture.substring(picture.lastIndexOf("\\"));
    String servletPath = session.getServletContext().getRealPath("pictures");
    System.out.println("servletPath:\n\t" + servletPath + "\npictureName:\n\t" + pictureName);
    String realPath = servletPath + "\\" + username + "\\" + pictureName;
    // 如果用户名中含有中文,路径是别名
    if (chn) {
      realPath = servletPath + "\\" + alias + "\\" + pictureName;
    }
    File file = new File(realPath);
    System.out.println("删除文件 : " + file.getAbsolutePath());
    return file.exists() ? file.delete() : true;
  }

  /**
   * 保存上传的图片.
   *
   * @param file 文件流
   * @param session httpsession
   * @param head 是否用户头像
   * @param chn 用户名中是否包含中文
   * @return 返回数据库中的图片路径
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public static String saveFile(MultipartFile file, HttpSession session, boolean head, boolean chn)
      throws IOException {
    String separator = File.separator;
    String location = "";
    //    如果操作系统是Linux
    if (System.getProperty("os.name").equals("Linux")) {
      location = new File(System.getProperty("user.home") + "/Projects/web/workspace/eclipse/"
          + session.getServletContext().getServletContextName()).getCanonicalPath();
    } else {
      location = new File(
          "D:/workspace/eclipse/" + session.getServletContext().getServletContextName())
              .getCanonicalPath();
    }
    String rootdir = location + separator + "WebContent" + separator + "pictures";
    User user = (User) session.getAttribute("user");
    System.out.println("用户名: " + user.getName());
    String username = user.getName();
    String alias = user.getAlias();
    File userFolder = new File(
        session.getServletContext().getRealPath("pictures") + separator + username);
    File userFolderBack = new File(rootdir + separator + username);
    if (chn) {
      userFolder = new File(
          session.getServletContext().getRealPath("pictures") + separator + alias);
      userFolderBack = new File(rootdir + separator + alias);
    }
    //  判断用户文件夹是否存在,不存在则创建
    if (!userFolder.exists()) {
      userFolder.mkdir();
    }
    if (!userFolderBack.exists()) {
      userFolderBack.mkdir();
    }
    //获取图片的名称
    String name = file.getOriginalFilename();
    String path = session.getServletContext().getRealPath("pictures") + separator + username
        + separator;
    String pathBack = rootdir + separator + username + separator;
    if (chn) {
      path = session.getServletContext().getRealPath("pictures") + separator + alias + separator;
      pathBack = rootdir + separator + alias + separator;
    }
    String suffix = name.substring(name.indexOf("."));
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
    name = simpleDateFormat.format(new Date());
    String pathname = path + name + suffix;
    String pathnameBack = pathBack + name + suffix;
    if (head) {
      pathname = path + username + suffix;
      pathnameBack = pathBack + username + suffix;
      if (chn) {
        pathname = path + alias + suffix;
        pathnameBack = pathBack + alias + suffix;
      }
    }
    System.out.println("上传的图片信息 : \n\t" + pathname);
    System.out.println("上传的图片备份信息 : \n\t" + pathnameBack);
    File saveFile = new File(pathname);
    File saveFileBack = new File(pathnameBack);
    saveFile.createNewFile();
    saveFileBack.createNewFile();
    InputStream inputStream = file.getInputStream();
    OutputStream outputStream = new FileOutputStream(saveFile);
    byte[] bys = new byte[inputStream.available()];
    int length = 0;
    while ((length = inputStream.read(bys)) != -1) {
      outputStream.write(bys, 0, length);
    }
    inputStream.close();
    outputStream.close();
    inputStream = file.getInputStream();
    OutputStream outputStreamBack = new FileOutputStream(saveFileBack);
    byte[] bysBack = new byte[inputStream.available()];
    int lengthBack = 0;
    while ((lengthBack = inputStream.read(bysBack)) != -1) {
      outputStreamBack.write(bysBack, 0, lengthBack);
    }
    outputStreamBack.close();
    String pictureUrl = "pictures" + separator + username + separator + name + suffix;
    if (chn) {
      pictureUrl = "pictures" + separator + alias + separator + name + suffix;
    }
    if (head) {
      pictureUrl = "pictures" + separator + username + separator + username + suffix;
      if (chn) {
        pictureUrl = "pictures" + separator + alias + separator + alias + suffix;
      }
    }
    System.out.println("数据库中的图片路径:" + pictureUrl);
    return pictureUrl;
  }

}
