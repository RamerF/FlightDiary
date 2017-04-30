package org.ramer.diary.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.alibaba.fastjson.JSON;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户通知.
 *
 * @author ramer
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notify{

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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user", nullable = false)
    private User user;

    /** The notified user. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notified_user", nullable = false)
    private User notifiedUser;

    /** The has check. */
    @Column(name = "has_check", nullable = false)
    private String hasCheck;

    public String toString() {
        return JSON.toJSONString(this);
    }
}
