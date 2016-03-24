/*
 *
 */
package test;

import java.util.Date;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import org.ramer.diary.domain.Comment;
import org.ramer.diary.domain.Reply;
import org.ramer.diary.domain.Topic;
import org.ramer.diary.repository.TopicRepository;
import org.ramer.diary.service.UserService;

/**
 * @author ramer
 *
 */
@ContextConfiguration("/applicationContext.xml")
@RunWith(value = SpringJUnit4ClassRunner.class)
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class CommentReplyTest {
  @Autowired
  UserService userService;
  @Autowired
  TopicRepository topicRepository;

  /**
   * 测试获取评论
   */
  @Test
  public void testGetComments() {
    //    ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
    //        "applicationContext.xml");
    //    AutowireCapableBeanFactory factory = applicationContext.getAutowireCapableBeanFactory();
    //    UserService userService = factory.getBean(UserService.class);
    Topic topic = userService.getTopicById(1);
    Set<Comment> comments = topic.getComments();
    if (comments != null) {
      for (Comment comment : comments) {
        System.out.println(comment.getUser().getName() + " : " + comment.getContent());
      }
    }

  }

  /**
   * 测试回复评论
   */
  @Test
  public void testReplyComment() {
    Comment comment = new Comment();
    Reply reply = new Reply();
    Topic topic = userService.getTopicById(1);
    Set<Comment> comments = topic.getComments();
    for (Comment comment2 : comments) {
      if (comment2.getUser().getId() == 3) {
        comment = comment2;
      }
    }
    reply.setComment(comment);
    reply.setDate(new Date());
    userService.replyComment(reply);
  }

  /**
   * Test name.
   *
   * @throws Exception the exception
   */
  @Test

  public void testDeleteCommentWithReply() throws Exception {
    Comment comment = new Comment();
    comment.setId(1);
    Topic topic = new Topic();
    topic.setId(1);
    comment.setTopic(topic);
    userService.deleteComment(comment);
    topic = topicRepository.getById(1);
    Set<Comment> comments = topic.getComments();
    for (Comment comment2 : comments) {
      System.out.println(comment2.getContent());
    }
  }
}
