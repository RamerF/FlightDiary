package org.ramer.diary.domain;

import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 评论回复.
 *
 * @author ramer
 */
@Cacheable
@Entity
public class Reply {

  /** The id. */
  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  /** 回复内容. */
  @Column(nullable = false)
  private String content;

  /** 时间. */
  @Temporal(value = TemporalType.TIMESTAMP)
  private Date date;

  /** 回复的评论. */
  @ManyToOne()
  @JoinColumn(name = "comment", nullable = false)
  private Comment comment;

  /** 回复的用户. */
  @OneToOne
  @JoinColumn(name = "user", nullable = false)
  private User user;

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
   * Gets the comment.
   *
   * @return the comment
   */
  public Comment getComment() {
    return comment;
  }

  /**
   * Sets the comment.
   *
   * @param comment the new comment
   */
  public void setComment(Comment comment) {
    this.comment = comment;
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
   * Gets the user.
   *
   * @return the user
   */
  public User getUser() {
    return user;
  }

}
