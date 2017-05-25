/*
 *
 */
package org.ramer.diary.repository;

import org.ramer.diary.domain.Follow;
import org.ramer.diary.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ramer
 *
 */
@Repository
public interface FollowRepository extends JpaRepository<Follow, Integer>{
    /**
     * 通过用户和被关注用户获取关注信息
     * @param user
     * @param followedUser
     * @return 返回指定关注信息
     */
    Follow getByUserAndFollowedUser(User user, User followedUser);

    /**
     * 获取指定用户关注的用户总数
     * @param user
     * @return 返回用户关注的总记录数
     */
    @Query("select count(f.id) from Follow f where f.user= :user")
    int getCountByUser(@Param("user") User user);

    /**
     * 删除指定的关注记录
     * @param user
     * @param followedUser
     */
    @Modifying
    @Query("delete from Follow f where f.user = :user and f.followedUser = :followedUser")
    void deleteByUserAndFollowedUser(@Param("user") User user, @Param("followedUser") User followedUser);

    /**
     * 获取所有关注了当前用户的follow
     * @return 包含所有关注了当前用户的Follow
     */
    @Query(value = "select f.user from follow f where  f.followed_user= :user", nativeQuery = true)
    List<Integer> getUserByFollowedUser(@Param("user") User user);
}
