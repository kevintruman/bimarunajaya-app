package com.app.bimarunajaya.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "update_date")
    private Date updateDate;

    private String username;
    private String password;
    private String email;
    private String phone;

    @Column(name = "full_name")
    private String fullName;

    @ManyToOne
    @JoinColumn(name = "type_user_id")
    private TypeEntity typeUser;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private TypeEntity role;

    @ManyToOne
    @JoinColumn(name = "station_id")
    private UserEntity station;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private TypeEntity status;

}
