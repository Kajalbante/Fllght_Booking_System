package com.Pay.Repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.Pay.Entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
