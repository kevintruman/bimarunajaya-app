package com.app.bimarunajaya.repo;

import com.app.bimarunajaya.entity.TypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TypeRepo extends JpaRepository<TypeEntity, Integer> {

    List<TypeEntity> findByGroupId(Integer id);

}
