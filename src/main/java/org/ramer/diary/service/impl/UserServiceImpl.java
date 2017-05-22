/*
 *
 */
package org.ramer.diary.service.impl;

import java.util.List;

import org.ramer.diary.domain.FeedBack;
import org.ramer.diary.domain.User;
import org.ramer.diary.repository.FeedBackRepository;
import org.ramer.diary.repository.UserRepository;
import org.ramer.diary.service.UserService;
import org.ramer.diary.util.Encrypt;
import org.ramer.diary.util.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

/**
 * @author ramer
 *
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FeedBackRepository feedBackRepository;

    @Override
    @Transactional(readOnly = true)
    public Pagination<User> getTopPeople(int page, int size) {
        page -= 1;
        int start = page * size;
        List<User> users = userRepository.getByIdJoinTopicUserId(start, size);
        int count = (int) userRepository.count();
        Pagination<User> pageUser = new Pagination<>(users, page, size, count);
        return pageUser;
    }

    @Override
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

    @Override
    @Transactional
    public User newOrUpdate(User user) {
        //    userRepository.testUpdate(user.getSays(), user.getId());
        User u = userRepository.saveAndFlush(user);
        return u;

    }

    @Override
    @Transactional(readOnly = true)
    public User getById(Integer id) {
        return userRepository.getById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public User getByName(String username) {
        String regex = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
        if (username.matches(regex)) {
            return userRepository.getByEmail(Encrypt.execEncrypt(username, true));
        }
        return userRepository.getByName(username);
    }

    @Override
    @Transactional
    public void updateHead(User user) {
        userRepository.saveAndFlush(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User getByEmail(String email) {
        return userRepository.getByEmail(email);
    }

    @Override
    @Transactional(readOnly = false)
    public boolean feedback(FeedBack feedBack) {
        FeedBack f = feedBackRepository.saveAndFlush(feedBack);
        if (f == null) {
            return false;
        }
        return true;
    }
}
