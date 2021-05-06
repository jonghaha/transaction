package com.project.transaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.transaction.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
