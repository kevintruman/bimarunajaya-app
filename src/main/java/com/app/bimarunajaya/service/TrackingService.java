package com.app.bimarunajaya.service;

import com.app.bimarunajaya.entity.TrackingEntity;
import com.app.bimarunajaya.repo.TrackingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
public class TrackingService extends BaseService {

    @Autowired
    private TrackingRepo trackingRepo;

    public List<TrackingEntity> getByPackageId(Integer packageId) {
        return trackingRepo.findByPackageDataId(packageId);
    }

    public List<TrackingEntity> getByCode(String code) {
        return trackingRepo.findByPackageDataResiNumber(code);
    }

    public TrackingEntity getLastByPackageId(Integer packageId) {
        return trackingRepo.findFirstByPackageDataIdOrderByCreateDateDesc(packageId);
    }

    public TrackingEntity save(TrackingEntity trackingEntity) {
        if (trackingEntity.getCreateDate() == null)
            trackingEntity.setCreateDate(new Date());
        return trackingRepo.save(trackingEntity);
    }

    @Transactional
    public Integer deleteByPackageId(Integer packageId) {
        return trackingRepo.delByPackageId(packageId);
    }

}
