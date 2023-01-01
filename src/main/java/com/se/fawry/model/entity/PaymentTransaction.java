package com.se.fawry.model.entity;

import jakarta.persistence.*;

@Entity
public class PaymentTransaction extends Transaction {
    @Override
    public void execute() {
        // handle payment transaction
    }
}

