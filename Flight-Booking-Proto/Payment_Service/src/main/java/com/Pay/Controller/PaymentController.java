package com.Pay.Controller;

import com.Pay.Service.TransactionService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "*")
public class PaymentController {

    @Value("${razorpay.key}")
    private String razorpayKey;

    @Value("${razorpay.secret}")
    private String razorpaySecret;

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/create-order")
    public ResponseEntity<String> createOrder(@RequestParam double amount) throws Exception {
        RazorpayClient razorpay = new RazorpayClient(razorpayKey, razorpaySecret);

        JSONObject options = new JSONObject();
        options.put("amount", (int) (amount * 100));
        options.put("currency", "INR");
        options.put("receipt", "txn_" + System.currentTimeMillis());
        options.put("payment_capture", 1);

        Order order = razorpay.orders.create(options);

        transactionService.saveOrderData(order.toJson()); 

        return ResponseEntity.ok(order.toString());
    }
}
