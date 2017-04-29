package org.ramer.diary.service;

import java.util.ArrayList;
import java.util.List;

import org.ramer.diary.domain.Follow;
import org.ramer.diary.domain.User;
import org.ramer.diary.repository.FollowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FollowService {

  @Autowired
  private FollowRepository followRepository;

  /**
   * 获取关注用户的总数
   * @param user 登录用户
   * @return 整型值
   */
  @Transactional(readOnly = true)
  public int getFollowedNumber(User user) {
    int number = followRepository.getCountByUser(user);
    return number > 0 ? number : 0;
  }

  /**
   * 获取所有关注'我'的用户
   * @param user
   * @return
   */
  @Transactional(readOnly = true)
  public List<User> getFollowUser(User user) {
    List<Integer> userids = followRepository.getUserByFollowedUser(user);
    List<User> users = new ArrayList<>();
    for (Integer userid : userids) {
      User u = new User();
      u.setId(userid);
      users.add(u);
    }
    return users;
  }

  /**
   * 添加关注
   * @param user 用户
   * @param followedUser 被关注的用户
   * @return 关注成功返回true
   */
  @Transactional
  public boolean follow(User user, User followedUser) {
    if (isFollowed(user, followedUser)) {
      System.out.println("重复添加关注");
      return false;
    }
    Follow follow = new Follow();
    follow.setUser(user);
    follow.setFollowedUser(followedUser);
    followRepository.saveAndFlush(follow);
    return true;
  }

  /**
   * 判断是否已关注该用户
   * @param user 用户
   * @param followedUser 被关注用户
   * @return 已关注返回true
   */
  @Transactional(readOnly = true)
  public boolean isFollowed(User user, User followedUser) {
    Follow f = followRepository.getByUserAndFollowedUser(user, followedUser);
    return (f != null) ? true : false;
  }

  /**
   * 取消关注用户
   * @param user 用户
   * @param followedUser 已关注用户
   * @return 取消成功返回true
   */
  @Transactional
  public boolean notFollow(User user, User followedUser) {
    followRepository.deleteByUserAndFollowedUser(user, followedUser);
    return true;
  }
}