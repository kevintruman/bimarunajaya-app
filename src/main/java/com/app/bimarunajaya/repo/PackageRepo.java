package com.app.bimarunajaya.repo;

import com.app.bimarunajaya.entity.PackageEntity;
import com.app.bimarunajaya.entity.UserEntity;
import com.app.bimarunajaya.service.TypeService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PackageRepo extends JpaRepository<PackageEntity, Integer> {

    @Query("select p from PackageEntity p where (p.resiNumber like %?1% or p.receiveName like %?1% or p.sendName like %?1%) and p.stationBy.id = ?2 " +
            "order by p.createDate desc")
    List<PackageEntity> getForCashier(String q, Integer stationById);

    @Query("select p from PackageEntity p where (p.resiNumber like %?1% or p.receiveName like %?1% or p.sendName like %?1%) " +
            "order by p.createDate desc")
    List<PackageEntity> getForAdmin(String q);

    @Query("select p from PackageEntity p where (p.resiNumber like %?1% or p.receiveName like %?1% or p.sendName like %?1%) and p.lastStation.id = ?2 and " +
            "p.lastDeliveryStatus.id = ?3 and p.lastProgressStatus.id = ?4 order by p.createDate desc")
    List<PackageEntity> getForDeliveryTransit(String q, Integer stationId, Integer deliveryStatusId,
                                              Integer progressStatusId);

    @Query("select p from PackageEntity p where (p.resiNumber like %?1% or p.receiveName like %?1% or p.sendName like %?1%) and p.lastPosition.id = ?2 and " +
            "p.lastDeliveryStatus.id = ?3 and p.lastProgressStatus.id = ?4 order by p.createDate desc")
    List<PackageEntity> getForDeliveryProcess(String q, Integer positionId, Integer deliveryStatusId,
                                              Integer progressStatusId);

    @Query("select p from PackageEntity p where p.resiNumber like %?1% and " +
            "p.lastDeliveryStatus.id = ?2 and p.approveStatus.id is null order by p.createDate desc")
    List<PackageEntity> getForAccountingNotApproveYet(String q, Integer deliveryStatusId);

    @Query(value = "select * from package where resi_number like %?1% and " +
            "last_delivery_status = ?2 and approve_status is null and month(create_date) = ?3 and " +
            "year(create_date) = ?4 order by create_date desc", nativeQuery = true)
    List<PackageEntity> getForAccountingNotApproveYetMY(String q, Integer deliveryStatusId, Integer month, Integer year);

    @Query("select p from PackageEntity p where p.resiNumber like %?1% and " +
            "p.lastDeliveryStatus.id = ?2 and p.approveStatus.id = ?3 order by p.createDate desc")
    List<PackageEntity> getForAccounting(String q, Integer deliveryStatusId, Integer approveStatusId);

    @Query(value = "select * from package where resi_number like %?1% and " +
            "last_delivery_status = ?2 and approve_status = ?3 and month(create_date) = ?4 and " +
            "year(create_date) = ?5 order by create_date desc", nativeQuery = true)
    List<PackageEntity> getForAccountingMY(String q, Integer deliveryStatusId, Integer approveStatusId, Integer month, Integer year);


    @Query("select count(p) from PackageEntity p where p.lastDeliveryStatus.id = ?1")
    Integer getCountCashier(Integer deliveryStatusId);

}
