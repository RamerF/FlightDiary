/*
 *
 */
package org.ramer.diary.repository;

import java.util.Set;

import org.ramer.diary.domain.Notify;
import org.ramer.diary.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author ramer
 *
 */
public interface NotifyRepository extends JpaRepository<Notify, Integer> {

  /**
   * 获取已/未读消息,按时间降序
   * @param user
   * @param hasCheck
   * @return 消息的集合
   */
  Set<Notify> getByNotifiedUserAndHasCheckOrderByDateDesc(User user, String hasCheck);

  /**
   * 通过id获取消息
   * @param notifyId
   * @return 一个消息
   */
  Notify getById(Integer notifyId);

  /**
   * 获取指定用户的消息总数
   * @param notifiedUser
   * @return 未读消息数
   */
  @Query("select count(n.id) from Notify n where n.notifiedUser= :notifiedUser and n.hasCheck='false'")
  int getCountByNotifiedUser(@Param("notifiedUser") User notifiedUser);

  /**
   * 通过id和被通知用户id获取消息.
   *
   * @param id the id
   * @param notifiedUser the notified user
   * @return 消息
   */
  Notify getByIdAndNotifiedUser(Integer id, User notifiedUser);
}
