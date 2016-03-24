/*
 *
 */
package org.ramer.diary.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import org.ramer.diary.domain.Topic;
import org.ramer.diary.domain.User;

/**
 * @author ramer
 *
 */
public interface TopicRepository extends PagingAndSortingRepository<Topic, Integer> {

  /**
   * 获取指定用户分享的总数
   * @param user
   * @return 返回指定用户分享的总数
   */
  @Query("select count(t.id) from Topic t where t.user= :user")
  int getCountByUser(@Param("user") User user);

  /**
   * 通过用户id,获取topic
   * @param user
   * @param id
   * @return 返回分享的集合topics
   */
  List<Topic> getByUserOrderByDateAsc(User user);

  /**
   * 通过分享id,获取topic
   * @param topic_id
   * @return 返回一个分享topic
   */
  Topic getById(Integer topic_id);

  /**
   * 分享的点赞次数 + 1.
   *
   * @param topic_id the topic_id
   */
  @Modifying
  @Query("update Topic t set t.upCounts = (t.upCounts + 1) where t.id = :topic_id")
  void updateTopicPraise(@Param("topic_id") Integer topic_id);

  /**
   * 分享的点赞次数 - 1.
   *
   * @param topic_id the topic_id
   */
  @Modifying
  @Query("update Topic t set t.upCounts = (t.upCounts - 1) where t.id = :topic_id")
  void updateTopicNotPraise(@Param("topic_id") Integer topic_id);

  /**
   * 通过用户ID和topic ID获取topic
   * @param topic_id
   * @param user
   * @return 返回一个分享topic
   */
  Topic getByIdAndUser(Integer topic_id, User user);

}
