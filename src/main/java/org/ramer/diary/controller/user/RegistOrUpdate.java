package org.ramer.diary.controller.user;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.ramer.diary.constant.PageConstant;
import org.ramer.diary.domain.Roles;
import org.ramer.diary.domain.Topic;
import org.ramer.diary.domain.User;
import org.ramer.diary.domain.dto.CommonResponse;
import org.ramer.diary.domain.map.UserRoleMap;
import org.ramer.diary.exception.SystemWrongException;
import org.ramer.diary.exception.UserExistException;
import org.ramer.diary.service.RolesService;
import org.ramer.diary.service.UserService;
import org.ramer.diary.util.EncryptUtil;
import org.ramer.diary.util.FileUtils;
import org.ramer.diary.util.StringUtils;
import org.ramer.diary.validator.UserValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

/**
 * 注册或更新类
 */
@Slf4j
@SessionAttributes(value = { "user", "topics", }, types = { User.class, Topic.class })
@Controller
public class RegistOrUpdate{
    @Resource
    private UserService userService;
    @Resource
    private UserValidator userValidator;
    @Resource
    private RolesService rolesService;

    @Value("${diary.encrypt.strength}")
    private int ENCRYPT_STRENGTH;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(userValidator);
    }

    /**
     * 更新前获取user.
     *
     * @param userId UID
     * @param map the map
     */
    @ModelAttribute
    public void getUser(@RequestParam(value = "userid", required = false) Integer userId, Map<String, Object> map) {
        if (userId != null) {
            log.debug("预加载user");
            map.put("user", userService.getById(userId));
        }
    }

    @PostMapping("/sign_up")
    @ResponseBody
    public CommonResponse createUser(@Valid User user, BindingResult result) {
        log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + "user: {}", user);
        if (result.hasErrors()) {
            StringBuilder message = new StringBuilder("提交信息有误:").append(PageConstant.BR);
            result.getAllErrors().stream().iterator().forEachRemaining(
                    objectError -> message.append(objectError.getDefaultMessage()).append(PageConstant.BR));
            log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + " message : {}", message.toString());
            return new CommonResponse(false, message.toString());
        }
        user.setEmail(EncryptUtil.execEncrypt(user.getEmail()));
        if (StringUtils.hasChinese(user.getUsername())) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
            user.setAlias(simpleDateFormat.format(new Date()));
        }
        user.setPassword(EncryptUtil.execEncrypt(user.getPassword()));
        List<Roles> roles = new ArrayList<>();
        Roles userRole = rolesService.getByName(UserRoleMap.USER);
        log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + " userRole : {}", userRole);
        roles.add(userRole);
        user.setRoles(roles);
        log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + " user : {}", user);
        if (userService.newOrUpdate(user)) {
            log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + " user.getId() : {}", user.getId());

            SecurityContextHolder.getContext()
                    .setAuthentication(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            return new CommonResponse(true, "注册成功");
        }
        return new CommonResponse(false, "注册失败,请稍后再试");
    }

    //  由于需要上传文件form 带有属性enctype="multipart/form-data",因此无法使用PUT请求
    @PostMapping("/user/{id}/update")
    public String update(@SessionAttribute(value = "user") @Valid User user, @RequestParam("id") Integer userId,
            @RequestParam("picture") MultipartFile file, HttpSession session, Map<String, Object> map,
            @RequestParam("checkFile") String checkFile) {
        if (!userService.getByName(user.getUsername()).getId().equals(userId)) {
            throw new UserExistException("用户名已存在,更新失败");
        }
        //如果是注册需要加密密码，而更新是不允许修改密码的
        if (user.getId() == null) {
            user.setPassword(EncryptUtil.execEncrypt(user.getPassword()));
        }
        //包含中文名称的用户,先设置别名
        if (StringUtils.hasChinese(user.getUsername())) {
            log.debug("用户名包含中文");
            user.setAlias(new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()));
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
        if (userService.newOrUpdate(user)) {
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
