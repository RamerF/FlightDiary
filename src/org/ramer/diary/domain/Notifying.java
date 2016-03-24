/*
 * 
 */
package org.ramer.diary.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

// TODO: Auto-generated Javadoc
/**
 * 用户通知.
 *
 * @author ramer
 */
@Entity
public class Notifying {

  /** The id. */
  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  /** The content. */
  @Column(nullable = false)
  private String content;

  /** The date. */
  @Temporal(value = TemporalType.TIMESTAMP)
  @Column(nullable = false)
  private Date date;

  /** The user. */
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user", nullable = false)
  private User user;

  /** The notified user. */
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "notified_user", nullable = false)
  private User notifiedUser;

  /** The has check. */
  @Column(name = "has_check", nullable = false)
  private String hasCheck;

  /**
   * Gets the checks for check.
   *
   * @return the checks for check
   */
  public String getHasCheck() {
    return hasCheck;
  }

  /**
   * Sets the checks for check.
   *
   * @param hasCheck the new checks for check
   */
  public void setHasCheck(String hasCheck) {
    this.hasCheck = hasCheck;
  }

  /**
   * Sets the notified user.
   *
   * @param notifiedUser the new notified user
   */
  public void setNotifiedUser(User notifiedUser) {
    this.notifiedUser = notifiedUser;
  }

  /**
   * Gets the notified user.
   *
   * @return the notified user
   */
  public User getNotifiedUser() {
    return notifiedUser;
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
   * Instantiates a new notifying.
   */
  public Notifying() {
  }

  /**
   * Instantiates a new notifying.
   *
   * @param id the id
   * @param content the content
   * @param date the date
   * @param user the user
   * @param notifiedUser the notified user
   * @param hasCheck the has check
   */
  public Notifying(Integer id, String content, Date date, User user, User notifiedUser,
      String hasCheck) {
    this.id = id;
    this.content = content;
    this.date = date;
    this.user = user;
    this.notifiedUser = notifiedUser;
    this.hasCheck = hasCheck;
  }

}
