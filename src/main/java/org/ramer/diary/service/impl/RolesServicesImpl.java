package org.ramer.diary.service.impl;

import org.ramer.diary.domain.Roles;
import org.ramer.diary.repository.RolesRepository;
import org.ramer.diary.service.RolesService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by RAMER on 6/6/2017.
 */
@Service
public class RolesServicesImpl implements RolesService{
    @Resource
    private RolesRepository rolesRepository;

    @Transactional(readOnly = true)
    @Override
    public Roles getByName(String name) {
        return rolesRepository.getByName(name);
    }
}
