/*
 *
 */
package org.ramer.diary.repository;

import org.ramer.diary.domain.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface Roles repository.
 *
 * @author ramer
 */
@Repository
public interface RolesRepository extends JpaRepository<Roles, Integer>{

    /**
     * Gets by name.
     *
     * @param name the name
     * @return the by name
     */
    Roles getByName(String name);
}
