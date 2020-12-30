package com.app.bimarunajaya.service;

import com.app.bimarunajaya.entity.PackageEntity;
import com.app.bimarunajaya.entity.TrackingEntity;
import com.app.bimarunajaya.entity.TypeEntity;
import com.app.bimarunajaya.entity.UserEntity;
import com.app.bimarunajaya.repo.PackageRepo;
import com.app.bimarunajaya.repo.TrackingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PackageService extends BaseService {

    @Autowired
    private PackageRepo packageRepo;

    public PackageEntity getById(Integer id) {
        return packageRepo.findById(id).orElse(null);
    }

    public List<PackageEntity> getForCashier(UserEntity station, String q) {
        return packageRepo.getForCashier(q, station.getId());
    }

    public List<PackageEntity> getForAdmin(String q) {
        return packageRepo.getForAdmin(q);
    }

    @Autowired
    private TypeService typeService;

    @Autowired
    private TrackingService trackingService;

    public PackageEntity save(PackageEntity packageEntity) {
        Date now = new Date();
        TypeEntity transit = typeService.statusTransit();
        TypeEntity done = typeService.done();

        packageEntity.setCreateDate(now);
        packageEntity.setLastProgressStatus(done);
        packageEntity.setLastDeliveryStatus(transit);
//        packageEntity.setLastStation(); // front
//        packageEntity.setLastPosition(); // front

        String resi = randomString(5);
        packageEntity.setResiNumber(resi);
        packageEntity = packageRepo.save(packageEntity);

        TrackingEntity t = new TrackingEntity();
        t.setCreateDate(now);
        t.setTransit(packageEntity.getLastStation());
        t.setConfirmBy(packageEntity.getLastPosition());
        t.setDeliveryStatus(transit);
        t.setPackageData(packageEntity);
        t.setProgressStatus(typeService.done());
        trackingService.save(t);

        return packageEntity;
    }

    public PackageEntity saveEdit(PackageEntity packageEntity) {
        Date now = new Date();
        TypeEntity transit = typeService.statusTransit();
        TypeEntity done = typeService.done();

        packageEntity.setCreateDate(now);
        packageEntity.setLastProgressStatus(done);
        packageEntity.setLastDeliveryStatus(transit);
//        packageEntity.setLastStation(); // front
//        packageEntity.setLastPosition(); // front

        PackageEntity temp = getById(packageEntity.getId());
        packageEntity.setResiNumber(temp.getResiNumber());
        packageEntity = packageRepo.save(packageEntity);

        return packageEntity;
    }

    @Transactional
    public Integer delete(Integer id) {
        packageRepo.deleteById(id);
        return trackingService.deleteByPackageId(id);
    }

    public List<PackageEntity> getForDeliveryTransit(UserEntity userEntity, String q) {
        List<PackageEntity> t = packageRepo
                .getForDeliveryTransit(q, userEntity.getStation().getId(), TypeService.statusTransit, TypeService.done);
        return t;
    }

    public List<PackageEntity> getForDeliveryProcess(UserEntity userEntity, String q) {
        List<PackageEntity> t = packageRepo
                .getForDeliveryProcess(q, userEntity.getId(), TypeService.statusProcess, TypeService.progress);
        return t;
    }

    public void pickPackage(UserEntity userEntity, Integer packageId) {
        PackageEntity p = packageRepo.findById(packageId).get();
        TypeEntity process = typeService.statusProcess();
        TypeEntity progress = typeService.progress();

        p.setLastPosition(userEntity);
        p.setLastProgressStatus(progress);
        p.setLastDeliveryStatus(process);
        p = packageRepo.save(p);

        TrackingEntity t = new TrackingEntity();
        t.setCreateDate(new Date());
        t.setSendBy(userEntity);
        t.setDeliveryStatus(process);
        t.setPackageData(p);
        t.setProgressStatus(progress);
        trackingService.save(t);
    }

    public void transitPackage(UserEntity userEntity, UserEntity transitPlace, Integer packageId) {
        PackageEntity p = packageRepo.findById(packageId).get();
        TypeEntity transit = typeService.statusTransit();
        TypeEntity done = typeService.done();

        p.setLastDeliveryStatus(transit);
        p.setLastProgressStatus(done);
        p.setLastStation(transitPlace);
        p.setLastPosition(null);
        p = packageRepo.save(p);

        TrackingEntity t = trackingService.getLastByPackageId(packageId);
        t.setProgressStatus(done);
        trackingService.save(t);

        t = new TrackingEntity();
        t.setCreateDate(new Date());
        t.setFromBy(userEntity);
        t.setTransit(transitPlace);
        t.setDeliveryStatus(transit);
        t.setPackageData(p);
        t.setProgressStatus(done);
        trackingService.save(t);
    }

    public void receivedPackage(String receiveBy, Integer packageId) {
        PackageEntity p = packageRepo.findById(packageId).get();
        TypeEntity received = typeService.statusReceived();
        TypeEntity done = typeService.done();

        p.setLastDeliveryStatus(received);
        p.setLastProgressStatus(done);
        p.setLastStation(null);
        p.setLastPosition(null);
        p = packageRepo.save(p);

        TrackingEntity t = trackingService.getLastByPackageId(packageId);
        t.setProgressStatus(done);
        trackingService.save(t);

        t = new TrackingEntity();
        t.setCreateDate(new Date());
        t.setReceiveBy(receiveBy);
        t.setDeliveryStatus(received);
        t.setPackageData(p);
        t.setProgressStatus(done);
        trackingService.save(t);
    }

    public List<PackageEntity> getForAccountingOrHead(String q, Integer approveStatusId, Integer month, Integer year) {
        List<PackageEntity> t;
        if (approveStatusId == null) {
            if (month != null && year != null)
                t = packageRepo.getForAccountingNotApproveYetMY(q, TypeService.statusReceived, month, year);
            else t = packageRepo.getForAccountingNotApproveYet(q, TypeService.statusReceived);
        } else {
            if (month != null && year != null)
                t = packageRepo.getForAccountingMY(q, TypeService.statusReceived, approveStatusId, month, year);
            else t = packageRepo.getForAccounting(q, TypeService.statusReceived, approveStatusId);
        }
        return t;
    }

    public void approveAccounting(Integer packageId) {
        PackageEntity p = packageRepo.findById(packageId).get();
        TypeEntity done = typeService.done();
        TypeEntity appAcc = typeService.approveAccounting();

        p.setApproveStatus(appAcc);
        p = packageRepo.save(p);

        TrackingEntity t = new TrackingEntity();
        t.setCreateDate(new Date());
        t.setPackageData(p);
        t.setProgressStatus(appAcc);
        trackingService.save(t);
    }

    public void approveHead(Integer packageId) {
        PackageEntity p = packageRepo.findById(packageId).get();
        TypeEntity done = typeService.done();
        TypeEntity appHead = typeService.approveHead();

        p.setApproveStatus(appHead);
        p = packageRepo.save(p);

        TrackingEntity t = new TrackingEntity();
        t.setCreateDate(new Date());
        t.setPackageData(p);
        t.setProgressStatus(appHead);
        trackingService.save(t);
    }

    public Integer getCountCashier(Integer deliveryStatusId) {
        return packageRepo.getCountCashier(deliveryStatusId);
    }

}
