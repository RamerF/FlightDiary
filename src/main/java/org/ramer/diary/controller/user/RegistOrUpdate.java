package org.ramer.diary.controller.user;

import lombok.extern.slf4j.Slf4j;
import org.ramer.diary.domain.Topic;
import org.ramer.diary.domain.User;
import org.ramer.diary.domain.dto.CommonResponse;
import org.ramer.diary.exception.SystemWrongException;
import org.ramer.diary.exception.UserExistException;
import org.ramer.diary.service.UserService;
import org.ramer.diary.util.EncryptUtil;
import org.ramer.diary.util.FileUtils;
import org.ramer.diary.util.StringUtils;
import org.ramer.diary.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
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
    @Autowired
    private UserValidator userValidator;

    @Value("${diary.encrypt.strength}")
    private int ENCRYPT_STRENGTH;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(userValidator);
    }

    /**
     * 更新前获取user.
     *
     * @param id UID
     * @param map the map
     */
    @ModelAttribute
    public void getUser(@RequestParam(value = "id", required = false) Integer id, Map<String, Object> map) {
        if (id != null) {
            log.debug("预加载user");
            map.put("user", userService.getById(id));
        }
    }

    @PostMapping("/sign_up")
    @ResponseBody
    public CommonResponse createUser(@Valid User user, BindingResult result) {
        log.debug("user: {}", user);
        if (result.hasErrors()) {
            StringBuilder message = new StringBuilder("提交信息有误:\n");
            result.getAllErrors().stream().iterator()
                    .forEachRemaining(objectError -> message.append(objectError.getDefaultMessage()).append("\n"));
            return new CommonResponse(false, message.toString());
        }

        user.setEmail(EncryptUtil.execEncrypt(user.getEmail()));
        if (StringUtils.hasChinese(user.getUsername())) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
            user.setAlias(simpleDateFormat.format(new Date()));
        }
        userService.newOrUpdate(user);
        log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + " user : {}", user);
        if (user.getId() > 0) {
            return new CommonResponse(true, "注册成功");
        }
        return new CommonResponse(false, "注册失败");
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
    @PostMapping("/user/{id}/update")
    public String newOrUpdate(@SessionAttribute(value = "user", required = false) User user,
            @RequestParam("picture") MultipartFile file, HttpSession session, Map<String, Object> map,
            @RequestParam("checkFile") String checkFile) {
        log.debug(Thread.currentThread().getStackTrace()[1].getMethodName());

        //  如果是更新,用户ID不为空
        if (userService.getByName(user.getUsername()) != null && user.getId() == null) {
            throw new UserExistException("用户名已存在,注册失败");
        }
        //如果是注册需要加密密码，而更新是不允许修改密码的
        if (user.getId() == null) {
            user.setPassword(EncryptUtil.execEncrypt(user.getPassword()));
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
            log.debug("checkFile: {}", checkFile);
            user.setHead(checkFile);
        }
        //    判断邮箱是否存在，如果存在说明以后未修改，不要加密
        if (userService.getByEmail(user.getEmail()) == null) {
            user.setEmail(EncryptUtil.execEncrypt(user.getEmail()));
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
