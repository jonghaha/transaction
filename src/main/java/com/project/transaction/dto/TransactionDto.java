package com.project.transaction.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionDto {
	@ApiModelProperty(example = "2018", notes = "연도")
	private Integer year;
	@ApiModelProperty(example = "제이", notes = "계좌명")
	private String name;
	@ApiModelProperty(example = "11111111", notes = "계좌번호")
	private Integer acctNo;
	@ApiModelProperty(example = "00000", notes = "합계")
	private Integer sumAmt;
	@ApiModelProperty(example = "판교점", notes = "관리점명")
	private String brName;
	@ApiModelProperty(example = "A", notes = "관리점코드")
	private String brCode;
}
