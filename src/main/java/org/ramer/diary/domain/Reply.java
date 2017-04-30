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

import com.alibaba.fastjson.JSON;

import lombok.Data;

/**
 * 评论回复.
 *
 * @author ramer
 */
@Cacheable
@Entity
@Data
public class Reply{

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

    public String toString() {
        return JSON.toJSONString(this);
    }
}
