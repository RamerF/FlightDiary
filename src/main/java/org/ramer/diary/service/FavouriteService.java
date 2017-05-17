package org.ramer.diary.service;

import lombok.extern.slf4j.Slf4j;
import org.ramer.diary.domain.Favourite;
import org.ramer.diary.domain.Topic;
import org.ramer.diary.domain.User;
import org.ramer.diary.repository.FavouriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
@Slf4j
@Service
public class FavouriteService {

  @Autowired
  private FavouriteRepository favouriteRepository;

  /**
   * 添加收藏
   * @param user 用户
   * @param topic 分享
   * @return 添加收藏成功返回true
   */
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

  /**
   * 获取已收藏分享的id
   * @param user 登录用户
   * @param other 当前浏览用户
   * @return 相对于当前用户的所有已收藏分享id的集合
   */
  @Transactional(readOnly = true)
  public List<Integer> getFavouriteTopicIds(User user, User other) {
    List<Integer> list = new ArrayList<>();
    list = favouriteRepository.getTopicIdsByUserIdAndOtherId(user.getId(), other.getId());
    return list;
  }

  /**
   * 取消收藏
   * @param user 用户
   * @param topic 收藏的分享
   * @return 取消成功返回true
   */
  @Transactional
  public boolean notFavourite(User user, Topic topic) {
    favouriteRepository.deleteByUserAndTopic(user, topic);
    return true;
  }

}