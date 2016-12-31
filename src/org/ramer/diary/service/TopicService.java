package org.ramer.diary.service;

import org.ramer.diary.domain.Topic;
import org.ramer.diary.domain.User;
import org.ramer.diary.repository.TopicRepository;
import org.ramer.diary.util.Pagination;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TopicService {

  @Autowired
  private TopicRepository topicRepository;

  /**
   * 用户发表日记
   * @param topic 用户分享,日记
   * @return 发表分享成功返回true
   */
  @Transactional
  public Topic publish(Topic topic) {
    Topic t = new Topic();
    try {
      t = topicRepository.save(topic);
    } catch (Exception exception) {
      return new Topic();
    }
    return t;
  }

  /**
   * 删除分享
   * @param topic 用户分享,日记
   */
  @Transactional
  public void deleteTopic(Topic topic) {
    System.out.println("删除topic");
    topicRepository.delete(topic.getId());
  }

  /**
   * 获取所有的标签，并根据出现的次数排序
   * @return 所有标签的集合
   */
  @Transactional(readOnly = true)
  public List<String> getAllTags() {
    List<String> cities = topicRepository.getOrderedTags();
    return cities;
  }

  /**
   * 按时间顺序获取所有分享的分页数据
   * @param page 当前页面
   * @param size 每一页的记录数
   * @return 返回分页数据
   */
  @Transactional(readOnly = true)
  public Page<Topic> getTopicsPage(int page, int size) {
    //页号从零开始
    page = page - 1;
    //按时间排序
    Order orders = new Order(Direction.DESC, "date");
    Sort sort = new Sort(orders);
    Pageable pageable = new PageRequest(page, size, sort);
    return topicRepository.findAll(pageable);
  }

  /**
   * 按热度获取所有分享的分页数据
   * @param page 当前页面
   * @param size 每一页的记录数
   * @return 返回分页数据
   */
  @Transactional(readOnly = true)
  public Page<Topic> getTopicsPageOrderByFavourite(int page, int size) {
    //页号从零开始
    page -= 1;
    //按热度排序
    Order order = new Order(Direction.DESC, "upCounts");
    Sort sort = new Sort(order);
    Pageable pageable = new PageRequest(page, size, sort);
    return topicRepository.findAll(pageable);
  }

  /**
   * 通过标签获取分享分页数据.
   *
   * @param tags the tags
   * @param page 当前页号
   * @param size 每页大小
   * @return 分享的分页数据
   */
  @Transactional(readOnly = true)
  public Pagination<Topic> getTopicsPageByTags(String tag, int page, int size) {
    page -= 1;
    int count = (int) getCountByTag(tag);
    int start = page * size;
    List<Topic> topics = topicRepository.getByTagsLimit(tag, start, size);
    if (topics.size() <= 0) {
      return new Pagination<>(new ArrayList<Topic>(), page, size, count);
    }
    Pagination<Topic> pageTopic = new Pagination<>(topics, page, size, count);
    return pageTopic;
  }

  /**
   * 通过用户UID获取所有分享.
   *
   * @param user 用户
   * @param page 页号
   * @param size 每页大小
   * @return 返回该用户的所有分享
   */
  @Transactional(readOnly = true)
  public Page<Topic> getTopicsPageByUserId(User user, int page, int size) {
    //页号从零开始
    page -= 1;
    Pageable pageable = new PageRequest(page, size);
    return topicRepository.getByUserOrderByDateDesc(user, pageable);
  }

  /**
   * 通过分享id获取分享
   * @param topic_id 分享UID
   * @return 返回一个分享
   */
  @Transactional(readOnly = true)
  public Topic getTopicById(Integer topic_id) {
    return topicRepository.getById(topic_id);
  }

  /**
   * 通过用户id和分享id获取分享
   * @param topic_id 分享UID
   * @param user 用户UID
   * @return 获取到分享返回true
   */
  @Transactional(readOnly = true)
  public boolean getTopicByUserIdAndTopicId(Integer topic_id, User user) {
    Topic topic = topicRepository.getByIdAndUser(topic_id, user);
    if (topic == null) {
      return false;
    }
    return true;
  }

  /**
   * 获取分享的总数
   * @param user 登录用户
   * @return 整型值
   */
  @Transactional(readOnly = true)
  public int getTopicNumber(User user) {
    int number = topicRepository.getCountByUser(user);
    return number > 0 ? number : 0;
  }

  /**
   * 获取动态的总数.
   *
   * @return 动态总数
   */
  @Transactional(readOnly = true)
  public long getCount() {
    return topicRepository.count();
  }

  /**
   * 获取相应标签的动态的总数.
   *
   * @return 动态总数
   */
  @Transactional(readOnly = true)
  public long getCountByTag(String tag) {
    return topicRepository.getCountByTag(tag);
  }

}