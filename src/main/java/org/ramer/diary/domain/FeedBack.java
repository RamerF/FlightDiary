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

import com.alibaba.fastjson.JSON;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 反馈
 * @author ramer
 *
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedBack{
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

    public String toString() {
        return JSON.toJSONString(this);
    }
}
