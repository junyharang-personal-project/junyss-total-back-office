package com.giggalpeople.backoffice.api.record.exception;

import com.giggalpeople.backoffice.common.enumtype.ErrorCode;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class ErrorLogException extends RuntimeException {

	private ErrorCode errorCode;

	public ErrorLogException(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	public ErrorLogException(String message) {
		super(message);
	}

	public ErrorLogException(ErrorCode errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	public ErrorLogException(String message, Throwable cause) {
		super(message, cause);
	}

	public ErrorLogException(Throwable cause) {
		super(cause);
	}

	public ErrorLogException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
