package com.app.bimarunajaya.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "package")
public class PackageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "create_date")
    private Date createDate;



    // last position user
    @ManyToOne
    @JoinColumn(name = "last_position_id")
    private UserEntity lastPosition;

    // last position station
    @ManyToOne
    @JoinColumn(name = "last_station_id")
    private UserEntity lastStation;



    @Column(name = "address_from")
    private String addressFrom;

    @Column(name = "send_name")
    private String sendName;

    @Column(name = "send_phone")
    private String sendPhone;

    @Column(name = "address_to")
    private String addressTo;

    @Column(name = "receive_name")
    private String receiveName;

    @Column(name = "receive_phone")
    private String receivePhone;

    @Column(name = "package_description")
    private String packageDescription;

    private Double weight;

    @ManyToOne
    @JoinColumn(name = "dimension")
    private ParameterEntity dimension;

    private Double price;

    @Column(name = "price_description")
    private String priceDescription;



    @ManyToOne
    @JoinColumn(name = "last_delivery_status")
    private TypeEntity lastDeliveryStatus;

    @ManyToOne
    @JoinColumn(name = "approve_status")
    private TypeEntity approveStatus;

    @Column(name = "resi_number")
    private String resiNumber;

    @ManyToOne
    @JoinColumn(name = "last_progress_status")
    private TypeEntity lastProgressStatus;

    @ManyToOne
    @JoinColumn(name = "create_by")
    private UserEntity createBy;

    @ManyToOne
    @JoinColumn(name = "station_by")
    private UserEntity stationBy;

    @ManyToOne
    @JoinColumn(name = "kota_id")
    private ParameterEntity kota;

}
