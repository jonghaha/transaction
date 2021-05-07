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
import com.project.transaction.exception.NotFoundException;
import com.project.transaction.repository.TransactionRepository;
import com.project.transaction.service.TransactionService;

@SpringBootTest
public class TransactionServiceTest {
	@Autowired
	private TransactionRepository transactionRepository;
	@Autowired
	private TransactionService transactionService;

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

	@Test
	void 연도별_관리점별_거래금액_합계() {
		/**
		 * -- 연도별 관리점별 거래금액 합계
		 * SELECT YEAR(t.tx_date), b.br_code, b.br_name, SUM(amt - fee) as sumAmt
		 * FROM TRANSACTIONS t
		 * JOIN ACCOUNT a ON a.acct_no = t.acct_no
		 * JOIN BRANCH b ON a.br_code = b.br_code
		 * WHERE t.cancel_flag = false
		 * GROUP BY YEAR(t.tx_date), b.br_code, b.br_name
		 * ORDER BY YEAR(t.tx_date), sumAmt DESC
		 *
		 * -- result
		 * year  brCode  brName     sumAmt
		 * 2018	  B	     분당점	    38484000
		 * 2018	  A	     판교점	    20505700
		 * 2018	  C	     강남점	    20232867
		 * 2018	  D	     잠실점	    14000000
		 * 2019	  A	     판교점	    66795100
		 * 2019	  B	     분당점	    45396700
		 * 2019	  C	     강남점	    19500000
		 * 2019	  D	     잠실점	    6000000
		 * 2020	  E	     을지로점  	1000000
		 * */
		//given
		List<TransactionDto> transactionList = transactionRepository.getBranchSumByYear();
		//when
		String brName2018Index3 = transactionList.stream().filter(t -> t.getYear() == 2018).collect(Collectors.toList()).get(3).getBrName();
		String brName2019Index2 = transactionList.stream().filter(t -> t.getYear() == 2019).collect(Collectors.toList()).get(2).getBrName();
		//then
		assertThat(brName2018Index3).isEqualTo("잠실점");
		assertThat(brName2019Index2).isEqualTo("강남점");
	}

	@Test
	void 해당지점_거래금액_합계_exception_테스트() {
		//given
		String brName = "분당점";
		//then
		assertThatThrownBy(() -> {
			transactionService.getSumAmountBranch(brName);
		}).isInstanceOf(NotFoundException.class).hasMessage("br code not found error");
	}

	@Test
	void 해당지점_거래금액_합계() {
		/**
		 * -- 판교점
		 * SELECT b.br_code, b.br_name, SUM(amt - fee) as sumAmt
		 * FROM TRANSACTIONS t
		 * JOIN ACCOUNT a ON a.acct_no = t.acct_no
		 * JOIN BRANCH b ON a.br_code = b.br_code
		 * WHERE t.cancel_flag = false AND b.br_name = '판교점'
		 * GROUP BY b.br_code, b.br_name
		 *
		 * -- result
		 * brCode brName  sumAmt
		 * A	  판교점	  87300800
		 * */
		//given
		String brName = "판교점";

		TransactionDto branchSum = transactionService.getSumAmountBranch(brName);

		assertThat(branchSum.getSumAmt()).isEqualTo(87300800);
	}
}
