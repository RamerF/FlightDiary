package org.ramer.diary.repository;

import org.ramer.diary.domain.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by RAMER on 8/8/2017.
 */
public interface PrivilegeRepository extends JpaRepository<Privilege, Integer>{
}
