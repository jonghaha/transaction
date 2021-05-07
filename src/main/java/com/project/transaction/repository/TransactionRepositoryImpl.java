package com.project.transaction.repository;

import java.util.List;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.project.transaction.dto.TransactionDto;
import com.project.transaction.entity.QAccount;
import com.project.transaction.entity.QBranch;
import com.project.transaction.entity.QTransactions;
import com.project.transaction.entity.Transactions;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class TransactionRepositoryImpl extends QuerydslRepositorySupport implements TransationRepositoryCustom {
	private QTransactions transactions = QTransactions.transactions;
	private QAccount account = QAccount.account;
	private QBranch branch = QBranch.branch;
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

	@Override
	public List<TransactionDto> getBranchSumByYear() {
		return queryFactory.select(Projections.bean(TransactionDto.class,
			transactions.txDate.year().as("year"),
			branch.brCode,
			branch.brName,
			transactions.amt.subtract(transactions.fee).sum().as("sumAmt")
			)).from(transactions)
			.join(account).on(transactions.acctNo.eq(account.acctNo))
			.join(branch).on(account.brCode.eq(branch.brCode))
			.where(transactions.cancelFlag.isFalse())
			.groupBy(transactions.txDate.year(), branch.brCode, branch.brName)
			.orderBy(transactions.txDate.year().asc(), transactions.amt.subtract(transactions.fee).sum().desc())
			.fetch();
	}
}
