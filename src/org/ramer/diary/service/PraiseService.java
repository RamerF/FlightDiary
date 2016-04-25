package org.ramer.diary.service;

import java.util.ArrayList;
import java.util.List;

import org.ramer.diary.domain.Praise;
import org.ramer.diary.domain.Topic;
import org.ramer.diary.domain.User;
import org.ramer.diary.repository.PraiseRepository;
import org.ramer.diary.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PraiseService {

  @Autowired
  private PraiseRepository praiseRepository;
  @Autowired
  private TopicRepository topicRepository;

  /**
   * 获取已点赞分享的id
   * @param user 登录用户
   * @param other 当前浏览的用户
   * @return 返回所有已点赞且,被浏览用户分享的,分享id的集合
   */
  @Transactional
  public List<Integer> getPraiseTopicIds(User user, User other) {
    List<Integer> list = new ArrayList<>();
    list = praiseRepository.getTopicIdsByUserIdAndOtherId(user.getId(), other.getId());
    return list;

  }

  /**
   * 用户点赞
   * @param user 用户
   * @param topic 分享
   * @return 用户点赞成功返回true
   */
  @Transactional
  public boolean praise(User user, Topic topic) {

    // 判断是否已点过赞
    Praise p = praiseRepository.getByUserAndTopic(user, topic);
    //如果不为空说明用户已点过赞,取消本次操作
    if (p != null) {
      return false;
    }
    //将分享点赞次数加1
    topicRepository.updateTopicPraise(topic.getId());
    //更新点赞表
    Praise praise = new Praise();
    praise.setUser(user);
    praise.setTopic(topic);
    praiseRepository.saveAndFlush(praise);
    return true;
  }

  /**
   * 取消点赞
   * @param topic 分享
   * @param user  用户
   * @return 取消成功返回true
   */
  @Transactional
  public boolean notPraise(Topic topic, User user) {
    // 判断是否已点过赞
    Praise p = praiseRepository.getByUserAndTopic(user, topic);
    //如果为空说明用户已取消点赞,取消本次操作
    if (p == null) {
      return false;
    }
    //将点赞次数-1
    topicRepository.updateTopicNotPraise(topic.getId());
    praiseRepository.deleteByTopicAndUser(topic, user);
    return true;
  }
}