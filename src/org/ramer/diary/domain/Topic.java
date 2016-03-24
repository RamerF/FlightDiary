/*
 *
 */
package org.ramer.diary.domain;

import java.util.Date;
import java.util.HashSet;
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
import javax.persistence.OrderBy;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 分享类.
 *
 * @author ramer
 */
@Cacheable
@Entity
public class Topic {

  /** UID. */
  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  /** 分享内容. */
  @Column(name = "content")
  private String content;

  /** 时间. */
  @Temporal(value = TemporalType.TIMESTAMP)
  private Date date;

  /** 图片. */
  @Column
  private String picture;

  /** 点赞次数. */
  @Column
  private Integer upCounts;

  /** 用户. */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user")
  private User user;

  /** 评论. */
  //对应多个用户评论
  @OrderBy("date")
  @Column
  @OneToMany(cascade = { CascadeType.REMOVE }, mappedBy = "topic")
  private Set<Comment> comments = new HashSet<>();

  /**
   * Gets the up counts.
   *
   * @return the up counts
   */
  public Integer getUpCounts() {
    return upCounts;
  }

  /**
   * Sets the up counts.
   *
   * @param upCounts the new up counts
   */
  public void setUpCounts(Integer upCounts) {
    this.upCounts = upCounts;
  }

  /**
   * Gets the comments.
   *
   * @return the comments
   */
  public Set<Comment> getComments() {
    return comments;
  }

  /**
   * Sets the comments.
   *
   * @param comments the new comments
   */
  public void setComments(Set<Comment> comments) {
    this.comments = comments;
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
   * Gets the picture.
   *
   * @return the picture
   */
  public String getPicture() {
    return picture;
  }

  /**
   * Sets the picture.
   *
   * @param picture the new picture
   */
  public void setPicture(String picture) {
    this.picture = picture;
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
   * Instantiates a new topic.
   *
   * @param id the id
   * @param content the content
   * @param date the date
   * @param picture the picture
   * @param upCounts the up counts
   * @param user the user
   * @param comments the comments
   */
  public Topic(Integer id, String content, Date date, String picture, Integer upCounts, User user,
      Set<Comment> comments) {
    super();
    this.id = id;
    this.content = content;
    this.date = date;
    this.picture = picture;
    this.upCounts = upCounts;
    this.user = user;
    this.comments = comments;
  }

  /**
   * Instantiates a new topic.
   */
  public Topic() {
  }

}
