package com.project.transaction.dto;

import java.time.LocalDate;

public class TransactionDto {
	private LocalDate transactionDate;
	private Integer accountNumber;
	private Integer transactionNumber;
	private Integer amount;
	private Integer fee;
	private boolean cancelFlag;
}
