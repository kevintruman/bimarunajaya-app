package com.app.bimarunajaya.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "parameter")
public class ParameterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String value;
    private String description;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private ParameterEntity parent;

}
