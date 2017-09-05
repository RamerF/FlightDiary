package org.ramer.diary.service;

import org.ramer.diary.domain.Privilege;

import java.util.List;

/**
 * Created by RAMER on 8/8/2017.
 */
public interface PrivilegeService{
    Privilege saveOrUpdate(Privilege privilege);

    boolean saveBatch(List<Privilege> privileges);
}
