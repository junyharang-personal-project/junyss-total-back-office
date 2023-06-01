package com.giggalpeople.backoffice.api.user.exception;

import com.giggalpeople.backoffice.common.enumtype.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class ConnectedUserException extends RuntimeException {

    private ErrorCode errorCode;

    public ConnectedUserException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ConnectedUserException(String message) {
        super(message);
    }

    public ConnectedUserException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ConnectedUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConnectedUserException(Throwable cause) {
        super(cause);
    }

    public ConnectedUserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
