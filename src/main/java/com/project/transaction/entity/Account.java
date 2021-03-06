package com.project.transaction.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long acctSeq;
	private Integer acctNo;
	private String name;
	private String brCode;
}
