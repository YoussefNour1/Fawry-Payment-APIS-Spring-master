package com.se.fawry.model.entity;

import com.se.fawry.service.Service;
import jakarta.persistence.*;

@Entity
@Table(name = "discounts")
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double percentage;

    @ManyToOne
    private Service service;
    private double value;

    @Enumerated(EnumType.STRING)
    private DiscountType type;

    public Discount() {}

    public Discount(Long id, String name, double percentage, Service service, double value, DiscountType type) {
        this.id = id;
        this.name = name;
        this.percentage = percentage;
        this.service = service;
        this.value = value;
        this.type = type;
    }

    public Discount(String name, double percentage, Service service, double value, DiscountType type) {
        this.name = name;
        this.percentage = percentage;
        this.service = service;
        this.value = value;
        this.type = type;
    }

    // getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    @Override
    public String toString() {
        return "Discount: {" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", percentage=" + percentage +
                ", service=" + service +
                '}';
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public DiscountType getType() {
        return type;
    }

    public void setType(DiscountType type) {
        this.type = type;
    }
}