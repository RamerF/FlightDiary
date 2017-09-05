package org.ramer.diary.config;

import lombok.extern.slf4j.Slf4j;
import org.ramer.diary.domain.Privilege;
import org.ramer.diary.domain.Roles;
import org.ramer.diary.service.PrivilegeService;
import org.ramer.diary.service.RolesService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by RAMER on 8/29/2017.
 */
@Component
@Slf4j
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent>{
    @Resource
    private RolesService rolesService;
    @Resource
    private PrivilegeService privilegeService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (rolesService.countRole() < 1) {
            log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + " ：初始化系统角色");
            initRoleAndPrivilege();
        }
    }

    private void initRoleAndPrivilege() {
        // 用户角色
        Roles roles = new Roles();
        roles.setName("ROLE_USER");
        List<Privilege> privileges = new ArrayList<>();
        Privilege userPrivilege = new Privilege();
        // 用户资源访问权限
        userPrivilege.setName("user:*");
        privileges.add(userPrivilege);
        roles.setPrivileges(privileges);
        rolesService.saveOrUpdate(roles);
        privilegeService.saveBatch(privileges);
        // 管理员资源访问权限
        roles = new Roles();
        roles.setName("ROLE_ADMIN");
        Privilege adminPrivilege = new Privilege();
        adminPrivilege.setName("global:*");
        privileges = new ArrayList<>();
        privileges.add(adminPrivilege);
        roles.setPrivileges(privileges);
        rolesService.saveOrUpdate(roles);
        privilegeService.saveBatch(privileges);
    }
}
