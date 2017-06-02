package org.ramer.diary.controller.user;

import java.io.IOException;

import javax.servlet.http.HttpSession;

import org.ramer.diary.domain.Topic;
import org.ramer.diary.domain.User;
import org.ramer.diary.service.UserService;
import org.ramer.diary.util.FileUtils;
import org.ramer.diary.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

/**
 * 更新用户头像.
 * @author ramer
 *
 *
 */
@Slf4j
@SessionAttributes(value = { "user", "topics", }, types = { User.class, Topic.class })
@Controller
public class EditHead {

  @Autowired
  private UserService userService;

  /**
   * 更新用户头像.
   *
   * @param user 用户
   * @param file 头像文件
   * @param session the session
   * @return 返回个人主页
   * @throws IOException Signals that an I/O exception has occurred.
   */
  @PutMapping("/user/update")
  public String editHead(User user,MultipartFile file,
      HttpSession session) throws IOException {
    log.debug("更新用户头像");
    if (!file.isEmpty()) {
      log.debug("保存图片");
      String pictureUrl = FileUtils.saveFile(file, session, true,
          StringUtils.hasChinese(user.getUsername()));
      user.setHead(pictureUrl);
    }
    userService.updateHead(user);
    return "redirect:/user/personal";
  }

}