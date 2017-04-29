/*
 *
 */
package org.ramer.diary.service;

import java.util.List;

import org.ramer.diary.domain.FeedBack;
import org.ramer.diary.domain.User;
import org.ramer.diary.repository.FeedBackRepository;
import org.ramer.diary.repository.UserRepository;
import org.ramer.diary.util.Encrypt;
import org.ramer.diary.util.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author ramer
 *
 */
@Service
public class UserService {
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private FeedBackRepository feedBackRepository;

  /**
   * 获取用户,按分享的数量排序
   * @param page 当前页号
   * @param 每页记录数
   * @return 用户的分页记录
   */
  @Transactional(readOnly = true)
  public Pagination<User> getTopPeople(int page, int size) {
    page -= 1;
    int start = page * size;
    List<User> users = userRepository.getByIdJoinTopicUserId(start, size);
    int count = (int) userRepository.count();
    Pagination<User> pageUser = new Pagination<>(users, page, size, count);
    return pageUser;
  }

  /**
   * 用户登录
   * @param user 用户
   * @return 返回登录用户信息
   */
  @Transactional(readOnly = true)
  public User login(User user) {
    User u = null;
    if (user.getName() != null) {
      u = userRepository.getByNameAndPassword(user.getName(), user.getPassword());
    } else {
      u = userRepository.getByEmailAndPassword(user.getEmail(), user.getPassword());
    }
    return u != null ? u : new User();
  }

  /**
   * 注册或更新用户
   * @param user 用户
   * @return 返回注册成功的用户信息或已更新的用户信息
   */
  @Transactional
  public User newOrUpdate(User user) {
    //    userRepository.testUpdate(user.getSays(), user.getId());
    User u = userRepository.saveAndFlush(user);
    return u;

  }

  /**
   * 通过UID获取用户
   * @param id 用户UID
   * @return 返回一个用户
   */
  @Transactional(readOnly = true)
  public User getById(Integer id) {
    return userRepository.getById(id);
  }

  /**
   * 通过用户名获取用户
   * @param username 用户名或邮箱
   * @return 返回一个用户
   */
  @Transactional(readOnly = true)
  public User getByName(String username) {
    String regex = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    if (username.matches(regex)) {
      System.out.println("邮箱");
      return userRepository.getByEmail(Encrypt.execEncrypt(username, true));
    } else {
      System.out.println("用户名");
      return userRepository.getByName(username);
    }
  }

  /**
   * 更新用户头像
   * @param user 要更新的用户
   */
  @Transactional
  public void updateHead(User user) {
    userRepository.saveAndFlush(user);
  }

  /**
   * 通过邮箱获取用户
   *
   * @param email the email
   * @return the by email
   */
  @Transactional(readOnly = true)
  public User getByEmail(String email) {
    return userRepository.getByEmail(email);
  }

  /**
   * 用户反馈.
   *
   * @param feedBack 反馈信息
   */
  @Transactional(readOnly = false)
  public boolean feedback(FeedBack feedBack) {
    FeedBack f = feedBackRepository.saveAndFlush(feedBack);
    if (f == null) {
      return false;
    }
    return true;
  }
}
