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

import com.alibaba.fastjson.JSON;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 关注.
 *
 * @author ramer
 */
@Cacheable
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Follow{

    /** UID. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    /** 用户. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user")
    private User user;

    /** 被关注用户. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "followed_user")
    private User followedUser;

    public String toString() {
        return JSON.toJSONString(this);
    }
}
