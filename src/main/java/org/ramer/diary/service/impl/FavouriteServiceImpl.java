package org.ramer.diary.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.ramer.diary.domain.*;
import org.ramer.diary.repository.FavouriteRepository;
import org.ramer.diary.service.FavouriteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class FavouriteServiceImpl implements FavouriteService{

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
        return favouriteRepository.getTopicIdsByUserIdAndOtherId(user.getId(), other.getId());
    }

    @Override
    @Transactional
    public boolean notFavourite(User user, Topic topic) {
        favouriteRepository.deleteByUserAndTopic(user, topic);
        return true;
    }

}