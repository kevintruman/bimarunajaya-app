package com.app.bimarunajaya.service;

import com.app.bimarunajaya.entity.ParameterEntity;
import com.app.bimarunajaya.repo.ParameterRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ParameterService {

    @Autowired
    private ParameterRepo parameterRepo;

    public List<ParameterEntity> getDimension() {
        return parameterRepo.findByIdIn(Arrays.asList(1, 5 ,6));
    }

    public List<ParameterEntity> getPulau() {
        return parameterRepo.findByParentId(pulau);
    }

    public List<ParameterEntity> getKota(Integer pulauId) {
        return parameterRepo.findByParentId(pulauId);
    }

    public ParameterEntity getWeight() {
        return parameterRepo.findById(2).get();
    }
    public ParameterEntity getTax() {
        return parameterRepo.findById(3).get();
    }
    public ParameterEntity getMinWeight() {
        return parameterRepo.findById(86).get();
    }

    public static Integer pulau = 85;

}
