package org.ramer.diary.service;

import java.util.List;

import org.ramer.diary.domain.Topic;
import org.ramer.diary.domain.User;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by RAMER on 5/18/2017.
 */
public interface FavouriteService {
    @Transactional
    boolean favourite(User user, Topic topic);

    @Transactional(readOnly = true)
    List<Integer> getFavouriteTopicIds(User user, User other);

    @Transactional
    boolean notFavourite(User user, Topic topic);
}
