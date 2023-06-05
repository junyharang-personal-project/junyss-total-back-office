package com.giggalpeople.backoffice.chatops.logback.appender.exception;

import com.giggalpeople.backoffice.common.enumtype.ErrorCode;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class ErrorLogAppenderException extends RuntimeException {

	private ErrorCode errorCode;

	public ErrorLogAppenderException(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	public ErrorLogAppenderException(String message) {
		super(message);
	}

	public ErrorLogAppenderException(ErrorCode errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	public ErrorLogAppenderException(String message, Throwable cause) {
		super(message, cause);
	}

	public ErrorLogAppenderException(Throwable cause) {
		super(cause);
	}

	public ErrorLogAppenderException(String message, Throwable cause, boolean enableSuppression,
		boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
