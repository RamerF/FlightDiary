/*
 *
 */
package org.ramer.diary.repository;

import org.ramer.diary.domain.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author ramer
 *
 */
@Repository
public interface ReplyRepository extends JpaRepository<Reply, Integer>{

}
