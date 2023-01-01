package com.se.fawry.controller;
import com.se.fawry.model.entity.*;
import com.se.fawry.enums.PaymentMethod;
import com.se.fawry.enums.TransactionType;
import com.se.fawry.service.Service;
import com.se.fawry.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    private UserService userService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public UserService getUserService() {
        return userService;
    }


    public UserController(UserService userService) {
        this.userService = userService;
    }

    record Card(Long userId, String cardNumber, String cvv, String holderName, String expirationDate){}
    @PostMapping("/user/add-card")
    public ResponseEntity<CreditCard> addCard(@RequestBody Card card) {
        CreditCard card1 = userService.addCreditCard(card.userId, card.cardNumber, card.cvv, card.holderName, card.expirationDate);
        return ResponseEntity.ok(card1);
    }

    record AddFundsRequest(long userId, double amount, String cardNumber){}
    @PostMapping("/user/add-funds")
    public ResponseEntity<Transaction> addFunds(@RequestBody AddFundsRequest addFundsRequest) {
        Transaction transaction = userService.addToWallet(addFundsRequest.userId, addFundsRequest.amount, addFundsRequest.cardNumber);
        if (transaction == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/user/{id}/cards")
    public List<CreditCard> showCards(@PathVariable Long id){
        return userService.getAllCards(id);
    }


    @GetMapping("/user/services")
    public List<Service> getServices( ) {
        return userService.getServices();
    }

    @GetMapping("/user/search")
    public List<Service> search(@RequestParam String query) {
        return userService.search(query);
    }


    record PayRequest(Long serviceId, Long user_id, boolean cashOnDelivery, boolean payWithCreditCard, String cardNumber, double amount){}
    @PostMapping("/user/pay")
    public ResponseEntity<Transaction> pay(@RequestBody PayRequest payRequest) {
        Transaction transaction;
        User user = userService.getById(payRequest.user_id);
        if (payRequest.payWithCreditCard && !payRequest.cashOnDelivery){
            transaction = userService.pay(payRequest.serviceId, user, PaymentMethod.CREDIT_CARD, payRequest.amount ,false, payRequest.cardNumber);
        } else if (!payRequest.payWithCreditCard && !payRequest.cashOnDelivery) {
            transaction = userService.pay(payRequest.serviceId, user, PaymentMethod.WALLET, payRequest.amount ,false, payRequest.cardNumber);
        }
        else {
            transaction = userService.pay(payRequest.serviceId, user, PaymentMethod.CASH_ON_DELIVERY, payRequest.amount , true, payRequest.cardNumber);
        }

        if (transaction == null) {
            return ResponseEntity.badRequest().build();
        }
        transaction.setType(TransactionType.PAYMENT);
        return ResponseEntity.ok(transaction);
    }

    record RequestRefund(long userId, long transactionId){}
    @PostMapping("/user/request-refund")
    public ResponseEntity<RefundRequest> refund(@RequestBody RequestRefund refundRequest) {
        RefundRequest createdRefundRequest = userService.requestRefund(refundRequest.transactionId, refundRequest.userId);
        if (createdRefundRequest == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(createdRefundRequest);
    }


    @GetMapping("/user/discounts")
    public List<Discount> getDiscounts() {
        return userService.getOverAllDiscounts();
    }
    @GetMapping("/user/{userId}/{serviceId}/discounts")
    public List<Discount> getDiscounts(@PathVariable long serviceId, @PathVariable long userId) {
        return userService.getDiscounts(serviceId, userId);
    }

}
