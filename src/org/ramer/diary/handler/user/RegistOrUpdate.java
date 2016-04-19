package org.ramer.diary.handler.user;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.ramer.diary.domain.Topic;
import org.ramer.diary.domain.User;
import org.ramer.diary.exception.SystemWrongException;
import org.ramer.diary.exception.UserExistException;
import org.ramer.diary.service.UserService;
import org.ramer.diary.util.Encrypt;
import org.ramer.diary.util.FileUtils;
import org.ramer.diary.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

/**
 * 注册或更新类
 */
@SessionAttributes(value = { "user", "topics", }, types = { User.class, Topic.class })
@Controller
public class RegistOrUpdate {
  @Autowired
  private UserService userService;

  /**
  * 注册或更新用户信息.
  *
  * @param user 用户
  * @param file 头像文件
  * @param session the session
  * @param map the map
  * @return 如果用户名存在或执行操作错误返回错误页面.
  *      否则,更新返回个人主页,注册返回主页.
  */
  //  由于需要上传文件form 带有属性enctype="multipart/form-data",因此无法使用PUT请求
  @RequestMapping(value = "/user", method = RequestMethod.POST)
  public String newOrUpdate(User user, @RequestParam("picture") MultipartFile file,
      HttpSession session, Map<String, Object> map) {
    user.setPassword(Encrypt.execEncrypt(user.getPassword()));
    //  如果是更新,用户ID不为空
    if (userService.getByName(user.getName()) != null && user.getId() == null) {
      throw new UserExistException("用户名已存在,注册失败");
    }
    //包含中文名称的用户,先设置别名
    if (StringUtils.hasChinese(user.getName())) {
      System.out.println("用户名包含中文");
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
      String alias = simpleDateFormat.format(new Date());
      user.setAlias(alias);
    }
    //先保存图片
    if (!file.isEmpty()) {
      System.out.println("保存图片");
      String pictureUrl;
      try {
        pictureUrl = FileUtils.saveFile(file, session, true,
            StringUtils.hasChinese(user.getName()));
        user.setHead(pictureUrl);
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      user.setHead("pictures/userHead.jpg");
    }
    Integer id = user.getId();
    if (userService.newOrUpdate(user).getId() > 0) {
      user = userService.login(user);
      if (user.getId() == null) {
        throw new SystemWrongException("系统出错了,操作被取消,请返回重新操作");
      }
      map.put("user", user);
      //更新用户应返回到个人主页
      if (id != null) {
        return "redirect:/user/personal";
      }
      //注册用户返回到主页
      return "redirect:/home";
    }
    throw new SystemWrongException("系统出错了,操作被取消,请返回重新操作");
  }

}
