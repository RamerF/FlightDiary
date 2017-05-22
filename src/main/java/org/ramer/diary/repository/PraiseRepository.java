/*
 *
 */
package org.ramer.diary.repository;

import java.util.List;

import org.ramer.diary.domain.Praise;
import org.ramer.diary.domain.Topic;
import org.ramer.diary.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author ramer
 *
 */
public interface PraiseRepository extends JpaRepository<Praise, Integer> {
  /**
   * 获取点赞信息
   * @param user 用户
   * @param topic 分享
   * @return 返回指定的点赞信息
   */
  Praise getByUserAndTopic(User user, Topic topic);

  /**
   * 通过topicid删除所有用户点赞信息
   * @param topic 分享
   * @param user 用户
   */
  @Modifying
  @Query("delete from  Praise p where p.topic= :topic")
  void deleteByTopic(@Param("topic") Topic topic);

  /**
   * 通过topicid和userid删除点赞信息
   * @param topic 分享
   * @param user 用户
   */
  @Modifying
  @Query("delete from  Praise p where p.topic= :topic and p.user= :user")
  void deleteByTopicAndUser(@Param("topic") Topic topic, @Param("user") User user);

  /**
   * 该方法获取用户当前浏览他人的分享的点赞信息
   * @param user_id UID
   * @param other_id
   * @return 返回所有,被浏览用户发表的分享并且登录用户已点赞,的点赞信息
   */
  @Query(value = "select topic from praise where user= :user_id and topic in (select id from topic where user= :other_id)", nativeQuery = true)
  List<Integer> getTopicIdsByUserIdAndOtherId(@Param("user_id") Integer user_id,
      @Param("other_id") Integer other_id);
}
