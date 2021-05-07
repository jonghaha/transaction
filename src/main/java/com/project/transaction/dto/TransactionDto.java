package com.project.transaction.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionDto {
	@ApiModelProperty(example = "2018", notes = "연도")
	private String year;
	@ApiModelProperty(example = "제이", notes = "계좌명")
	private String name;
	@ApiModelProperty(example = "11111111", notes = "계좌번호")
	private Integer acctNo;
	@ApiModelProperty(example = "00000", notes = "합계")
	private Integer sumAmt;
}
