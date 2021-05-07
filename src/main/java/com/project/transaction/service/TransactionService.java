package com.project.transaction.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
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

		Map<Integer, Optional<TransactionDto>> maxCustomer = transactionList.stream()
			.collect(Collectors.groupingBy(TransactionDto::getYear, Collectors.maxBy(
				Comparator.comparingInt(TransactionDto::getSumAmt))));

		respons.add(maxCustomer.get(2018).orElse(null));
		respons.add(maxCustomer.get(2019).orElse(null));

		return respons;
	}

	public List<TransactionDto> getNoTransactionCustomerByYear() {
		List<TransactionDto> respons = new ArrayList<>();
		List<TransactionDto> noCusromerList2018 = transactionRepository.getNoTransactionCustomer(2018);
		List<TransactionDto> noCusromerList2019 = transactionRepository.getNoTransactionCustomer(2019);

		respons.addAll(setYear(noCusromerList2018, 2018));
		respons.addAll(setYear(noCusromerList2019, 2019));

		return respons;
	}

	private List<TransactionDto> setYear(List<TransactionDto> transactionDtoList, Integer year) {
		for (TransactionDto t : transactionDtoList) {
			t.setYear(year);
		}

		return transactionDtoList;
	}

	public List<Map<String, Object>> getTotalAmountByYearByBranch() {
		List<Map<String, Object>> response = new ArrayList<>();
		List<TransactionDto> branchList = transactionRepository.getBranchSumByYear();
		Map<String, Object> branchList2018 = new HashMap<>();
		Map<String, Object> branchList2019 = new HashMap<>();

		branchList2018.put("year", 2018);
		branchList2018.put("dataList",
			branchList.stream().filter(b -> b.getYear() == 2018).collect(Collectors.toList()));

		branchList2019.put("year", 2019);
		branchList2019.put("dataList",
			branchList.stream().filter(b -> b.getYear() == 2019).collect(Collectors.toList()));

		response.add(branchList2018);
		response.add(branchList2019);

		return response;
	}
}
