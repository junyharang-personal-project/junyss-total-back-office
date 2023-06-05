package com.giggalpeople.backoffice.common.env.exception;

import com.giggalpeople.backoffice.common.enumtype.ErrorCode;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class ServerInfoException extends RuntimeException {

	private ErrorCode errorCode;

	public ServerInfoException(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	public ServerInfoException(String message) {
		super(message);
	}

	public ServerInfoException(ErrorCode errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	public ServerInfoException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServerInfoException(Throwable cause) {
		super(cause);
	}

	public ServerInfoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
