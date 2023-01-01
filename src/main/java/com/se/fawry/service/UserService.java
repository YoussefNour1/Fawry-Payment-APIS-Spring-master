package com.se.fawry.service;

import com.se.fawry.enums.PaymentMethod;
import com.se.fawry.enums.Role;
import com.se.fawry.enums.ServiceType;
import com.se.fawry.enums.TransactionType;
import com.se.fawry.model.entity.*;
import com.se.fawry.model.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@org.springframework.stereotype.Service
public class UserService {

    private UserRepository userRepository;

    private ServiceRepository serviceRepository;

    private TransactionRepository transactionRepository;

    private DiscountRepository discountRepository;

    private RefundRequestRepository refundRequestRepository;

    private CreditCardRepository creditCardRepository;

    User globalUser;


    public UserService() {
    }

    @Autowired
    public UserService(UserRepository userRepository, ServiceRepository serviceRepository, TransactionRepository transactionRepository, DiscountRepository discountRepository, RefundRequestRepository refundRequestRepository, CreditCardRepository creditCardRepository) {
        this.userRepository = userRepository;
        this.serviceRepository = serviceRepository;
        this.transactionRepository = transactionRepository;
        this.discountRepository = discountRepository;
        this.refundRequestRepository = refundRequestRepository;
        this.creditCardRepository = creditCardRepository;
    }


    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getById(Long id) {
        return userRepository.findById(id).get();
    }

