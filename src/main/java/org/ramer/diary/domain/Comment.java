package org.ramer.diary.domain;

import java.util.Date;
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
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.alibaba.fastjson.JSON;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户评论.
 *
 * @author ramer
 */
@Cacheable
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment{

    /** UID. */
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** 评论内容. */
    @Column
    private String content;

    /** 时间. */
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date date;

    /**分享. */
    //评论的分享
    @ManyToOne
    @JoinColumn(name = "topic")
    private Topic topic;

    /** 用户. */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user")
    private User user;

    /** 评论回复. */
    @OneToMany(mappedBy = "comment", cascade = { CascadeType.REMOVE })
    @OrderBy("date asc")
    private Set<Reply> replies;

    public String toString() {
        return JSON.toJSONString(this);
    }
}
