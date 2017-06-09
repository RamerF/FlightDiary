package org.ramer.diary.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.ramer.diary.domain.Favourite;
import org.ramer.diary.domain.Topic;
import org.ramer.diary.domain.User;
import org.ramer.diary.repository.FavouriteRepository;
import org.ramer.diary.service.FavouriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

@Slf4j
@Service
public class FavouriteServiceImpl implements FavouriteService {

    @Resource
    private FavouriteRepository favouriteRepository;

    @Override
    @Transactional
    public boolean favourite(User user, Topic topic) {
        Favourite f = favouriteRepository.getByUserAndTopic(user, topic);
        if (f != null) {
            log.debug("重复添加收藏");
            return false;
        }
        Favourite favourite = new Favourite();
        favourite.setUser(user);
        favourite.setTopic(topic);
        favouriteRepository.saveAndFlush(favourite);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Integer> getFavouriteTopicIds(User user, User other) {
        List<Integer> list = new ArrayList<>();
        list = favouriteRepository.getTopicIdsByUserIdAndOtherId(user.getId(), other.getId());
        return list;
    }

    @Override
    @Transactional
    public boolean notFavourite(User user, Topic topic) {
        favouriteRepository.deleteByUserAndTopic(user, topic);
        return true;
    }

}