    public User signIn(String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty() || !optionalUser.get().getPassword().equals(password)) {
            throw new RuntimeException("Invalid email or password");
        }
        return optionalUser.get();
    }


    public User signUp(String username, String email, String password, Role role) {
        // check if the email or username is registered before
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            throw new RuntimeException("Email is already registered");
        }
        optionalUser = Optional.ofNullable(userRepository.findByUsername(username));
        if (optionalUser.isPresent()) {
            throw new RuntimeException("Username is already registered");
        }

        // create the new user
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role == null ? Role.USER : role);
        userRepository.save(user);

        return user;
    }


    // User Add to wallet
    public Transaction addToWallet(Long userId, double amount, String cardNumber) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        List<CreditCard> cards = creditCardRepository.findAllByUser(user);

        for (CreditCard card : cards) {
            if (Objects.equals(card.getCardNumber(), cardNumber)) {
                user.setWalletBalance(user.getWalletBalance() + amount);
                card.setBalance(card.getBalance() - amount);
                userRepository.save(user);
                creditCardRepository.save(card);
                Transaction transaction = new AddToWalletTransaction();
                transaction.setAmount(amount);
                transaction.setUser(user);
                transaction.setType(TransactionType.ADD_TO_WALLET);
                transactionRepository.save(transaction);
                return transaction;
            }
        }
        throw new RuntimeException("No cards found");
    }

    // add credit card
    public CreditCard addCreditCard(Long userId, String cardNumber, String cvv, String holderName, String expirationDate) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        CreditCard creditCard = new CreditCard(cardNumber, holderName, expirationDate, user, cvv);
        creditCard.setCardNumber(cardNumber);
        creditCard.setCardHolderName(holderName);
        creditCard.setExpirationDate(expirationDate);
        creditCard.setUser(user);
        creditCard.setBalance(1000);
        creditCardRepository.save(creditCard);
        return creditCard;
    }

    public List<CreditCard> getAllCards(Long id) {
        return creditCardRepository.findAllByUser(userRepository.findById(id).get());
    }

    public List<Service> search(String query) {
        // normal users can only search for available services
        return serviceRepository.findByNameContainingIgnoreCaseAndIsAvailableIsTrue(query);
    }


    //4. Pay Service
    public Transaction pay(Long serviceId, User user, PaymentMethod paymentMethod, double totalAmount, boolean cashOnDelivery, String cardNumber) {
        Service service = serviceRepository.findById(serviceId).orElse(null);
        if (service == null) {
            throw new RuntimeException("Service not found");
        }

        if (user.getRole() == Role.USER) {
            // normal users can only pay for available services
            if (!service.isAvailable()) {
                throw new RuntimeException("Service is not available");
            }
        }

        // apply discounts
        List<Discount> discounts = getDiscounts(serviceId, (long) user.getId());
        for (Discount discount : discounts) {
            totalAmount *= (1 - discount.getPercentage());
        }

        // check if the service accepts cash on delivery
        if (!cashOnDelivery || !service.isCacheOnDelivery()) {
            // process the payment via credit card or wallet
            if (paymentMethod == PaymentMethod.CREDIT_CARD) {
                // process the payment via credit card
                List<CreditCard> userCards = creditCardRepository.findAllByUser(user);
                for (CreditCard card : userCards) {
                    if (card.getCardNumber().equals(cardNumber)) {
                        if (card.getBalance() - totalAmount >= 0) {
                            card.setBalance(card.getBalance() - totalAmount);
                            creditCardRepository.save(card);
                            System.out.println("inside if");
                        } else throw new RuntimeException("Can't complete this payment");
                    }
                }

            } else if (paymentMethod == PaymentMethod.WALLET) {
                // check if the user has enough balance in the wallet
                if (user.getWalletBalance() < totalAmount) {
                    throw new RuntimeException("Insufficient balance in the wallet");
                }
                // process the payment via wallet
                user.setWalletBalance(user.getWalletBalance() - totalAmount);
                userRepository.save(user);
            } else {
                throw new RuntimeException("Invalid payment method");
            }
        }

        // create the transaction
        Transaction transaction = new PaymentTransaction();
        transaction.setUser(user);
        transaction.setService(service);
        transaction.setAmount(totalAmount);
        transaction.setComplete(true);
        transactionRepository.save(transaction);
        return transaction;
    }


    //5.requestRefund
    public RefundRequest requestRefund(long transactionId, long userId) {
        // check if the transaction exists and belongs to the user
        User user = userRepository.findById(userId).get();
        Transaction transaction = transactionRepository.findById(transactionId).orElse(null);
        if (transaction == null || !(transaction.getUser().getId() == user.getId())) {
            throw new RuntimeException("Transaction not found or not belonging to the user");
        }

        // check if the transaction is complete
        if (!transaction.isComplete()) {
            throw new RuntimeException("Transaction is not complete");
        }

        // create the refund request
        RefundRequest refundRequest = new RefundRequest();
        refundRequest.setTransaction(transaction);
        refundRequest.setAmount(transaction.getAmount());
        refundRequestRepository.save(refundRequest);
        return refundRequest;
    }


    //6.addFunds


    //7.getDiscounts
    public List<Discount> getDiscounts(long serviceId, Long user_id) {

        // get overall discounts
        User user = userRepository.findById(user_id).get();
        List<Discount> discounts = new ArrayList<>(discountRepository.findByType(DiscountType.OVERALL));

        // get specific discounts for the service
        serviceRepository.findById(serviceId).ifPresent(service -> discounts.addAll(discountRepository.findByTypeAndService(DiscountType.SPECIFIC, service)));

        // apply discounts to the user's transactions
        if (user.getRole() == Role.USER) {
            List<Transaction> transactions = transactionRepository.findByUser(user);
            for (Transaction transaction : transactions) {
                for (Discount discount : discounts) {
                    if (Objects.equals(transaction.getService().getId(), discount.getService().getId())) {
                        transaction.setAmount(transaction.getAmount() * (1 - discount.getValue()));
                        transactionRepository.save(transaction);
                    }
                }
            }
        }
        return discounts;
    }

    public List<Discount> getOverAllDiscounts() {
        return discountRepository.findAll();
    }

    public List<Service> getServices() {
        return new ArrayList<>(serviceRepository.findAll());
    }







    // 5. Admin List all refund request
    public List<RefundRequest> listRefundRequests() {
        if (globalUser.getRole() == Role.ADMIN) {
            return refundRequestRepository.findAll();
        }
        throw new RuntimeException("You aren't authorized");
    }

    // 6. Accept refund request
    public void approveRefundRequest(RefundRequest request) {
        if (globalUser.getRole() == Role.ADMIN) {
            request.setStatus("APPROVED");
            RefundTransaction transaction = new RefundTransaction();
            transaction.setUser(request.getUser());
            transaction.setService(request.getService());
            transaction.setAmount(request.getAmount());
            transaction.setTimestamp(LocalDateTime.now());
            transaction.setType(TransactionType.REFUND);
            transaction.execute();
            transactionRepository.save(transaction);
            refundRequestRepository.save(request);
        } else throw new RuntimeException("You aren't authorized");
    }

    public void rejectRefundRequest(RefundRequest request) {
        request.setStatus("REJECTED");
        refundRequestRepository.save(request);
    }

}


