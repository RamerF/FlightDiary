package org.ramer.diary.service.impl;

import java.util.Date;
import java.util.Set;

import org.ramer.diary.domain.Notify;
import org.ramer.diary.domain.User;
import org.ramer.diary.repository.NotifyRepository;
import org.ramer.diary.service.NotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class NotifyServiceImpl implements NotifyService {

    @Resource
    private NotifyRepository notifyRepository;

    @Override
    @Transactional(readOnly = true)
    public Set<Notify> getNotifies(User user, String hasCheck) {
        Set<Notify> notifies = notifyRepository.getByNotifiedUserAndHasCheckOrderByDateDesc(user, hasCheck);
        return notifies;
    }

    @Override
    @Transactional(readOnly = true)
    public Notify getNotifyById(Notify notify) {
        Notify n = notifyRepository.getById(notify.getId());
        return n == null ? new Notify() : n;
    }

    @Override
    @Transactional(readOnly = true)
    public int getNotifiedNumber(User user) {
        int number = notifyRepository.getCountByNotifiedUser(user);
        return number > 0 ? number : 0;
    }

    @Override
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

    @Override
    @Transactional
    public boolean sendPrivMess(Notify notify) {
        Notify n = notifyRepository.saveAndFlush(notify);
        return n != null;
    }

    @Override
    @Transactional
    public boolean updateNotify(Notify notify) {
        Notify n = notifyRepository.saveAndFlush(notify);
        return n != null;
    }

    @Override
    @Transactional
    public boolean delete(Notify notify) {
        notifyRepository.delete(notify);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public Notify getByIdAndNotifiedUserId(Integer notify_id, User notified_user) {
        return notifyRepository.getByIdAndNotifiedUser(notify_id, notified_user);
    }

}