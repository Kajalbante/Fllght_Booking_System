package com.Pay.Service;


import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Pay.Entity.Transaction;
import com.Pay.Repository.TransactionRepository;

import java.time.LocalDateTime;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository repository;

    public Transaction saveOrderData(JSONObject order) {
        Transaction txn = new Transaction();
        txn.setRazorpayOrderId(order.getString("id"));
        txn.setReceipt(order.getString("receipt"));
        txn.setAmount(order.getDouble("amount") / 100.0);
        txn.setCurrency(order.getString("currency"));
        txn.setStatus(order.getString("status"));
        txn.setAttempts(order.getInt("attempts"));
        txn.setCreatedAt(LocalDateTime.now());

        return repository.save(txn);
    }
}


