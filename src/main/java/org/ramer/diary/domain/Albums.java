package org.ramer.diary.domain;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

/**
 *  TOPIC 套图
 * Created by RAMER on 6/19/2017.
 */
@Entity
@Data
public class Albums{
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String url;
    @ManyToOne
    @JoinColumn(name = "topic")
    private Topic topic;
    @CreationTimestamp
    private Date createTime;
    @UpdateTimestamp
    private Date updateTime;

    public Date getCreateTime() {
        return (Date) createTime.clone();
    }

    public Date getUpdateTime() {
        return (Date) updateTime.clone();
    }

    public void setCreateTime(Date createTime) {
        this.createTime = new Date(createTime.getTime());
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = new Date(updateTime.getTime());
    }
}
