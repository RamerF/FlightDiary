package org.ramer.diary.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

/**
 * 反馈
 * @author ramer
 *
 */
@Entity
@Data
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
        return "FeedBack{" + "id=" + id + ", content='" + content + '\'' + ", date=" + date + ", hasCheck='" + hasCheck
                + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;

        FeedBack feedBack = (FeedBack) o;

        if (id != null ? !id.equals(feedBack.id) : feedBack.id != null)
            return false;
        if (content != null ? !content.equals(feedBack.content) : feedBack.content != null)
            return false;
        if (date != null ? !date.equals(feedBack.date) : feedBack.date != null)
            return false;
        return hasCheck != null ? hasCheck.equals(feedBack.hasCheck) : feedBack.hasCheck == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (hasCheck != null ? hasCheck.hashCode() : 0);
        return result;
    }
}
