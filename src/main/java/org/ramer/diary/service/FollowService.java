package org.ramer.diary.service;

import org.ramer.diary.domain.User;

import java.util.List;

/**
 * Created by RAMER on 5/17/2017.
 */
public interface FollowService {
    /**
     * 获取关注用户的总数
     * @param user 登录用户
     * @return 整型值
     */
    int getFollowedNumber(User user);

    /**
     * 获取所有关注'我'的用户
     * @param user
     * @return
     */
    List<User> getFollowUser(User user);

    /**
     * 添加关注
     * @param user 用户
     * @param followedUser 被关注的用户
     * @return 关注成功返回true
     */
    boolean follow(User user, User followedUser);

    /**
     * 判断是否已关注该用户
     * @param user 用户
     * @param followedUser 被关注用户
     * @return 已关注返回true
     */
    boolean isFollowed(User user, User followedUser);

    /**
     * 取消关注用户
     * @param user 用户
     * @param followedUser 已关注用户
     * @return 取消成功返回true
     */
    boolean notFollow(User user, User followedUser);
}
