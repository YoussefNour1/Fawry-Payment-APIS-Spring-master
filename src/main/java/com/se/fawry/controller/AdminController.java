package com.se.fawry.controller;
import com.se.fawry.enums.ServiceType;
import com.se.fawry.enums.TransactionType;
import com.se.fawry.model.entity.Discount;
import com.se.fawry.model.entity.RefundRequest;
import com.se.fawry.model.entity.Transaction;
import com.se.fawry.service.AdminServices;
import com.se.fawry.service.Service;
import com.se.fawry.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private AdminServices adminServices;

    public AdminController(AdminServices adminServices) {
        this.adminServices = adminServices;
    }

    public AdminServices getAdminServices() {
        return adminServices;
    }

    public void setAdminServices(AdminServices adminServices) {
        this.adminServices = adminServices;
    }

    record AddOverDis(String name, double percentage){}
    @PostMapping("/addOverallDiscount")
    public ResponseEntity<Discount> addOverallDiscount(@RequestBody AddOverDis addOverDis){
        Discount discount = adminServices.addOverallDiscount(addOverDis.name , addOverDis.percentage);
        if (discount == null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(discount);
    }

    record AddSpeDis(String name, double percentage ,long serviceId){}
    @PostMapping("/addSpesificDiscount")
    public ResponseEntity<Discount> addSpecificDiscount(@RequestBody AddSpeDis addSpeDis){
        Discount discount = adminServices.addSpecificDiscount(addSpeDis.name , addSpeDis.percentage , addSpeDis.serviceId);
        if (discount == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(discount);
    }


    record AddService(String name, boolean cashOnDelivery, boolean creditCardPayment, String provider, ServiceType serviceType){}
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/addService")
    public ResponseEntity<Service> addService(@RequestBody AddService service){
        Service service1 = adminServices.addService(service.name, service.cashOnDelivery, service.creditCardPayment, service.provider, service.serviceType);
        return ResponseEntity.ok(service1);
    }

    @GetMapping("/getTransactions")
    public List<Transaction> listTransactions(){
        return adminServices.listTransactions();
    }

    @GetMapping("/refundRequestList")
    public List<RefundRequest> refundRequestList(){
        return adminServices.refundRequestList();
    }
}
