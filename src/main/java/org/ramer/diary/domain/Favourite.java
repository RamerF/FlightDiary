package org.ramer.diary.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

/**
 * 收藏.
 *
 * @author ramer
 */
@Cacheable
@Entity
@Data
public class Favourite{

    /** UID. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    /** 用户. */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user")
    private User user;

    /** 分享. */
    @ManyToOne(fetch = FetchType.LAZY)
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

    @Override
    public String toString() {
        return "Favourite{" + "id=" + id + ", topic=" + topic + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;

        Favourite favourite = (Favourite) o;

        if (id != null ? !id.equals(favourite.id) : favourite.id != null)
            return false;
        return topic != null ? topic.equals(favourite.topic) : favourite.topic == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (topic != null ? topic.hashCode() : 0);
        return result;
    }
}
