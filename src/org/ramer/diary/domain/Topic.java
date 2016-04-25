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
  //  标签
  @Column
  private String tags;

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
  /** 收藏. */
  //对应多个用户收藏
  @Column
  @OneToMany(cascade = { CascadeType.REMOVE }, mappedBy = "topic")
  private Set<Favourite> favourites = new HashSet<>();
  /** 点赞. */
  //对应多个用户点赞
  @Column
  @OneToMany(cascade = { CascadeType.REMOVE }, mappedBy = "topic")
  private Set<Praise> praises = new HashSet<>();

  public Set<Favourite> getFavourites() {
    return favourites;
  }

  public void setFavourites(Set<Favourite> favourites) {
    this.favourites = favourites;
  }

  public Set<Praise> getPraises() {
    return praises;
  }

  public void setPraises(Set<Praise> praises) {
    this.praises = praises;
  }

  public Integer getUpCounts() {
    return upCounts;
  }

  public void setUpCounts(Integer upCounts) {
    this.upCounts = upCounts;
  }

  public Set<Comment> getComments() {
    return comments;
  }

  public void setComments(Set<Comment> comments) {
    this.comments = comments;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public String getPicture() {
    return picture;
  }

  public void setPicture(String picture) {
    this.picture = picture;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public String getTags() {
    return tags;
  }

  public void setTags(String tags) {
    this.tags = tags;
  }

  /**
   * Instantiates a new topic.
   *
   * @param id the id
   * @param content the content
   * @param date the date
   * @param picture the picture
   * @param tags the tags
   * @param upCounts the up counts
   * @param user the user
   * @param comments the comments
   * @param favourites the favourites
   * @param praises the praises
   */

  public Topic(Integer id, String content, Date date, String picture, String tags, Integer upCounts,
      User user, Set<Comment> comments, Set<Favourite> favourites, Set<Praise> praises) {
    super();
    this.id = id;
    this.content = content;
    this.date = date;
    this.picture = picture;
    this.tags = tags;
    this.upCounts = upCounts;
    this.user = user;
    this.comments = comments;
    this.favourites = favourites;
    this.praises = praises;
  }

  public Topic() {
  }
}
