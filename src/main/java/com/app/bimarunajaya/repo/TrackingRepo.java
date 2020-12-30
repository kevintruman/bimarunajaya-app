package com.app.bimarunajaya.repo;

import com.app.bimarunajaya.entity.TrackingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface TrackingRepo extends JpaRepository<TrackingEntity, Integer> {

    List<TrackingEntity> findByPackageDataId(Integer packageId);
    TrackingEntity findFirstByPackageDataIdOrderByCreateDateDesc(Integer packageId);

    @Modifying
    @Transactional
    @Query(value = "delete from TrackingEntity u where u.packageData.id = ?1")
    Integer delByPackageId(Integer packageId);

    List<TrackingEntity> findByPackageDataResiNumber(String resiNumber);

}
