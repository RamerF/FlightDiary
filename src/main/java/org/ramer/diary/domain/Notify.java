package org.ramer.diary.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

/**
 * 用户通知.
 *
 * @author ramer
 */
@Entity
@Data
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
        return "Notify{" + "id=" + id + ", content='" + content + '\'' + ", date=" + date + ", hasCheck='" + hasCheck
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

        Notify notify = (Notify) o;

        if (id != null ? !id.equals(notify.id) : notify.id != null)
            return false;
        if (content != null ? !content.equals(notify.content) : notify.content != null)
            return false;
        if (date != null ? !date.equals(notify.date) : notify.date != null)
            return false;
        return hasCheck != null ? hasCheck.equals(notify.hasCheck) : notify.hasCheck == null;
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
