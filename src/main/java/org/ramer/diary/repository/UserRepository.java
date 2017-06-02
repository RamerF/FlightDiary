/*
 *
 */
package org.ramer.diary.repository;

import org.ramer.diary.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ramer
 *
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User>{

    /**
     * 根据用户名和密码获取用户
     * @param name
     * @param password
     * @return 返回一个用户
     */
    User getByUsernameAndPassword(String name, String password);

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
    User getByUsername(String username);

    /**
     * 通过邮箱获取用户信息
     *
     * @param email 邮箱
     * @return 用户
     */
    User getByEmail(String email);

    /**
     * 统计发表分享最多的用户,并联合user表查询相关信息.
     *
     * @param start 记录开始的序号
     * @param size 每页记录大小
     * @return 用户的分页记录
     */
    @Query(value = "select *from user join (select user,count(id) as n from topic group by user) as t on "
            + "t.user=id order by t.n desc limit :start,:size", nativeQuery = true)
    List<User> getByIdJoinTopicUserId(@Param("start") int start, @Param("size") int size);
}
