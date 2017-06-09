package org.ramer.diary.domain;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by RAMER on 5/26/2017.
 */
@Entity
@Data
public class Privilege {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;

}
