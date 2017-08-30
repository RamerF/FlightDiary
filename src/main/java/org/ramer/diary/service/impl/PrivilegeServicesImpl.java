package org.ramer.diary.service.impl;

import org.ramer.diary.domain.Privilege;
import org.ramer.diary.repository.PrivilegeRepository;
import org.ramer.diary.service.PrivilegeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by RAMER on 6/6/2017.
 */
@Service
public class PrivilegeServicesImpl implements PrivilegeService{
    @Resource
    private PrivilegeRepository privilegeRepository;

    @Transactional
    @Override
    public Privilege saveOrUpdate(Privilege privilege) {
        return privilegeRepository.saveAndFlush(privilege);
    }

    @Transactional
    @Override
    public boolean saveBatch(List<Privilege> privileges) {
        return privilegeRepository.save(privileges).size() > 0;
    }

}
