package com.giggalpeople.backoffice.api.crew.exception;

import com.giggalpeople.backoffice.common.enumtype.ErrorCode;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class CrewException extends RuntimeException {

	private ErrorCode errorCode;

	public CrewException(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	public CrewException(String message) {
		super(message);
	}

	public CrewException(ErrorCode errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	public CrewException(String message, Throwable cause) {
		super(message, cause);
	}

	public CrewException(Throwable cause) {
		super(cause);
	}

	public CrewException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
