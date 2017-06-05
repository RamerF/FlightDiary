package org.ramer.diary.validator;

import org.ramer.diary.domain.User;
import org.ramer.diary.util.MailUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * The {@link User} validator.
 * Created by RAMER on 6/3/2017.
 */
@Component
public class UserValidator implements Validator{

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(User.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;
        String password = user.getPassword();
        if (password.length() < 8 || password.length() > 15) {
            errors.rejectValue("password", "field.password.length", "密码必须为8-15个字符");
        }
        String username = user.getUsername();
        if (username.length() < 8 || username.length() > 15) {
            errors.rejectValue("username", "field.username.length", "用户名必须为8-15个字符");
        }
        String email = user.getEmail();
        if (!MailUtils.isEmail(email)) {
            errors.rejectValue("email", "field.email.error", "邮箱格式不正确");
        }

    }
}
