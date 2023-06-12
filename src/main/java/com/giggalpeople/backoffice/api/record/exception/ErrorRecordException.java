package com.giggalpeople.backoffice.api.record.exception;

import com.giggalpeople.backoffice.common.enumtype.ErrorCode;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class ErrorRecordException extends RuntimeException {

	private ErrorCode errorCode;

	public ErrorRecordException(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	public ErrorRecordException(String message) {
		super(message);
	}

	public ErrorRecordException(ErrorCode errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	public ErrorRecordException(String message, Throwable cause) {
		super(message, cause);
	}

	public ErrorRecordException(Throwable cause) {
		super(cause);
	}

	public ErrorRecordException(String message, Throwable cause, boolean enableSuppression,
		boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
