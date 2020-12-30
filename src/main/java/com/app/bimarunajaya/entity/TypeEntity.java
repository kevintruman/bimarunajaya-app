package com.app.bimarunajaya.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "type")
public class TypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private TypeEntity group;

    private String name;
    private String description;

}
