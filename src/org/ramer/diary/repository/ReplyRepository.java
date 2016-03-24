/*
 *
 */
package org.ramer.diary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.ramer.diary.domain.Reply;

/**
 * @author ramer
 *
 */
public interface ReplyRepository extends JpaRepository<Reply, Integer> {

  /**
   * 通过回复ID获取评论ID.
   *
   * @param id 回复ID
   * @return 返回评论ID
   */
  @Query(value = "select comment from reply r where r.id= :id", nativeQuery = true)
  Integer getCommentByReply(@Param("id") Integer id);
}
