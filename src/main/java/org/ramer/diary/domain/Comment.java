package org.ramer.diary.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * 用户评论.
 *
 * @author ramer
 */
@Cacheable
@Entity
@Data
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
    @CreationTimestamp
    private Date createTime;
    @UpdateTimestamp
    private Date updateTime;

    public Date getDate() {
        return (Date) date.clone();
    }

    public void setDate(Date date) {
        this.date = new Date(date.getTime());
    }

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
        return "Comment{" + "id=" + id + ", content='" + content + '\'' + ", date=" + date + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;

        Comment comment = (Comment) o;

        if (id != null ? !id.equals(comment.id) : comment.id != null)
            return false;
        if (content != null ? !content.equals(comment.content) : comment.content != null)
            return false;
        return date != null ? date.equals(comment.date) : comment.date == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }
}
