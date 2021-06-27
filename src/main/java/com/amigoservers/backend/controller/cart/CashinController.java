package com.amigoservers.backend.controller.cart;

import com.amigoservers.backend.payment.PayPal;
import com.amigoservers.backend.payment.Transaction;
import com.amigoservers.backend.user.Session;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
public class CashinController {
    @RequestMapping(path = "/api/cashin/pay", method = RequestMethod.GET, produces = "application/json")
    public String pay(@RequestHeader(name = "Session-Token") String sessionId,
                      @RequestParam String paymentMethod, @RequestParam BigDecimal amount) {
        if (amount.doubleValue() <= 0) {
            return "{\"success\": false, \"error\": \"amount_too_low\"}";
        }
        Session session = new Session(sessionId);
        int userId = session.getUserId();
        if (userId == 0) {
            // No User found
            return "{\"success\": false, \"error\": \"user_not_found\"}";
        }
        Transaction transaction = new Transaction();
        transaction.setUserId(userId);
        transaction.setTax(new BigDecimal(0));
        transaction.setSubTotal(new BigDecimal(0));
        transaction.setTotal(amount);
        transaction.setPaymentMethod(paymentMethod);
        transaction.setTransactionType("cashin");

        switch (paymentMethod) {
            case "paypal": {
                PayPal paypal = new PayPal(transaction);
                return "{\"success\": true, \"link\": \"" + paypal.getPaymentUrl() + "\"}";
            }
            case "paysafecard": {

            }
            case "bitcoin": {

            }
            default: {
                return "{\"success\": false, \"error\": \"payment_method_doesnt_exist\"}";
            }
        }
    }
}
