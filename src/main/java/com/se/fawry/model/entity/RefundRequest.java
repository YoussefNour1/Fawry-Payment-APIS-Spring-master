package com.se.fawry.model.entity;

import com.se.fawry.service.Service;
import jakarta.persistence.*;

@Entity
@Table(name = "refund_requests")
public class RefundRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User user;
    @ManyToOne
    private Service service;
    private double amount;
    private String status;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;

    // Constructors

    public RefundRequest(Long id, User user, Service service, double amount, String status, RefundTransaction transaction) {
        this.id = id;
        this.user = user;
        this.service = service;
        this.amount = amount;
        this.status = status;
        this.transaction = transaction;
    }

    public RefundRequest(User user, Service service, double amount, String status, RefundTransaction transaction) {
        this.user = user;
        this.service = service;
        this.amount = amount;
        this.status = status;
        this.transaction = transaction;
    }

    public RefundRequest() {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
    public Transaction getTransaction() {
        return this.transaction;
    }
}
