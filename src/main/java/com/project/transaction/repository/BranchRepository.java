package com.project.transaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.transaction.entity.Branch;

public interface BranchRepository extends JpaRepository<Branch, String> {
}
