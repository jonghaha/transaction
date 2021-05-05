package com.project.transaction.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.transaction.dto.TransactionDto;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

	/**
	 * 2018, 2019년 각 연도별 합계 금액이 가장 많은 고객을 추출
	 * (단, 취소여부가 'Y' 거래는 취소된 거래, 합계 금액은 거래금액에서 수수료를 차감한 금액)
	 * */
	@ApiOperation(value = "연도별 합계 금액이 가장 많은 고객 요청", notes = "2018, 2019년 각 연도별 합계 금액이 가장 많은 고객 요청")
	@GetMapping(value = "/getTotalAmountList")
	public @ResponseBody List<TransactionDto> getLargeTotalAmountCustomerYearly() {
		return null;
	}

	/**
	 * 2018년 또는 2019년에 거래가 없는 고객을 추출
	 * (취소여부가 'Y' 거래는 취소된 거래)
	 * */
	@ApiOperation(value = "거래가 없는 고객 요청", notes = "2018년 또는 2019년에 거래가 없는 고객을 추출")
	@GetMapping(value = "/getNoTransaction")
	public @ResponseBody List<TransactionDto> getNoTransactionCustomer() {
		return null;
	}

	/**
	 * 연도별 관리점별 거래금액 합계를 구하고 합계금액이 큰 순서로 출력
	 * (취소여부가 'Y' 거래는 취소된 거래)
	 * */
	@ApiOperation(value = "연도별 합계 금액이 가장 많은 고객 요청", notes = "연도별 관리점별 거래금액 합계를 구하고 합계금액이 큰 순서로 출력")
	@GetMapping(value = "/getTotalAmount")
	public @ResponseBody List<TransactionDto> getTotalAmountByYearByBranch() {
		return null;
	}

	/**
	 * 분당점과 판교점을 통폐합하여 판교점으로 관리점 이관을 하였습니다.
	 * 지점명을 입력하면 해당지점의 거래금액 합계를 출력
	 * (취소여부가 'Y' 거래는 취소된 거래)
	 * */
	@ApiOperation(value = "지점명을 입력하면 해당지점의 거래금액 합계 요청", notes = "지점명을 입력하면 해당지점의 거래금액 합계를 출력")
	@PostMapping(value = "getSumAmount")
	public @ResponseBody List<TransactionDto> getSumAmountBranch() {
		return null;
	}
}
