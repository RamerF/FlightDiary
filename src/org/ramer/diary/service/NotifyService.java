package org.ramer.diary.service;

import java.util.Date;
import java.util.Set;

import org.ramer.diary.domain.Notifying;
import org.ramer.diary.domain.User;
import org.ramer.diary.repository.NotifyingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotifyService {

  @Autowired
  private NotifyingRepository notifyingRepository;

  /**
   * 获取指定的消息
   * @param user 用户
   * @param hasCheck 消息是否已被读取
   * @return 满足hasCheck条件的所有消息
   */
  @Transactional(readOnly = true)
  public Set<Notifying> getNotifyings(User user, String hasCheck) {
    Set<Notifying> notifyings = notifyingRepository.getByNotifiedUserAndHasCheck(user, hasCheck);
    return notifyings;
  }

  /**
   * 通过消息UID获取消息
   * @param notifying 消息
   * @return 返回一个消息
   */
  @Transactional(readOnly = true)
  public Notifying getNotifyingById(Notifying notifying) {
    Notifying n = notifyingRepository.getById(notifying.getId());
    return n == null ? new Notifying() : n;
  }

  /**
   * 获取消息的总数
   * @param user 登录用户
   * @return 整型值
   */
  @Transactional(readOnly = true)
  public int getNotifiedNumber(User user) {
    int number = notifyingRepository.getCountByNotifiedUser(user);
    return number > 0 ? number : 0;
  }

  /**
   * 通知关注的人
   * @param user
   * @return
   */
  @Transactional
  public boolean notifyFollowUser(User user, User followUser, String message) {
    Notifying notifying = new Notifying();
    notifying.setContent(message);
    notifying.setDate(new Date());
    notifying.setHasCheck("false");
    notifying.setNotifiedUser(followUser);
    notifying.setUser(user);
    notifyingRepository.saveAndFlush(notifying);
    return true;

  }

  /**
   * 发送私信
   * @param notifying 消息
   * @return 发送成功返回true
   */
  @Transactional
  public boolean sendPrivMess(Notifying notifying) {
    Notifying n = notifyingRepository.saveAndFlush(notifying);
    if (n == null) {
      return false;
    }
    return true;
  }

  /**
   * 更新消息状态,将消息置为已读状态
   * @param notifying 消息
   * @return 执行成功返回true
   */
  @Transactional
  public boolean updateNotifying(Notifying notifying) {
    Notifying n = notifyingRepository.saveAndFlush(notifying);
    System.out.println(n.getHasCheck());
    return n == null ? false : true;
  }

}