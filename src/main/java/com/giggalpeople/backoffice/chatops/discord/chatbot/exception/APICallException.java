package com.giggalpeople.backoffice.chatops.discord.chatbot.exception;

import com.giggalpeople.backoffice.common.enumtype.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class APICallException extends RuntimeException {

    private ErrorCode errorCode;

    public APICallException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public APICallException(String message) {
        super(message);
    }

    public APICallException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public APICallException(String message, Throwable cause) {
        super(message, cause);
    }

    public APICallException(Throwable cause) {
        super(cause);
    }

    public APICallException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
