/*
 *
 */
package org.ramer.diary.domain;

import java.util.Date;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 用户评论.
 *
 * @author ramer
 */
@Cacheable
@Entity
public class Comment {

  /** UID. */
  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  /** 评论内容. */
  @Column
  private String content;

  /** 时间. */
  @Temporal(value = TemporalType.TIMESTAMP)
  private Date date;

  /**分享. */
  //评论的分享
  @ManyToOne
  @JoinColumn(name = "topic")
  private Topic topic;

  /** 用户. */
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user")
  private User user;

  /** 评论回复. */
  @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE)
  @OrderBy("date asc")
  private Set<Reply> replies;

  /**
   * Sets the sub comments.
   *
   * @param replies 回复
   */
  public void setReplies(Set<Reply> replies) {
    this.replies = replies;
  }

  /**
   * Gets the replies.
   *
   * @return the replies
   */

  public Set<Reply> getReplies() {
    return replies;
  }

  /**
   * Gets the date.
   *
   * @return the date
   */
  public Date getDate() {
    return date;
  }

  /**
   * Sets the date.
   *
   * @param date the new date
   */
  public void setDate(Date date) {
    this.date = date;
  }

  /**
   * Gets the user.
   *
   * @return the user
   */
  public User getUser() {
    return user;
  }

  /**
   * Sets the user.
   *
   * @param user the new user
   */
  public void setUser(User user) {
    this.user = user;
  }

  /**
   * Gets the id.
   *
   * @return the id
   */
  public Integer getId() {
    return id;
  }

  /**
   * Sets the id.
   *
   * @param id the new id
   */
  public void setId(Integer id) {
    this.id = id;
  }

  /**
   * Gets the content.
   *
   * @return the content
   */
  public String getContent() {
    return content;
  }

  /**
   * Sets the content.
   *
   * @param content the new content
   */
  public void setContent(String content) {
    this.content = content;
  }

  /**
   * Gets the topic.
   *
   * @return the topic
   */
  public Topic getTopic() {
    return topic;
  }

  /**
   * Sets the topic.
   *
   * @param topic the new topic
   */
  public void setTopic(Topic topic) {
    this.topic = topic;
  }

}
