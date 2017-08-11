/*
 *
 */
package org.ramer.diary.repository;

import org.ramer.diary.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author ramer
 *
 */
@Repository
public interface AlbumsRepository extends JpaRepository<Albums, Integer>{
}
