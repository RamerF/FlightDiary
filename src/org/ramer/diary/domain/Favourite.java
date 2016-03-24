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
 * 收藏.
 *
 * @author ramer
 */
@Cacheable
@Entity
public class Favourite {

  /** UID. */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column
  private Integer id;

  /** 用户. */
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user")
  private User user;

  /** 分享. */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "topic")
  private Topic topic;

  /**
   * Instantiates a new favourite.
   */
  public Favourite() {
  }

  /**
   * Instantiates a new favourite.
   *
   * @param id the id
   * @param user the user
   * @param topic the topic
   */
  public Favourite(Integer id, User user, Topic topic) {
    super();
    this.id = id;
    this.user = user;
    this.topic = topic;
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
   * Sets the topic.
   *
   * @param topic the new topic
   */
  public void setTopic(Topic topic) {
    this.topic = topic;
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
   * Gets the topic.
   *
   * @return the topic
   */
  public Topic getTopic() {
    return topic;
  }

  /**
   * Gets the user.
   *
   * @return the user
   */
  public User getUser() {
    return user;
  }

}
