package com.project.transaction.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.transaction.dto.TransactionDto;
import com.project.transaction.entity.Transactions;
import com.project.transaction.repository.TransactionRepository;
import com.project.transaction.service.TransactionService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transaction")
public class TransactionController {
	private final TransactionService transactionService;

	/**
	 * 2018, 2019년 각 연도별 합계 금액이 가장 많은 고객을 추출
	 * (단, 취소여부가 'Y' 거래는 취소된 거래, 합계 금액은 거래금액에서 수수료를 차감한 금액)
	 *
	 * @return TransactionDto
	 * */
	@ApiOperation(value = "연도별 합계 금액이 가장 많은 고객 요청", notes = "2018, 2019년 각 연도별 합계 금액이 가장 많은 고객 요청")
	@GetMapping(value = "/maxAmtCustomerByYear")
	public @ResponseBody List<TransactionDto> getMaxAmtCustomerByYear() {
		return transactionService.getMaxAmtCustomerByYear();
	}

	/**
	 * 2018년 또는 2019년에 거래가 없는 고객을 추출
	 * (취소여부가 'Y' 거래는 취소된 거래)
	 *
	 * @return TransactionDto
	 * */
	@ApiOperation(value = "거래가 없는 고객 요청", notes = "2018년 또는 2019년에 거래가 없는 고객을 추출")
	@GetMapping(value = "/noTransactionCustomerByYear")
	public @ResponseBody List<TransactionDto> getNoTransactionCustomerByYear() {
		return transactionService.getNoTransactionCustomerByYear();
	}

	/**
	 * 연도별 관리점별 거래금액 합계를 구하고 합계금액이 큰 순서로 출력
	 * (취소여부가 'Y' 거래는 취소된 거래)
	 *
	 * @return TransactionDto
	 * */
	@ApiOperation(value = "연도별 관리점별 거래금액 합계 리스트 요청", notes = "연도별 관리점별 거래금액 합계를 구하고 합계금액이 큰 순서로 출력")
	@GetMapping(value = "/totalAmountByYearByBranch")
	public @ResponseBody List<Map<String, Object>> getTotalAmountByYearByBranch() {
		return transactionService.getTotalAmountByYearByBranch();
	}

	/**
	 * 분당점과 판교점을 통폐합하여 판교점으로 관리점 이관을 하였습니다.
	 * 지점명을 입력하면 해당지점의 거래금액 합계를 출력
	 * (취소여부가 'Y' 거래는 취소된 거래)
	 *
	 * @param brName 관리점코드
	 * @return TransactionDto
	 * */
	@ApiOperation(value = "지점별 거래 합계 요청", notes = "지점명을 입력하면 해당지점의 거래금액 합계를 출력")
	@ApiImplicitParam(name = "brName", required = true, example = "판교점", value = "관리점코드\n(BRANCH BR_NAME 컬럼)")
	@ApiResponses({
		@ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 404, message = "br code not found error")
	})
	@GetMapping(value = "/sumAmountByBranch")
	public @ResponseBody TransactionDto getSumAmountBranch(String brName) {
		return transactionService.getSumAmountBranch(brName);
	}
}
