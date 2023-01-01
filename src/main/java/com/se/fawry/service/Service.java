package com.se.fawry.service;

import com.se.fawry.enums.ServiceType;
import jakarta.persistence.*;

@Entity
@Table(name = "services")
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private boolean cacheOnDelivery;
    private boolean creditCardPayment;
    private String provider;

    private double price;

    @Enumerated(EnumType.STRING)
    private ServiceType type;

    private boolean isAvailable;

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }

    public Service() {
    }

    public Service(Long id, String name, String provider, boolean cacheOnDelivery, boolean creditCardPayment, double price, ServiceType type, boolean isAvailable) {
        this.id = id;
        this.name = name;
        this.provider = provider;
        this.cacheOnDelivery = cacheOnDelivery;
        this.creditCardPayment = creditCardPayment;
        this.price = price;
        this.type = type;
        this.isAvailable = isAvailable;
    }

    public Service(String name, boolean cacheOnDelivery, String provider, boolean creditCardPayment, double price, ServiceType type, boolean isAvailable) {
        this.name = name;
        this.provider = provider;
        this.cacheOnDelivery = cacheOnDelivery;
        this.creditCardPayment = creditCardPayment;
        this.price = price;
        this.type = type;
        this.isAvailable = isAvailable;

    }

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

    public boolean isCacheOnDelivery() {
        return cacheOnDelivery;
    }

    public void setCacheOnDelivery(boolean cacheOnDelivery) {
        this.cacheOnDelivery = cacheOnDelivery;
    }

    public boolean isCreditCardPayment() {
        return creditCardPayment;
    }

    public void setCreditCardPayment(boolean creditCardPayment) {
        this.creditCardPayment = creditCardPayment;
    }

    public ServiceType getType() {
        return type;
    }

    public void setType(ServiceType type) {
        this.type = type;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    @Override
    public String toString() {
        return "Service{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cacheOnDelivery=" + cacheOnDelivery +
                ", creditCardPayment=" + creditCardPayment +
                ", provider='" + provider + '\'' +
                ", price=" + price +
                ", type=" + type +
                ", isAvailable=" + isAvailable +
                '}';
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}


