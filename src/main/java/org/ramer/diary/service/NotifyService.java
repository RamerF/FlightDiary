package org.ramer.diary.service;

import org.ramer.diary.domain.Notify;
import org.ramer.diary.domain.User;
import org.ramer.diary.repository.NotifyRepository;

import java.util.Date;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotifyService{

    @Autowired
    private NotifyRepository notifyRepository;

    /**
     * 获取指定的消息
     * @param user 用户
     * @param hasCheck 消息是否已被读取
     * @return 满足hasCheck条件的所有消息
     */
    @Transactional(readOnly = true)
    public Set<Notify> getNotifies(User user, String hasCheck) {
        Set<Notify> notifies = notifyRepository.getByNotifiedUserAndHasCheckOrderByDateDesc(user, hasCheck);
        return notifies;
    }

    /**
     * 通过消息UID获取消息
     * @param notify 消息
     * @return 返回一个消息
     */
    @Transactional(readOnly = true)
    public Notify getNotifyById(Notify notify) {
        Notify n = notifyRepository.getById(notify.getId());
        return n == null ? new Notify() : n;
    }

    /**
     * 获取消息的总数
     * @param user 登录用户
     * @return 整型值
     */
    @Transactional(readOnly = true)
    public int getNotifiedNumber(User user) {
        int number = notifyRepository.getCountByNotifiedUser(user);
        return number > 0 ? number : 0;
    }

    /**
     * 通知关注的人
     * @param user
     * @return
     */
    @Transactional
    public boolean notifyFollowUser(User user, User followUser, String message) {
        Notify notify = new Notify();
        notify.setContent(message);
        notify.setDate(new Date());
        notify.setHasCheck("false");
        notify.setNotifiedUser(followUser);
        notify.setUser(user);
        notifyRepository.saveAndFlush(notify);
        return true;

    }

    /**
     * 发送私信
     * @param notify 消息
     * @return 发送成功返回true
     */
    @Transactional
    public boolean sendPrivMess(Notify notify) {
        Notify n = notifyRepository.saveAndFlush(notify);
        if (n == null) {
            return false;
        }
        return true;
    }

    /**
     * 更新消息状态,将消息置为已读状态
     * @param notify 消息
     * @return 执行成功返回true
     */
    @Transactional
    public boolean updateNotify(Notify notify) {
        Notify n = notifyRepository.saveAndFlush(notify);
        return n == null ? false : true;
    }

    /**
     * 删除消息.
     *
     * @param notify the notify
     * @return true, if successful
     */
    @Transactional
    public boolean delete(Notify notify) {
        notifyRepository.delete(notify);
        return true;
    }

    /**
     * 通过id和被通知用户id获取消息.
     *
     * @param notify_id the notify_id
     * @param notified_user_id the notified_user_id
     * @return 消息
     */
    @Transactional(readOnly = true)
    public Notify getByIdAndNotifiedUserId(Integer notify_id, User notified_user) {
        return notifyRepository.getByIdAndNotifiedUser(notify_id, notified_user);
    }

}