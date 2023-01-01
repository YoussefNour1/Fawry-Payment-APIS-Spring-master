package com.se.fawry.model.repository;

import com.se.fawry.model.entity.CreditCard;
import com.se.fawry.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {
    List<CreditCard> findAllByUser(User user);
}