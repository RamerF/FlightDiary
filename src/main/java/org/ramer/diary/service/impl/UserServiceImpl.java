/*
 *
 */
package org.ramer.diary.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.ramer.diary.domain.FeedBack;
import org.ramer.diary.domain.User;
import org.ramer.diary.repository.FeedBackRepository;
import org.ramer.diary.repository.UserRepository;
import org.ramer.diary.service.UserService;
import org.ramer.diary.util.EncryptUtil;
import org.ramer.diary.util.IntegerUtil;
import org.ramer.diary.util.Pagination;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ramer
 *
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService{
    @Resource
    private UserRepository userRepository;
    @Resource
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
        if (user.getUsername() != null) {
            u = userRepository.getByUsernameAndPassword(user.getUsername(), user.getPassword());
        } else {
            u = userRepository.getByEmailAndPassword(user.getEmail(), user.getPassword());
        }
        return u != null ? u : new User();
    }

    @Override
    @Transactional
    public boolean newOrUpdate(User user) {
        user = userRepository.saveAndFlush(user);
        return IntegerUtil.isPositiveValue(user.getId());
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
            return userRepository.getByEmail(EncryptUtil.execEncrypt(username));
        }
        return userRepository.getByUsername(username);
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
        return f != null;
    }
}
