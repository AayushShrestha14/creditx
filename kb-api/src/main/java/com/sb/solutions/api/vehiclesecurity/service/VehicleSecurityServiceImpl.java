package com.sb.solutions.api.vehiclesecurity.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sb.solutions.api.vehiclesecurity.entity.VehicleSecurity;
import com.sb.solutions.api.vehiclesecurity.repository.VehicleSecurityRepository;

/**
 * @author Elvin Shrestha on 1/12/20
 */
@Service
public class VehicleSecurityServiceImpl implements VehicleSecurityService {

    private final VehicleSecurityRepository repository;

    public VehicleSecurityServiceImpl(
        VehicleSecurityRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<VehicleSecurity> findAll() {
        return repository.findAll();
    }

    @Override
    public VehicleSecurity findOne(Long id) {
        return repository.getOne(id);
    }

    @Override
    public VehicleSecurity save(VehicleSecurity vehicleSecurity) {
        return repository.save(vehicleSecurity);
    }

    @Override
    public Page<VehicleSecurity> findAllPageable(Object t, Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public List<VehicleSecurity> saveAll(List<VehicleSecurity> list) {
        return repository.saveAll(list);
    }
}
