package com.app.bimarunajaya.service;

import com.app.bimarunajaya.entity.UserEntity;
import com.app.bimarunajaya.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    public UserEntity getById(Integer id) {
        return userRepo.findById(id).get();
    }

    public UserEntity getByUsername(String username) {
        return userRepo.findFirstByUsername(username);
    }

    public List<UserEntity> getUsers(String fullname) {
        return userRepo.findByFullNameContainingAndTypeUserId(fullname, TypeService.typeUser);
    }

    public UserEntity login(String username, String password) {
        UserEntity s = getByUsername(username);
        if (s != null && s.getPassword().equals(password)) return s;
        return null;
    }

    @Autowired
    private TypeService typeService;

    public UserEntity register(UserEntity userEntity) {
        userEntity.setCreateDate(new Date());
        userEntity.setTypeUser(typeService.typeUser());
        return userRepo.save(userEntity);
    }

    public List<UserEntity> getStations(String fullname) {
        return userRepo.findByFullNameContainingAndTypeUserId(fullname, TypeService.typeStation);
    }

    public UserEntity saveUser(UserEntity s) {
        Optional<UserEntity> t = userRepo.findById(s.getId());
        s.setCreateDate(t.get().getCreateDate());
        s.setUpdateDate(new Date());
        s.setTypeUser(typeService.typeUser());
        s.setStatus(t.get().getStatus());
        return userRepo.save(s);
    }

    public UserEntity saveStation(UserEntity s) {
        if (s.getId() != null) {
            Optional<UserEntity> t = userRepo.findById(s.getId());
            s.setCreateDate(t.get().getCreateDate());
            s.setStatus(t.get().getStatus());
        } else {
            s.setCreateDate(new Date());
            s.setStatus(typeService.approveAccount());
        }
        s.setUpdateDate(new Date());
        s.setTypeUser(typeService.typeStation());
        return userRepo.save(s);
    }

    public UserEntity approveUser(Integer id) {
        UserEntity t = userRepo.findById(id).get();
        t.setUpdateDate(new Date());
        t.setStatus(typeService.approveAccount());
        return userRepo.save(t);
    }

    public UserEntity notActiveUser(Integer id) {
        UserEntity t = userRepo.findById(id).get();
        t.setUpdateDate(new Date());
        t.setStatus(typeService.notActiveAccount());
        return userRepo.save(t);
    }
}
