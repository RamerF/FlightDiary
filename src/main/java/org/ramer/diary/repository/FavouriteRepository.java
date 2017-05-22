/*
 *
 */
package org.ramer.diary.repository;

import org.ramer.diary.domain.Favourite;
import org.ramer.diary.domain.Topic;
import org.ramer.diary.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author ramer
 *
 */
public interface FavouriteRepository extends JpaRepository<Favourite, Integer> {
  /**
   * 通过用户和分享获取收藏信息
   * @param user
   * @param topic
   * @return 返回所有满足条件的收藏
   */
  Favourite getByUserAndTopic(User user, Topic topic);

  /**
   * 删除指定的收藏记录
   * @param user 用户
   * @param topic 收藏的分享
   */
  @Modifying
  @Query("delete from Favourite f where f.user = :user and f.topic = :topic")
  void deleteByUserAndTopic(@Param("user") User user, @Param("topic") Topic topic);

  /**
   * 获取所有,被浏览用户分享的并且当前用户收藏的,分享ID
   * @param user_id
   * @param other_id
   * @return 返回
   */
  @Query(value = "select topic from favourite where user= :user_id and topic in (select id from topic where user= :other_id)", nativeQuery = true)
  List<Integer> getTopicIdsByUserIdAndOtherId(@Param("user_id") Integer user_id,
      @Param("other_id") Integer other_id);
}
