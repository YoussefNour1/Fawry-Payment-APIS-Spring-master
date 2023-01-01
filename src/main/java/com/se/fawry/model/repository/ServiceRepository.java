package com.se.fawry.model.repository;

import com.se.fawry.service.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    List<Service> findByNameContainingIgnoreCase(String name);
    List<Service> findByNameContainingIgnoreCaseAndIsAvailableIsTrue(String name);



}