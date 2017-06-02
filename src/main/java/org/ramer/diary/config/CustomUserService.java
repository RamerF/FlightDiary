package org.ramer.diary.config;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.ramer.diary.domain.Roles;
import org.ramer.diary.domain.User;
import org.ramer.diary.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RAMER on 5/22/2017.
 */
@Service
@Slf4j
public class CustomUserService implements UserDetailsService{
    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug(" 登录: {}", username);
        User user = userService.getByName(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Roles role : user.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + " role : {}",
                JSONObject.toJSONString(authorities));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                authorities);
    }
}
