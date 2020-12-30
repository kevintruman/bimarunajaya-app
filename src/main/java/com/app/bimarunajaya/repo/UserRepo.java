package com.app.bimarunajaya.repo;

import com.app.bimarunajaya.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepo extends JpaRepository<UserEntity, Integer> {

    UserEntity findFirstByUsername(String username);

    List<UserEntity> findByFullNameContainingAndTypeUserId(String fullname, Integer typeUserId);

    List<UserEntity> findByTypeUserId(Integer typeUserId);

}
