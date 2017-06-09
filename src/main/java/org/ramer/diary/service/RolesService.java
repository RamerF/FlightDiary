package org.ramer.diary.service;

import org.ramer.diary.domain.Roles;

/**
 * Created by RAMER on 6/6/2017.
 */
public interface RolesService{
    /**
     * Gets by name.
     *
     * @param name the name
     * @return the by name
     */
    Roles getByName(String name);
}
