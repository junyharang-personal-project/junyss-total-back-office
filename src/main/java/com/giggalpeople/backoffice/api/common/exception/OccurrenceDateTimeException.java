package com.giggalpeople.backoffice.api.common.exception;

import com.giggalpeople.backoffice.common.enumtype.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class OccurrenceDateTimeException extends RuntimeException {

    private ErrorCode errorCode;

    public OccurrenceDateTimeException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public OccurrenceDateTimeException(String message) {
        super(message);
    }

    public OccurrenceDateTimeException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public OccurrenceDateTimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public OccurrenceDateTimeException(Throwable cause) {
        super(cause);
    }

    public OccurrenceDateTimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
