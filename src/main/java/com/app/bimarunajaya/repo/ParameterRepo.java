package com.app.bimarunajaya.repo;

import com.app.bimarunajaya.entity.ParameterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParameterRepo extends JpaRepository<ParameterEntity, Integer> {

    List<ParameterEntity> findByIdIn(List<Integer> id);

    List<ParameterEntity> findByParentId(Integer parentId);

}
