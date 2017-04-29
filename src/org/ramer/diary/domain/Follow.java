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

/**
 * 关注.
 *
 * @author ramer
 */
@Cacheable
@Entity
public class Follow {

  /** UID. */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column
  private Integer id;

  /** 用户. */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user")
  private User user;

  /** 被关注用户. */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "followed_user")
  private User followedUser;

  /**
   * Instantiates a new follow.
   */
  public Follow() {
  }

  /**
   * Instantiates a new follow.
   *
   * @param id the id
   * @param user the user
   * @param followedUser the followed user
   */
  public Follow(Integer id, User user, User followedUser) {
    super();
    this.id = id;
    this.user = user;
    this.followedUser = followedUser;
  }

  /**
   * Sets the followed user.
   *
   * @param followedUser the new followed user
   */
  public void setFollowedUser(User followedUser) {
    this.followedUser = followedUser;
  }

  /**
   * Gets the followed user.
   *
   * @return the followed user
   */
  public User getFollowedUser() {
    return followedUser;
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
   * Gets the user.
   *
   * @return the user
   */
  public User getUser() {
    return user;
  }
}
