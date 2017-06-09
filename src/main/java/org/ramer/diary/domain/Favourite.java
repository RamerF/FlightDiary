package org.ramer.diary.domain;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 收藏.
 *
 * @author ramer
 */
@Cacheable
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
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
