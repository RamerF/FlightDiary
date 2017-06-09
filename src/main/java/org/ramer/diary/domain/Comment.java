package org.ramer.diary.domain;

import java.util.Date;
import java.util.Set;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户评论.
 *
 * @author ramer
 */
@Cacheable
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
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
