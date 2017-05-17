package org.ramer.diary.service;

import org.ramer.diary.domain.Notify;
import org.ramer.diary.domain.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * Created by RAMER on 5/17/2017.
 */
public interface NotifyService {
    /**
     * 获取指定的消息
     * @param user 用户
     * @param hasCheck 消息是否已被读取
     * @return 满足hasCheck条件的所有消息
     */
    Set<Notify> getNotifies(User user, String hasCheck);

    /**
     * 通过消息UID获取消息
     * @param notify 消息
     * @return 返回一个消息
     */
    Notify getNotifyById(Notify notify);

    /**
     * 获取消息的总数
     * @param user 登录用户
     * @return 整型值
     */
    int getNotifiedNumber(User user);

    /**
     * 通知关注的人
     * @param user
     * @return
     */
    boolean notifyFollowUser(User user, User followUser, String message);

    /**
     * 发送私信
     * @param notify 消息
     * @return 发送成功返回true
     */
    boolean sendPrivMess(Notify notify);

    /**
     * 更新消息状态,将消息置为已读状态
     * @param notify 消息
     * @return 执行成功返回true
     */
    boolean updateNotify(Notify notify);

    /**
     * 删除消息.
     *
     * @param notify the notify
     * @return true, if successful
     */
    boolean delete(Notify notify);

    /**
     * 通过id和被通知用户id获取消息.
     *
     * @param notify_id the notify_id
     * @param notified_user_id the notified_user_id
     * @return 消息
     */
    Notify getByIdAndNotifiedUserId(Integer notify_id, User notified_user);
}
