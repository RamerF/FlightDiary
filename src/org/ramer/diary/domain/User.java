package org.ramer.diary.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Transient;

/**
 * 用户.
 *
 * @author ramer
 */
@Entity
public class User {

  /** UID. */
  @Id
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  /** 用户头像. */
  @Column
  private String head;

  /** 用户签名. */
  @Column
  private String says;

  /** 用户名. */
  @Column(unique = true, nullable = false)
  private String name;

  /** 用户别名. */
  @Column(unique = true)
  private String alias;

  /** 密码. */
  @Column(nullable = false)
  private String password;

  /** qq号. */
  @Column(name = "qq_num")
  private String qqNum;

  /** 微博号. */
  @Column(name = "weibo_num")
  private String weiboNum;

  /** 邮箱. */
  @Column(unique = true, nullable = false)
  private String email;

  /** 过期时间. */
  private String expireTime;

  /** 性别. */
  @Column
  private String sex;

  /** 年龄. */
  @Column
  private Integer age;

  /** 住址. */
  @Column
  private String address;

  /** 手机号码. */
  @Column(length = 11)
  private String telephone;

  // 一对多策略
  /** 分享. */
  // 按时间降序排列
  @OrderBy(value = "date desc")
  @OneToMany(cascade = { CascadeType.REMOVE }, mappedBy = "user", fetch = FetchType.LAZY)
  private Set<Topic> topics = new HashSet<>();

  /** 关注. */
  @OneToMany(cascade = { CascadeType.REMOVE }, mappedBy = "user", fetch = FetchType.LAZY)
  private Set<Follow> follows;

  /** 收藏. */
  @OneToMany(cascade = { CascadeType.REMOVE }, mappedBy = "user", fetch = FetchType.LAZY)
  private Set<Favourite> favourites;

  /** 通知. */
  @OrderBy(value = "date desc")
  @OneToMany(cascade = { CascadeType.REMOVE }, mappedBy = "notifiedUser", fetch = FetchType.LAZY)
  private Set<Notify> notifies;
  @Transient
  private Set<Notify> readedNotifies = new HashSet<>();

  /**
   * 空构造器
   */
  public User() {
  }

  public Set<Notify> getReadedNotifies() {
    return readedNotifies;
  }

  public void setReadedNotifies(Set<Notify> readedNotifies) {
    this.readedNotifies = readedNotifies;
  }

  /**
   * 设置过期时间
   *
   * @param expireTime the new expire time
   */
  public void setExpireTime(String expireTime) {
    this.expireTime = expireTime;
  }

  /**
   * Gets the expire time.
   *
   * @return the expire time
   */
  public String getExpireTime() {
    return expireTime;
  }

  /**
   * Sets the email.
   *
   * @param email the new email
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * Gets the email.
   *
   * @return the email
   */
  public String getEmail() {
    return email;
  }

  /**
   * Sets the notifies.
   *
   * @param notifies the new notifies
   */
  public void setNotifies(Set<Notify> notifies) {
    this.notifies = notifies;
  }

  /**
   * Gets the notifies.
   *
   * @return the notifies
   */
  public Set<Notify> getNotifies() {
    return notifies;
  }

  /**
   * Sets the favourites.
   *
   * @param favourites the new favourites
   */
  public void setFavourites(Set<Favourite> favourites) {
    this.favourites = favourites;
  }

  /**
   * Gets the favourites.
   *
   * @return the favourites
   */
  public Set<Favourite> getFavourites() {
    return favourites;
  }

  /**
   * Sets the alias.
   *
   * @param alias the new alias
   */
  public void setAlias(String alias) {
    this.alias = alias;
  }

  /**
   * Gets the alias.
   *
   * @return the alias
   */
  public String getAlias() {
    return alias;
  }

  /**
   * Sets the qq num.
   *
   * @param qqNum the new qq num
   */
  public void setQqNum(String qqNum) {
    this.qqNum = qqNum;
  }

  /**
   * Sets the weibo num.
   *
   * @param weiboNum the new weibo num
   */
  public void setWeiboNum(String weiboNum) {
    this.weiboNum = weiboNum;
  }

  /**
   * Gets the qq num.
   *
   * @return the qq num
   */
  public String getQqNum() {
    return qqNum;
  }

  /**
   * Sets the follows.
   *
   * @param follows the new follows
   */
  public void setFollows(Set<Follow> follows) {
    this.follows = follows;
  }

  /**
   * Gets the follows.
   *
   * @return the follows
   */
  public Set<Follow> getFollows() {
    return follows;
  }

