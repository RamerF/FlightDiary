package org.ramer.diary.domain;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by RAMER on 5/22/2017.
 */
@Entity
@Data
public class Roles{
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
}
