package com.project.transaction.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.project.transaction.dto.TransactionDto;
import com.project.transaction.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TransactionService {
	private final TransactionRepository transactionRepository;

	public List<TransactionDto> getMaxAmtCustomerByYear() {
		List<TransactionDto> respons = new ArrayList<>();
		List<TransactionDto> transactionList = transactionRepository.groupByTxDateAndAcctNoSumAmt();

		Map<String, Optional<TransactionDto>> maxCustomer = transactionList.stream()
			.collect(Collectors.groupingBy(TransactionDto::getYear, Collectors.maxBy(
				Comparator.comparingInt(TransactionDto::getSumAmt))));

		respons.add(maxCustomer.get("2018").orElse(null));
		respons.add(maxCustomer.get("2019").orElse(null));

		return respons;
	}
}
