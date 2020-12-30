package com.app.bimarunajaya.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "tracking")
public class TrackingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "create_date")
    private Date createDate;

    // for send
    @ManyToOne
    @JoinColumn(name = "send_by")
    private UserEntity sendBy;

    // for transit
    @ManyToOne
    @JoinColumn(name = "from_by")
    private UserEntity fromBy;

    // for transit
    @ManyToOne
    @JoinColumn(name = "transit_id")
    private UserEntity transit;

    // for transit
    @Deprecated
    @ManyToOne
    @JoinColumn(name = "confirm_by")
    private UserEntity confirmBy;

    // for received
    @Column(name = "receive_by")
    private String receiveBy;

    @ManyToOne
    @JoinColumn(name = "delivery_status")
    private TypeEntity deliveryStatus;

    @ManyToOne
    @JoinColumn(name = "package_id")
    private PackageEntity packageData;

    @ManyToOne
    @JoinColumn(name = "progress_status")
    private TypeEntity progressStatus;

}
