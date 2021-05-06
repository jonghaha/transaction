package com.project.transaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.transaction.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
