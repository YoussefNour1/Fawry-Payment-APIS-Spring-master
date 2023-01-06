package com.se.fawry.model.repository;

import com.se.fawry.enums.TransactionType;
import com.se.fawry.model.entity.PaymentTransaction;
import com.se.fawry.model.entity.Transaction;
import com.se.fawry.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUser(User user);
    List<Transaction> findByType(TransactionType type);
}