package com.project.transaction.repository;

import java.util.List;

import com.project.transaction.dto.TransactionDto;

public interface TransationRepositoryCustom {
	List<TransactionDto> groupByTxDateAndAcctNoSumAmt();
	List<TransactionDto> getNoTransactionCustomer(Integer year);
}
