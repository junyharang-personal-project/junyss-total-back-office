package com.giggalpeople.backoffice.common.exception;

import com.giggalpeople.backoffice.common.enumtype.ErrorCode;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class CryptoException extends RuntimeException {

	private ErrorCode errorCode;

	public CryptoException(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	public CryptoException(String message) {
		super(message);
	}

	public CryptoException(ErrorCode errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	public CryptoException(String message, Throwable cause) {
		super(message, cause);
	}

	public CryptoException(Throwable cause) {
		super(cause);
	}

	public CryptoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
