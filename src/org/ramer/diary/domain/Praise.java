/*
 *
 */
package org.ramer.diary.domain;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 * 点赞类.
 *
 * @author ramer
 */
@Cacheable
@Entity
public class Praise {

  /** UID. */
  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  /** 点赞用户. */
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user")
  private User user;

  /** 分享. */
  //被点赞的分享
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "topic")
  private Topic topic;

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

  /**
   * Instantiates a new praise.
   *
   * @param id the id
   * @param user the user
   * @param topic the topic
   */
  public Praise(Integer id, User user, Topic topic) {
    super();
    this.id = id;
    this.user = user;
    this.topic = topic;
  }

  /**
   * Instantiates a new praise.
   */
  public Praise() {
  }
}
