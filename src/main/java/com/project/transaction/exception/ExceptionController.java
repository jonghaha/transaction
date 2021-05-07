package com.project.transaction.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.project.transaction.dto.ErrorCode;

@ControllerAdvice
public class ExceptionController {
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(value = NotFoundException.class)
	public ResponseEntity<?> NotFoundException(NotFoundException e) {
		ErrorCode errorCode = ErrorCode.builder()
			.code("404")
			.message("br code not found error")
			.build();

		return new ResponseEntity<>(errorCode, HttpStatus.NOT_FOUND);
	}
}
