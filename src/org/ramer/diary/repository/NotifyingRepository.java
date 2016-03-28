/*
 *
 */
package org.ramer.diary.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.ramer.diary.domain.Notifying;
import org.ramer.diary.domain.User;

/**
 * @author ramer
 *
 */
public interface NotifyingRepository extends JpaRepository<Notifying, Integer> {

  /**
   * 获取已/未读消息
   * @param user
   * @param hasCheck
   * @return 消息的集合
   */
  Set<Notifying> getByNotifiedUserAndHasCheck(User user, String hasCheck);

  /**
   * 通过id获取消息
   * @param notifyId
   * @return 一个消息
   */
  Notifying getById(Integer notifyId);

  /**
   * 获取指定用户的消息总数
   * @param notifiedUser
   * @return 消息数
   */
  @Query("select count(n.id) from Notifying n where n.notifiedUser= :notifiedUser and n.hasCheck='false'")
  int getCountByNotifiedUser(@Param("notifiedUser") User notifiedUser);
}
