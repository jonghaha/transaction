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

import io.swagger.models.auth.In;

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
		 Map<Integer, Optional<TransactionDto>> maxCustomer = transactionList.stream()
			.collect(Collectors.groupingBy(TransactionDto::getYear, Collectors.maxBy(
				Comparator.comparingInt(TransactionDto::getSumAmt))));

		 //then
		assertThat(maxCustomer.get(2018).orElse(null).getAcctNo()).isEqualTo(11111114);
		assertThat(maxCustomer.get(2018).orElse(null).getSumAmt()).isEqualTo(28992000);
		assertThat(maxCustomer.get(2019).orElse(null).getAcctNo()).isEqualTo(11111112);
		assertThat(maxCustomer.get(2019).orElse(null).getSumAmt()).isEqualTo(40998400);
	}

	@Test
	void 거래내역_없는_고객() {
		/**
		 * -- 2018년도 거래가 없는 고객
		 * SELECT a.acct_no, a.name
		 * FROM ACCOUNT a
		 * LEFT JOIN (SELECT * FROM TRANSACTIONS WHERE cancel_flag = false AND YEAR(tx_date) = '2018') t ON t.acct_no = a.acct_no
		 * WHERE t.tx_seq IS NULL
		 *
		 * -- result
		 * acctNo   name
		 * 11111115	사라
		 * 11111118	제임스
		 * 11111121	에이스
		 *
		 * -- 2019년도 거래가 없는 고객
		 * SELECT a.acct_no, a.name
		 * FROM ACCOUNT a
		 * LEFT JOIN (SELECT * FROM TRANSACTIONS WHERE cancel_flag = false AND YEAR(tx_date) = '2019') t ON t.acct_no = a.acct_no
		 * WHERE t.tx_seq IS NULL
		 *
		 * -- result
		 * acctNo   name
		 * 11111114	테드
		 * 11111118	제임스
		 * 11111121	에이스
		 * */
		//given
		List<TransactionDto> noCusromerList2018 = transactionRepository.getNoTransactionCustomer(2018);
		//then
		assertThat(noCusromerList2018.stream().map(TransactionDto::getAcctNo).collect(Collectors.toList())).contains(11111115, 11111118, 11111121);

		//given
		List<TransactionDto> noCusromerList2019 = transactionRepository.getNoTransactionCustomer(2019);
		//then
		assertThat(noCusromerList2019.stream().map(TransactionDto::getAcctNo).collect(Collectors.toList())).contains(11111114, 11111118, 11111121);
	}
}
