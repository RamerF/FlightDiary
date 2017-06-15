package org.ramer.diary.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Created by caipeijun on 2017/5/29.
 */

@Cacheable
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tags {

    private static final long serialVersionUID = 1L;

    /** UID. */
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 标签名
     */
    private String tagName;

    /**
     * 标签热度
     */
    private Integer hot;
}
