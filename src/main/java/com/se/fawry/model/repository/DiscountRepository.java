package com.se.fawry.model.repository;

import com.se.fawry.model.entity.Discount;
import com.se.fawry.model.entity.DiscountType;
import com.se.fawry.service.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {
    List<Discount> findByService(Service service);
    List<Discount> findByType( DiscountType type );
    List<Discount> findByTypeAndService(DiscountType type, Service service);
}


