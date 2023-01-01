package com.se.fawry.model.entity;

import jakarta.persistence.*;

@Entity

public class RefundTransaction extends Transaction {
    @Override
    public void execute() {
        user.setWalletBalance(user.getWalletBalance() + amount);
    }
}