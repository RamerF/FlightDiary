package org.ramer.diary.domain;

import java.io.Serializable;
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

import com.alibaba.fastjson.JSON;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分享类.
 *
 * @author ramer
 */
@Cacheable
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Topic implements Serializable{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

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

    public String toString() {
        return JSON.toJSONString(this);
    }
}
