/*
 *
 */
package org.ramer.diary.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.ramer.diary.domain.Comment;
import org.ramer.diary.domain.Favourite;
import org.ramer.diary.domain.Follow;
import org.ramer.diary.domain.Notifying;
import org.ramer.diary.domain.Praise;
import org.ramer.diary.domain.Reply;
import org.ramer.diary.domain.Topic;
import org.ramer.diary.domain.User;
import org.ramer.diary.repository.CommentRepository;
import org.ramer.diary.repository.FavouriteRepository;
import org.ramer.diary.repository.FollowRepository;
import org.ramer.diary.repository.NotifyingRepository;
import org.ramer.diary.repository.PraiseRepository;
import org.ramer.diary.repository.ReplyRepository;
import org.ramer.diary.repository.TopicRepository;
import org.ramer.diary.repository.UserRepository;
import org.ramer.diary.util.Encrypt;
import org.ramer.diary.util.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author ramer
 *
 */
@Service
public class UserService {
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private TopicRepository topicRepository;
  @Autowired
  private PraiseRepository praiseRepository;
  @Autowired
  private CommentRepository commentRepository;
  @Autowired
  private ReplyRepository replyRepository;
  @Autowired
  private FollowRepository followRepository;
  @Autowired
  private FavouriteRepository favouriteRepository;
  @Autowired
  private NotifyingRepository notifyingRepository;

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
    page = page - 1;
    //按热度排序
    Order order = new Order(Direction.DESC, "upCounts");
    Sort sort = new Sort(order);
    Pageable pageable = new PageRequest(page, size, sort);
    return topicRepository.findAll(pageable);
  }

  /**
   * 获取用户,按分享的数量排序
   * @param page 当前页号
   * @param 每页记录数
   * @return 用户的分页记录
   */
  @Transactional(readOnly = true)
  public Pagination<User> getTopPeople(int page, int size) {
    List<User> users = userRepository.getByIdJoinTopicUserId();
    Pagination<User> pageUser = new Pagination<>(users, page, size);
    return pageUser;
  }

  /**
   * 获取所有的城市
   * @return 所有城市的集合
   */
  public List<String> getAllCities() {
    List<String> cities = topicRepository.getOrderedCity();
    return cities;
  }

  /**
   * 通过城市名称获取分享
   * @param city 城市名称
   * @param page 当前页号
   * @param size 每页大小
   * @return 分享的分页数据
   */

  @Transactional(readOnly = true)
  public Pagination<Topic> getTopicsPageByCity(String city, int page, int size) {
    List<Topic> topics = topicRepository.getByCity(city);
    if (topics.size() <= 0) {
      return new Pagination<>(new ArrayList<Topic>(), page, size);
    }
    Pagination<Topic> pageTopic = new Pagination<>(topics, page, size);
    return pageTopic;
  }

  /**
   * 用户登录
   * @param user 用户
   * @return 返回登录用户信息
   */
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

  /**
   * 注册或更新用户
   * @param user 用户
   * @return 返回注册成功的用户信息或已更新的用户信息
   */
  @Transactional
  public User newOrUpdate(User user) {
    //    userRepository.testUpdate(user.getSays(), user.getId());
    User u = userRepository.saveAndFlush(user);
    return u;

  }

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
   * 获取所有关注'我'的用户
   * @param user
   * @return
   */
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

  /**
   * @param user
   * @return
   */
  @Transactional
  public boolean notifyFollowUser(User user, User followUser, String message) {
    Notifying notifying = new Notifying();
    notifying.setContent(message);
    notifying.setDate(new Date());
    notifying.setHasCheck("false");
    notifying.setNotifiedUser(followUser);
    notifying.setUser(user);
    notifyingRepository.saveAndFlush(notifying);
    return true;

  }

  /**
   * 删除分享
   * @param topic 用户分享,日记
   */
  @Transactional
  public void deleteTopic(Topic topic) {
    //先删除praise,因为它关联了topic
    System.out.println("删除praise");
    praiseRepository.deleteByTopic(topic);
    System.out.println("删除topic");
    topicRepository.delete(topic.getId());
  }

  /**
   * 通过UID获取用户
   * @param id 用户UID
   * @return 返回一个用户
   */
  @Transactional(readOnly = true)
  public User getById(Integer id) {
    return userRepository.getById(id);
  }

  /**
   * 通过用户名获取用户
   * @param username 用户名或邮箱
   * @return 返回一个用户
   */
  @Transactional(readOnly = true)
  public User getByName(String username) {
    String regex = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    if (username.matches(regex)) {
      System.out.println("邮箱");
      return userRepository.getByEmail(Encrypt.execEncrypt(username, true));
    } else {
      System.out.println("用户名");
      return userRepository.getByName(username);
    }
  }

  /**
   * 更新用户头像
   * @param user 要更新的用户
   */
  @Transactional
  public void updateHead(User user) {
    userRepository.saveAndFlush(user);
  }

  /**
   * 通过用户UID获取所有分享
   * @param user 用户
   * @return 返回该用户的所有分享
   */
  @Transactional(readOnly = true)
  public List<Topic> getTopicsByUserId(User user) {
    return topicRepository.getByUserOrderByDateAsc(user);
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
   * 添加关注
   * @param user 用户
   * @param followedUser 被关注的用户
   * @return 关注成功返回true
   */
  @Transactional
  public boolean follow(User user, User followedUser) {
    if (isFollowed(user, followedUser)) {
      System.out.println("重复添加关注");
      return false;
    }
    Follow follow = new Follow();
    follow.setUser(user);
    follow.setFollowedUser(followedUser);
    followRepository.saveAndFlush(follow);
    return true;
  }

  /**
   * 取消关注用户
   * @param user 用户
   * @param followedUser 已关注用户
   * @return 取消成功返回true
   */
  @Transactional
  public boolean notFollow(User user, User followedUser) {
    followRepository.deleteByUserAndFollowedUser(user, followedUser);
    return true;
  }

  /**
   * 判断是否已关注该用户
   * @param user 用户
   * @param followedUser 被关注用户
   * @return 已关注返回true
   */
  @Transactional(readOnly = true)
  public boolean isFollowed(User user, User followedUser) {
    Follow f = followRepository.getByUserAndFollowedUser(user, followedUser);
    return (f != null) ? true : false;
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
   * 添加收藏
   * @param user 用户
   * @param topic 分享
   * @return 添加收藏成功返回true
   */
  @Transactional
  public boolean favourite(User user, Topic topic) {
    Favourite f = favouriteRepository.getByUserAndTopic(user, topic);
    if (f != null) {
      System.out.println("重复添加收藏");
      return false;
    }
    Favourite favourite = new Favourite();
    favourite.setUser(user);
    favourite.setTopic(topic);
    favouriteRepository.saveAndFlush(favourite);
    return true;
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
   * 用户评论
   * @param comment 用户评论
   */
  @Transactional
  public void comment(Comment comment) {
    commentRepository.saveAndFlush(comment);
  }

  /**
   * 删除评论
   * @param comment 用户评论
   * @return 删除评论成功返回true
   */
  @Transactional
  public boolean deleteComment(Comment comment) {
    Comment c = commentRepository.getByIdAndTopic(comment.getId(), comment.getTopic());
    if (c == null) {
      return false;
    }
    commentRepository.delete(comment.getId());
    return true;

  }

  /**
   * 回复评论.
   *
   * @param reply 回复类
   * @return 回复成功返回true
   */
  @Transactional
  public boolean replyComment(Reply reply) {
    replyRepository.saveAndFlush(reply);
    return true;
  }

  /**
   * 通过回复ID获取评论ID
   *
   * @param reply_id 回复ID
   * @return 评论ID
   */
  @Transactional(readOnly = true)
  public Integer getCommentIdByReplyId(Integer reply_id) {
    return replyRepository.getCommentByReply(reply_id);
  }

  /**
   * 回复一个回复
   *
   * @param reply 回复
   */
  @Transactional
  public void replyReply(Reply reply) {
    replyRepository.saveAndFlush(reply);
  }

  /**
   * @param id 回复ID
   * @return 操作成功返回true
   */
  @Transactional
  public boolean deleteReply(Integer id) {
    replyRepository.delete(id);
    return true;
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
   * 发送私信
   * @param notifying 消息
   * @return 发送成功返回true
   */
  @Transactional
  public boolean sendPrivMess(Notifying notifying) {
    Notifying n = notifyingRepository.saveAndFlush(notifying);
    if (n == null) {
      return false;
    }
    return true;
  }

  /**
   * 获取指定的消息
   * @param user 用户
   * @param hasCheck 消息是否已被读取
   * @return 满足hasCheck条件的所有消息
   */
  @Transactional(readOnly = true)
  public Set<Notifying> getNotifyings(User user, String hasCheck) {
    Set<Notifying> notifyings = notifyingRepository.getByNotifiedUserAndHasCheck(user, hasCheck);
    return notifyings;
  }

  /**
   * 更新消息状态,将消息置为已读状态
   * @param notifying 消息
   * @return 执行成功返回true
   */
  @Transactional
  public boolean updateNotifying(Notifying notifying) {
    Notifying n = notifyingRepository.saveAndFlush(notifying);
    System.out.println(n.getHasCheck());
    return n == null ? false : true;
  }

  /**
   * 通过消息UID获取消息
   * @param notifying 消息
   * @return 返回一个消息
   */
  @Transactional(readOnly = true)
  public Notifying getNotifyingById(Notifying notifying) {
    Notifying n = notifyingRepository.getById(notifying.getId());
    return n == null ? new Notifying() : n;
  }

  /**
   * 通过邮箱获取用户
   *
   * @param email the email
   * @return the by email
   */
  @Transactional(readOnly = true)
  public User getByEmail(String email) {
    return userRepository.getByEmail(email);
  }

  /**
   * 获取消息的总数
   * @param user 登录用户
   * @return 整型值
   */
  @Transactional(readOnly = true)
  public int getNotifiedNumber(User user) {
    int number = notifyingRepository.getCountByNotifiedUser(user);
    return number > 0 ? number : 0;
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
   * 获取关注用户的总数
   * @param user 登录用户
   * @return 整型值
   */
  @Transactional(readOnly = true)
  public int getFollowedNumber(User user) {
    int number = followRepository.getCountByUser(user);
    return number > 0 ? number : 0;
  }

}
