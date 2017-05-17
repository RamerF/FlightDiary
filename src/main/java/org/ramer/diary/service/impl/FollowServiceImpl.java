package org.ramer.diary.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.ramer.diary.domain.Follow;
import org.ramer.diary.domain.User;
import org.ramer.diary.repository.FollowRepository;
import org.ramer.diary.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class FollowServiceImpl implements FollowService{

    @Autowired
    private FollowRepository followRepository;

    @Override
    @Transactional(readOnly = true)
    public int getFollowedNumber(User user) {
        int number = followRepository.getCountByUser(user);
        return number > 0 ? number : 0;
    }

    @Override
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

    @Override
    @Transactional
    public boolean follow(User user, User followedUser) {
        if (isFollowed(user, followedUser)) {
            log.debug("重复添加关注");
            return false;
        }
        Follow follow = new Follow();
        follow.setUser(user);
        follow.setFollowedUser(followedUser);
        followRepository.saveAndFlush(follow);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isFollowed(User user, User followedUser) {
        Follow f = followRepository.getByUserAndFollowedUser(user, followedUser);
        return (f != null) ? true : false;
    }

    @Override
    @Transactional
    public boolean notFollow(User user, User followedUser) {
        followRepository.deleteByUserAndFollowedUser(user, followedUser);
        return true;
    }
}