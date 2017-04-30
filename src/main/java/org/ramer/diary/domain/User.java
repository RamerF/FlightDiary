package org.ramer.diary.domain;

import java.io.Serializable;
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

import com.alibaba.fastjson.JSON;

import lombok.Data;

/**
 * 用户.
 *
 * @author ramer
 */
@Entity
@Data
public class User implements Serializable{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

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
    @Column
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

    /** The sessionid. */
    @Column
    private String sessionid;

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

    public String toString() {
        return JSON.toJSONString(this);
    }
}
