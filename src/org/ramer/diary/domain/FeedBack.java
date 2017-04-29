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

/**
 * 反馈
 * @author ramer
 *
 */
@Entity
public class FeedBack {
  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user")
  private User user;
  @Column
  private String content;
  @Column
  private Date date;

  @Column
  private String hasCheck;

  public FeedBack() {
  }

  public void setHasCheck(String hasCheck) {
    this.hasCheck = hasCheck;
  }

  public String getHasCheck() {
    return hasCheck;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public User getUser() {
    return user;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public Date getDate() {
    return date;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

}
