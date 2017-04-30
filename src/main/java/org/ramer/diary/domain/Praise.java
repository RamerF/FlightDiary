package org.ramer.diary.domain;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.alibaba.fastjson.JSON;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 点赞类.
 *
 * @author ramer
 */
@Cacheable
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Praise{

    /** UID. */
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** 点赞用户. */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user")
    private User user;

    /** 分享. */
    //被点赞的分享
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic")
    private Topic topic;

    public String toString() {
        return JSON.toJSONString(this);
    }
}
