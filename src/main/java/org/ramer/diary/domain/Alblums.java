package org.ramer.diary.domain;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

/**
 *  TOPIC 套图
 * Created by RAMER on 6/19/2017.
 */
@Entity
public class Alblums{
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String url;
    @CreationTimestamp
    private Date createTime;
}
