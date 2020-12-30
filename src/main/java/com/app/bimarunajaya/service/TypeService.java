package com.app.bimarunajaya.service;

import com.app.bimarunajaya.entity.TypeEntity;
import com.app.bimarunajaya.repo.TypeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeService {

    @Autowired
    private TypeRepo typeRepo;

    public TypeEntity getById(Integer id) {
        return typeRepo.findById(id).orElse(null);
    }

    public List<TypeEntity> getByGroupId(Integer groupId) {
        return typeRepo.findByGroupId(groupId);
    }


    public List<TypeEntity> getByStatus() {
        return getByGroupId(11);
    }
    public List<TypeEntity> getByTypeUser() {
        return getByGroupId(12);
    }
    public List<TypeEntity> getByRole() {
        return getByGroupId(13);
    }
    public List<TypeEntity> getByProgressStatus() {
        return getByGroupId(14);
    }


    public static Integer statusTransit = 1;
    public static Integer statusProcess = 2;
    public static Integer statusReceived = 3;
    public static Integer typeUser = 4;
    public static Integer typeStation = 5;
    public static Integer roleCashier = 6;
    public static Integer roleAdmin = 7;
    public static Integer roleDelivery = 8;
    public static Integer roleAccounting = 9;
    public static Integer roleHead = 10;
    public static Integer progress = 14;
    public static Integer done = 15;
    public static Integer approveAccounting = 17;
    public static Integer approveHead = 18;
    public static Integer approveAccount = 20;
    public static Integer notActiveAccount = 21;

    public TypeEntity statusTransit() {
        return getById(1);
    }
    public TypeEntity statusProcess() {
        return getById(2);
    }
    public TypeEntity statusReceived() {
        return getById(3);
    }

    public TypeEntity typeUser() {
        return getById(4);
    }
    public TypeEntity typeStation() {
        return getById(5);
    }

    public TypeEntity roleCashier() {
        return getById(6);
    }
    public TypeEntity roleAdmin() {
        return getById(7);
    }
    public TypeEntity roleDelivery() {
        return getById(8);
    }
    public TypeEntity roleAccounting() {
        return getById(9);
    }
    public TypeEntity roleHead() {
        return getById(10);
    }

    public TypeEntity progress() {
        return getById(14);
    }
    public TypeEntity done() {
        return getById(15);
    }

    public TypeEntity approveAccounting() {
        return getById(17);
    }
    public TypeEntity approveHead() {
        return getById(18);
    }

    public TypeEntity approveAccount() {
        return getById(20);
    }
    public TypeEntity notActiveAccount() {
        return getById(21);
    }

}
