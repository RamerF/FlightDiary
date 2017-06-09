package org.ramer.diary.util;

import lombok.extern.slf4j.Slf4j;
import org.ramer.diary.domain.Topic;
import org.ramer.diary.domain.User;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 文件操作工具类，包含常用的静态方法：
 * <strong>
 * 1.删除文件
 * 2.保存文件
 * 3.读取xml文件
 * 4.向文件中写入数据
 * <strong>
 * @author ramer
 *
 */
@Slf4j
public class FileUtils{
    /**
     * 删除分享图片.
     *
     * @param topic 图片对应的分享
     * @param session the session
     * @param chn 是否中文
     * @return 删除成功返回true
     * @throws IOException
     */
    public static boolean deleteFile(Topic topic, HttpSession session, boolean chn) throws IOException {
        String separator = File.separator;
        String location = "";
        //  如果操作系统是Linux
        if (System.getProperty("os.username").equals("Linux")) {
            location = new File(System.getProperty("user.home") + "/Projects/web/workspace/eclipse/"
                    + session.getServletContext().getServletContextName()).getCanonicalPath();
        } else {
            location = new File(session.getServletContext().getRealPath("/")).getCanonicalPath();
        }
        String rootdir = location + separator + "pictures" + separator + "publish";
        User user = (User) session.getAttribute("user");
        String username = user.getUsername();
        String alias = user.getAlias();
        String picture = topic.getPicture();
        String pictureName = picture.substring(picture.lastIndexOf(separator) + 1);
        log.debug("rootdir:\n\t" + rootdir + "\npictureName:\n\t" + pictureName);
        String realPath = rootdir + separator + username + separator + pictureName;

        // 如果用户名中含有中文,路径是别名
        if (chn) {
            realPath = rootdir + separator + alias + separator + pictureName;

        }
        File file = new File(realPath);
        log.debug("删除文件 : " + file.getAbsolutePath());
        return !file.exists() || file.delete();
    }

    /**
     * 保存上传的图片：将会保存两份：
     *  1.备份文件（项目路径下）.
     *  2.原文件（服务器路径下）.
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
        String location;
        //    如果操作系统是Linux
        if (System.getProperty("os.name").equals("Linux")) {
            location = new File(System.getProperty("user.home") + "/Projects/web/workspace/eclipse/"
                    + session.getServletContext().getServletContextName()).getCanonicalPath();
        } else {
            location = new File(session.getServletContext().getRealPath("/")).getAbsolutePath();
        }
        String rootdir = location + separator + "pictures" + separator + "publish";
        User user = (User) session.getAttribute("user");
        log.debug("用户名: " + user.getUsername());
        String username = user.getUsername();
        String alias = user.getAlias();
        File userFolder = new File(rootdir + separator + username);
        if (chn) {
            userFolder = new File(rootdir + separator + alias);
        }
        if (!userFolder.exists()) {
            userFolder.mkdirs();
        }
        //获取图片的名称
        String name = file.getOriginalFilename();
        String path = rootdir + separator + username + separator;
        if (chn) {
            path = rootdir + separator + alias + separator;
        }
        String suffix = name.substring(name.indexOf("."));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        name = simpleDateFormat.format(new Date());

        String pathname = path + name + suffix;
        if (head) {
            pathname = path + username + suffix;
            if (chn) {
                pathname = path + alias + suffix;
            }
        }
        log.debug("上传的图片信息 : \n\t" + pathname);
        File saveFile = new File(pathname);
        saveFile.createNewFile();
        InputStream inputStream = file.getInputStream();
        OutputStream outputStream = new FileOutputStream(saveFile);
        byte[] bys = new byte[inputStream.available()];
        int length = 0;
        while ((length = inputStream.read(bys)) != -1) {
            outputStream.write(bys, 0, length);
        }
        outputStream.close();
        String pictureUrl = "\\pictures" + separator + "publish" + separator + username + separator + name + suffix;
        if (chn) {
            pictureUrl = "\\pictures" + separator + "publish" + separator + alias + separator + name + suffix;
        }
        if (head) {
            pictureUrl = "\\pictures" + separator + "publish" + separator + username + separator + username + suffix;
            if (chn) {
                pictureUrl = "\\pictures" + separator + "publish" + separator + alias + separator + alias + suffix;
            }
        }
        log.debug("数据库中的图片路径:" + pictureUrl);
        return pictureUrl;
    }

    /**
     * 读取文件中的tag.
     *
     * @param file 文件路径
     * @return 返回tag集合
     * @throws Exception the exception
     */
    public static Set<String> readTag(String file) throws Exception {

        Set<String> tagsList = new HashSet<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputStream inputStream = new FileInputStream(file);
        Document document = builder.parse(inputStream);
        NodeList tagsNode = document.getElementsByTagName("tag");
        for (int i = 0; i < tagsNode.getLength(); i++) {
            Node item = tagsNode.item(i);
            Element element = (Element) item;
            String name = element.getAttribute("name");
            tagsList.add(name);
        }
        return tagsList;
    }

    /**
     * 向文件添加tag.
     *
     * @param tags 要新添加的tag
     * @param file 文件路径
     * @throws Exception the exception
     */
    public static void writeTag(List<String> tags, String file) throws Exception {
        //    系统换行符
        String endLine = System.getProperty("line.separator");
        //   要写入的字符串
        String tagString = "";

        RandomAccessFile reader = new RandomAccessFile(file, "rw");
        String line = null;
        while ((line = reader.readLine()) != null) {
            if (line.indexOf("</tags>") >= 0) {
                reader.seek(reader.getFilePointer() - 8);
                reader.write(endLine.getBytes());
                for (String tag : tags) {
                    tagString = "    <tag username=\"" + tag + "\" />" + endLine;
                    reader.write(tagString.getBytes());
                }
                reader.write("</tags>".getBytes());
                break;
            }
        }
        reader.close();
    }
}
