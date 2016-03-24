/*
 * 
 */
package org.ramer.diary.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.ramer.diary.domain.User;

/**
 * @author ramer
 *
 */
public interface UserRepository extends JpaRepository<User, Integer> {

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
   * 通过邮箱获取用户信息
   *
   * @param email 邮箱
   * @return 用户
   */
  User getByEmail(String email);

}
