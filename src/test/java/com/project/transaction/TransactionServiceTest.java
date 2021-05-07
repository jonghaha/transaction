package com.project.transaction;

import static org.assertj.core.api.Assertions.*;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.project.transaction.dto.TransactionDto;
import com.project.transaction.repository.TransactionRepository;

@SpringBootTest
public class TransactionServiceTest {
	@Autowired
	private TransactionRepository transactionRepository;

	@Test
	void 거래내역_groupBy_연도_계좌번호() {
		/**
		 * -- 연도별 합계 큰 고객 Query
		 *
		 * SELECT t1.year, t1.acctNo, t1.name, t1.sumAmt
		 * FROM (
		 *     SELECT YEAR(tx_date) as year, t.acct_no as acctNo, name, SUM(t.amt - t.fee) as sumAmt
		 *     FROM TRANSACTIONS t
		 *     JOIN ACCOUNT a on a.acct_no = t.acct_no
		 *     WHERE cancel_flag = false
		 *     GROUP BY YEAR(tx_date), t.acct_no
		 * ) t1
		 * INNER JOIN (
		 *     SELECT year, MAX(sumAmt) as maxAmt
		 *     FROM (
		 *         SELECT YEAR(tx_date) as year, t.acct_no as acctNo, name, SUM(t.amt - t.fee) as sumAmt
		 *         FROM TRANSACTIONS t
		 *         JOIN ACCOUNT a on a.acct_no = t.acct_no
		 *         WHERE cancel_flag = false
		 *         GROUP BY YEAR(tx_date), t.acct_no
		 *     )  t
		 *     GROUP BY year
		 * ) t2 ON t2.maxAmt = t1.sumAmt and t2.year = t1.year
		 *
		 * -- result
		 * year acctNo      name    sumAmt
		 * 2018	11111114	테드	28992000
		 * 2019	11111112	에이스	40998400
		 * 2020	11111121	에이스	1000000
		 * */

		//given
		List<TransactionDto> transactionList = transactionRepository.groupByTxDateAndAcctNoSumAmt();

		//when
		 Map<String, Optional<TransactionDto>> maxCustomer = transactionList.stream()
			.collect(Collectors.groupingBy(TransactionDto::getYear, Collectors.maxBy(
				Comparator.comparingInt(TransactionDto::getSumAmt))));

		 //then
		assertThat(maxCustomer.get("2018").orElse(null).getAcctNo()).isEqualTo(11111114);
		assertThat(maxCustomer.get("2018").orElse(null).getSumAmt()).isEqualTo(28992000);
		assertThat(maxCustomer.get("2019").orElse(null).getAcctNo()).isEqualTo(11111112);
		assertThat(maxCustomer.get("2019").orElse(null).getSumAmt()).isEqualTo(40998400);
	}

}
