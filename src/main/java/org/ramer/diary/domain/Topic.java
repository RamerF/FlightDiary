package org.ramer.diary.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

/**
 * 分享类.
 *
 * @author ramer
 */
@Cacheable
@Entity
@Data
public class Topic implements Serializable{

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /** UID. */
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /** 分享内容. */
    @Column(name = "content")
    private String content;
    /** 时间. */
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date date;
    /** 图片. */
    @Column
    @OneToMany(cascade = { CascadeType.REMOVE }, mappedBy = "topic")
    private List<Albums> albums;
    //  标签
    @OrderBy("hot desc")
    @OneToMany(cascade = { CascadeType.REMOVE }, mappedBy = "topic")
    private Set<Tags> tagses;
    /** 点赞次数. */
    @Column
    private Integer upCounts;
    /** 用户. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user")
    private User user;
    /** 评论. */
    //对应多个用户评论
    @OrderBy("date")
    @Column
    @OneToMany(cascade = { CascadeType.REMOVE }, mappedBy = "topic")
    private Set<Comment> comments = new HashSet<>();
    /** 收藏. */
    //对应多个用户收藏
    @Column
    @OneToMany(cascade = { CascadeType.REMOVE }, mappedBy = "topic")
    private Set<Favourite> favourites = new HashSet<>();
    /** 点赞. */
    //对应多个用户点赞
    @Column
    @OneToMany(cascade = { CascadeType.REMOVE }, mappedBy = "topic")
    private Set<Praise> praises = new HashSet<>();
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
        return "Topic{" + "id=" + id + ", content='" + content + '\'' + ", date=" + date + '\'' + ", upCounts="
                + upCounts + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;

        Topic topic = (Topic) o;

        if (id != null ? !id.equals(topic.id) : topic.id != null)
            return false;
        if (content != null ? !content.equals(topic.content) : topic.content != null)
            return false;
        if (date != null ? !date.equals(topic.date) : topic.date != null)
            return false;
        return upCounts != null ? upCounts.equals(topic.upCounts) : topic.upCounts == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (upCounts != null ? upCounts.hashCode() : 0);
        return result;
    }
}
