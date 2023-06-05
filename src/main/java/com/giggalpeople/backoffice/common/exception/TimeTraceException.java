package com.giggalpeople.backoffice.common.exception;

import com.giggalpeople.backoffice.common.enumtype.ErrorCode;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class TimeTraceException extends RuntimeException {

	private ErrorCode errorCode;

	public TimeTraceException(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	public TimeTraceException(String message) {
		super(message);
	}

	public TimeTraceException(ErrorCode errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	public TimeTraceException(String message, Throwable cause) {
		super(message, cause);
	}

	public TimeTraceException(Throwable cause) {
		super(cause);
	}

	public TimeTraceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
