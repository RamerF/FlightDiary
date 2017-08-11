package org.ramer.diary.config;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.ramer.diary.domain.Privilege;
import org.ramer.diary.domain.Roles;
import org.ramer.diary.service.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by RAMER on 5/22/2017.
 */
@Service
@Slf4j
public class CustomUserService implements UserDetailsService {
    @Resource
    private UserService userService;
    @Resource
    private RolesService rolesService;
    @Resource
    private PrivilegeService privilegeService;
    private static final String[] PRIVILEGE_SUFFIXES = { "view", "edit" };

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (rolesService.countRole() < 1) {
            initRoleAndPrivilege();
        }
        log.debug(" 登录: {}", username);
        org.ramer.diary.domain.User user = userService.getByName(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
            role.getPrivileges().forEach(privilege -> {
                String privilegeName = privilege.getName();
                if (privilegeName.contains("*")) {
                    authorities.add(new SimpleGrantedAuthority(privilegeName));
                    for (String privilegeSuffix : PRIVILEGE_SUFFIXES) {
                        authorities.add(new SimpleGrantedAuthority(
                                privilegeName.substring(0, privilegeName.indexOf("*")) + privilegeSuffix));
                    }
                } else {
                    authorities.add(new SimpleGrantedAuthority(privilegeName));
                }
            });
        });
        log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + " permission : {}",
                JSONObject.toJSONString(authorities));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                authorities);
    }

    private void initRoleAndPrivilege() {
        // 用户角色
        Roles roles = new Roles();
        roles.setName("ROLE_USER");
        List<Privilege> privileges = new ArrayList<>();
        Privilege privilege = new Privilege();
        // 用户资源访问权限
        privilege.setName("user:*");
        privileges.add(privilege);
        roles.setPrivileges(privileges);

        roles.setName("ROLE_ADMIN");
        // 管理员资源访问权限
        privilege.setName("global:*");
        //管理员角色
    }
}
