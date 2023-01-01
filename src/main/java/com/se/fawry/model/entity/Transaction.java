package com.se.fawry.model.entity;


import com.se.fawry.enums.TransactionType;
import com.se.fawry.service.Service;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    protected User user;
    @ManyToOne
    private Service service;
    protected double amount;
    private LocalDateTime timestamp;

    private boolean complete;
    @Enumerated(EnumType.STRING)
    protected TransactionType type;

    public abstract void execute();

    // Constructors
    public Transaction() {
    }

    public Transaction(Long id, User user, Service service, double amount, LocalDateTime timestamp, boolean complete) {
        this.id = id;
        this.user = user;
        this.service = service;
        this.amount = amount;
        this.timestamp = timestamp;
        this.complete = complete;
    }

    public Transaction(User user, Service service, double amount, LocalDateTime timestamp, boolean complete) {
        this.user = user;
        this.service = service;
        this.amount = amount;
        this.timestamp = timestamp;
        this.complete = complete;
    }

    // getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Transaction: {" +
                "id=" + id +
                ", user=" + user +
                ", service=" + service +
                ", amount=" + amount +
                ", timestamp=" + timestamp +
                '}';
    }

}