  /**
   * Gets the weibo num.
   *
   * @return the weibo num
   */
  public String getWeiboNum() {
    return weiboNum;
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
   * Gets the head.
   *
   * @return the head
   */
  public String getHead() {
    return head;
  }

  /**
   * Sets the head.
   *
   * @param head the new head
   */
  public void setHead(String head) {
    this.head = head;
  }

  /**
   * Gets the says.
   *
   * @return the says
   */
  public String getSays() {
    return says;
  }

  /**
   * Sets the says.
   *
   * @param says the new says
   */
  public void setSays(String says) {
    this.says = says;
  }

  /**
   * Gets the name.
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name.
   *
   * @param name the new name
   */
  public void setName(String name) {
    this.name = name;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getSex() {
    return sex;
  }

  public void setSex(String sex) {
    this.sex = sex;
  }

  public Integer getAge() {
    return age;
  }

  public void setAge(Integer age) {
    this.age = age;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getTelephone() {
    return telephone;
  }

  public void setTelephone(String telephone) {
    this.telephone = telephone;
  }

  public Set<Topic> getTopics() {
    return topics;
  }

  public void setTopics(Set<Topic> topics) {
    this.topics = topics;
  }

  @Override
  public String toString() {
    return "User [id=" + id + ", name=" + name + ", topics=" + topics + ", follows=" + follows
        + ", favourites=" + favourites + ", notifies=" + notifies + " , readedNotifies="
        + readedNotifies + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((address == null) ? 0 : address.hashCode());
    result = prime * result + ((age == null) ? 0 : age.hashCode());
    result = prime * result + ((alias == null) ? 0 : alias.hashCode());
    result = prime * result + ((favourites == null) ? 0 : favourites.hashCode());
    result = prime * result + ((follows == null) ? 0 : follows.hashCode());
    result = prime * result + ((head == null) ? 0 : head.hashCode());
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + ((notifies == null) ? 0 : notifies.hashCode());
    result = prime * result + ((password == null) ? 0 : password.hashCode());
    result = prime * result + ((qqNum == null) ? 0 : qqNum.hashCode());
    result = prime * result + ((says == null) ? 0 : says.hashCode());
    result = prime * result + ((sex == null) ? 0 : sex.hashCode());
    result = prime * result + ((telephone == null) ? 0 : telephone.hashCode());
    result = prime * result + ((topics == null) ? 0 : topics.hashCode());
    result = prime * result + ((weiboNum == null) ? 0 : weiboNum.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    User other = (User) obj;
    if (address == null) {
      if (other.address != null) {
        return false;
      }
    } else if (!address.equals(other.address)) {
      return false;
    }
    if (age == null) {
      if (other.age != null) {
        return false;
      }
    } else if (!age.equals(other.age)) {
      return false;
    }
    if (alias == null) {
      if (other.alias != null) {
        return false;
      }
    } else if (!alias.equals(other.alias)) {
      return false;
    }
    if (favourites == null) {
      if (other.favourites != null) {
        return false;
      }
    } else if (!favourites.equals(other.favourites)) {
      return false;
    }
    if (follows == null) {
      if (other.follows != null) {
        return false;
      }
    } else if (!follows.equals(other.follows)) {
      return false;
    }
    if (head == null) {
      if (other.head != null) {
        return false;
      }
    } else if (!head.equals(other.head)) {
      return false;
    }
    if (id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!id.equals(other.id)) {
      return false;
    }
    if (name == null) {
      if (other.name != null) {
        return false;
      }
    } else if (!name.equals(other.name)) {
      return false;
    }
    if (notifies == null) {
      if (other.notifies != null) {
        return false;
      }
    } else if (!notifies.equals(other.notifies)) {
      return false;
    }
    if (password == null) {
      if (other.password != null) {
        return false;
      }
    } else if (!password.equals(other.password)) {
      return false;
    }
    if (qqNum == null) {
      if (other.qqNum != null) {
        return false;
      }
    } else if (!qqNum.equals(other.qqNum)) {
      return false;
    }
    if (says == null) {
      if (other.says != null) {
        return false;
      }
    } else if (!says.equals(other.says)) {
      return false;
    }
    if (sex == null) {
      if (other.sex != null) {
        return false;
      }
    } else if (!sex.equals(other.sex)) {
      return false;
    }
    if (telephone == null) {
      if (other.telephone != null) {
        return false;
      }
    } else if (!telephone.equals(other.telephone)) {
      return false;
    }
    if (topics == null) {
      if (other.topics != null) {
        return false;
      }
    } else if (!topics.equals(other.topics)) {
      return false;
    }
    if (weiboNum == null) {
      if (other.weiboNum != null) {
        return false;
      }
    } else if (!weiboNum.equals(other.weiboNum)) {
      return false;
    }
    return true;
  }

}
