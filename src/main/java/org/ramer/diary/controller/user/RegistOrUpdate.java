package org.ramer.diary.controller.user;

import lombok.extern.slf4j.Slf4j;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 注册或更新类
 */
@Slf4j
@SessionAttributes(value = { "user", "topics", }, types = { User.class, Topic.class })
@Controller
public class RegistOrUpdate{
    @Autowired
    private UserService userService;

    /**
     * 更新前获取user.
     *
     * @param id UID
     * @param map the map
     */
    @ModelAttribute
    public void getUser(@RequestParam(value = "id", required = false) Integer id, Map<String, Object> map) {
        log.debug("更新前获取user");
        if (id != null) {
            log.debug("更新前获取user");
            log.debug("id = " + id);
            User user = userService.getById(id);
            map.put("user", user);
        }
    }

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
    public String newOrUpdate(User user, @RequestParam("picture") MultipartFile file, HttpSession session,
            Map<String, Object> map, @RequestParam("checkFile") String checkFile) {
        log.debug(Thread.currentThread().getStackTrace()[1].getMethodName());

        //  如果是更新,用户ID不为空
        if (userService.getByName(user.getUsername()) != null && user.getId() == null) {
            throw new UserExistException("用户名已存在,注册失败");
        }
        //如果是注册需要加密密码，而更新是不允许修改密码的
        if (user.getId() == null) {
            user.setPassword(Encrypt.execEncrypt(user.getPassword(), false));
        }
        //包含中文名称的用户,先设置别名
        if (StringUtils.hasChinese(user.getUsername())) {
            log.debug("用户名包含中文");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
            String alias = simpleDateFormat.format(new Date());
            user.setAlias(alias);
        }
        //先保存图片
        if (!file.isEmpty()) {
            log.debug("保存图片");
            String pictureUrl;
            try {
                pictureUrl = FileUtils.saveFile(file, session, true, StringUtils.hasChinese(user.getUsername()));
                user.setHead(pictureUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            checkFile = checkFile.substring(checkFile.indexOf("/", 2));
            log.debug("------------------------------" + checkFile);
            user.setHead(checkFile);
        }
        //    判断邮箱是否存在，如果存在说明以后未修改，不不要加密
        if (userService.getByEmail(user.getEmail()) == null) {
            user.setEmail(Encrypt.execEncrypt(user.getEmail(), true));
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
