package com.project.transaction.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.project.transaction.dto.TransactionDto;
import com.project.transaction.entity.QAccount;
import com.project.transaction.entity.QTransactions;
import com.project.transaction.entity.Transactions;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class TransactionRepositoryImpl extends QuerydslRepositorySupport implements TransationRepositoryCustom {
	private QTransactions transactions = QTransactions.transactions;
	private QAccount account = QAccount.account;
	private final JPAQueryFactory queryFactory;

	public TransactionRepositoryImpl(JPAQueryFactory queryFactory) {
		super(Transactions.class);
		this.queryFactory = queryFactory;
	}

	@Override
	public List<TransactionDto> groupByTxDateAndAcctNoSumAmt() {
		return queryFactory.select(Projections.bean(TransactionDto.class,
			transactions.txDate.year().as("year"),
			transactions.acctNo,
			account.name,
			transactions.amt.subtract(transactions.fee).sum().as("sumAmt")
			)).from(transactions)
			.where(transactions.cancelFlag.isFalse())
			.innerJoin(account).on(transactions.acctNo.eq(account.acctNo))
			.groupBy(transactions.txDate.year(), transactions.acctNo)
			.fetch();
	}

	@Override
	public List<TransactionDto> getNoTransactionCustomer(Integer year) {
		return queryFactory.select(Projections.bean(TransactionDto.class,
			account.acctNo,
			account.name
			)).from(account)
			.leftJoin(transactions).on(transactions.acctNo.eq(account.acctNo).and(transactions.cancelFlag.isFalse()).and(transactions.txDate.year().eq(year)))
			.where(transactions.isNull())
			.fetch();
	}
}
