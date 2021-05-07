package com.project.transaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.transaction.entity.Transactions;

public interface TransactionRepository extends JpaRepository<Transactions, Long>, TransationRepositoryCustom {
}
