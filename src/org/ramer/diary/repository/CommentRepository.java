/*
 *
 */
package org.ramer.diary.repository;

import org.ramer.diary.domain.Comment;
import org.ramer.diary.domain.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author ramer
 *
 */
public interface CommentRepository extends JpaRepository<Comment, Integer> {
  /**
   * 通过id和分享UID获取评论
   * @param id
   * @param topic
   * @return 返回指定的评论
   */
  Comment getByIdAndTopic(Integer id, Topic topic);

}
