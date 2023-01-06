package com.se.fawry.service;

import com.se.fawry.enums.ServiceType;
import com.se.fawry.enums.TransactionType;
import com.se.fawry.model.entity.*;
import com.se.fawry.model.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;


import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Service
public class AdminServices {
    User globalUser;
    private UserRepository userRepository;

    private ServiceRepository serviceRepository;

    private TransactionRepository transactionRepository;

    private DiscountRepository discountRepository;

    private RefundRequestRepository refundRequestRepository;

    private CreditCardRepository creditCardRepository;

    public AdminServices() {
    }

    @Autowired
    public AdminServices(UserRepository userRepository, ServiceRepository serviceRepository, TransactionRepository transactionRepository, DiscountRepository discountRepository, RefundRequestRepository refundRequestRepository, CreditCardRepository creditCardRepository) {
        this.userRepository = userRepository;
        this.serviceRepository = serviceRepository;
        this.transactionRepository = transactionRepository;
        this.discountRepository = discountRepository;
        this.refundRequestRepository = refundRequestRepository;
        this.creditCardRepository = creditCardRepository;
    }

    // 1. Admin add overall Discount
    public Discount addOverallDiscount(String name, double percentage) {
            Discount discount = new Discount();
            discount.setName(name);
            discount.setPercentage(percentage);
            discountRepository.save(discount);
            return discount;
    }

    // 2 . Add Specific Discount

    public Discount addSpecificDiscount(String name, double percentage, long serviceId) {
            Service service = serviceRepository.findById(serviceId).orElseThrow(() ->
                    new ResourceNotFoundException("Service not found"));
            Discount discount = new Discount();
            discount.setName(name);
            discount.setPercentage(percentage);
            discount.setService(service);
            discountRepository.save(discount);
            return discount;
        }

    // 3. Admin add service
    public Service addService(String name, boolean cacheOnDelivery, boolean creditCardPayment, String provider, ServiceType type) {
        Service service = new Service();
        service.setName(name);
        service.setCacheOnDelivery(cacheOnDelivery);
        service.setCreditCardPayment(creditCardPayment);
        service.setAvailable(true);
        service.setProvider(provider);
        service.setType(type);
        return serviceRepository.save(service);
    }


    // 4 . List Transactions
    public List<Transaction> listTransactions() {
        List<Transaction> paymentTransactions = transactionRepository.findByType(TransactionType.PAYMENT);
        List<Transaction> RefundTransactions = transactionRepository.findByType(TransactionType.REFUND);
        List<Transaction> AddToWalletTransactions = transactionRepository.findByType(TransactionType.ADD_TO_WALLET);
        List<Transaction> transactions = new ArrayList<Transaction>();
        transactions.add((Transaction) paymentTransactions);
        transactions.add((Transaction) RefundTransactions);
        transactions.add((Transaction) AddToWalletTransactions);

        return transactions;

    }

    public  List<RefundRequest> refundRequestList(){
        List<RefundRequest> refundRequestList = new ArrayList<>(refundRequestRepository.findAll());
        return refundRequestList;
    }


}



