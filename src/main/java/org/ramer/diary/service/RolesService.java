package org.ramer.diary.service;

import org.ramer.diary.domain.Roles;

/**
 * Created by RAMER on 6/6/2017.
 */
public interface RolesService{
    Roles saveOrUpdate(Roles roles);

    /**
     * Gets by name.
     *
     * @param name the name
     * @return the by name
     */
    public Roles getByName(String name);

    long countRole();
}
