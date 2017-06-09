/*
 *
 */
package org.ramer.diary.repository;

import org.ramer.diary.domain.FeedBack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author ramer
 *
 */
@Repository
public interface FeedBackRepository extends JpaRepository<FeedBack, Integer>{

}
