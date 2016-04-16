/*
 *
 */
package org.ramer.diary.repository;

import java.util.List;

import org.ramer.diary.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * @author ramer
 *
 */
public interface UserRepository
    extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {

  /**
   * 根据用户名和密码获取用户
   * @param name
   * @param password
   * @return 返回一个用户
   */
  User getByNameAndPassword(String name, String password);

  /**
   * 通过邮箱和密码获取用户
   *
   * @param email 用户邮箱
   * @param password 密码
   * @return 用户
   */
  User getByEmailAndPassword(String email, String password);

  /**通过id获取用户
   * @param id
   * @return 返回一个用户
   */
  User getById(Integer id);

  //  @Modifying
  //  @Query("update User u set u.says= :says where u.id= :user_id")
  //  void testUpdate(@Param("says") String says, @Param("user_id") Integer user_id);

  /**
   * 通过用户名获取用户信息
   * @param username
   * @return 用户
   */
  User getByName(String username);

  /**
   * 通过已加密邮箱获取用户
   * @param id 用户ID
   * @param email 已加密邮箱
   * @return
   */
  User getByIdAndEmail(Integer id, String email);

  /**
   * 通过已加密密码获取用户
   * @param id 用户ID
   * @param password 已加密密码
   * @return
   */
  User getByIdAndPassword(Integer id, String password);

  /**
   * 通过邮箱获取用户信息
   *
   * @param email 邮箱
   * @return 用户
   */
  User getByEmail(String email);

  /**
   * 统计发表分享最多的用户,并联合user表查询相关信息
   * @return
   */
  @Query(value = "select *from user join (select user,count(id) as n from topic group by user) as t on "
      + "t.user=id order by t.n desc", nativeQuery = true)
  List<User> getByIdJoinTopicUserId();
}
