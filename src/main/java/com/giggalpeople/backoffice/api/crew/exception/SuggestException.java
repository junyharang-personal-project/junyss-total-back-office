package com.giggalpeople.backoffice.api.crew.exception;

import com.giggalpeople.backoffice.common.enumtype.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class SuggestException extends RuntimeException {

    private ErrorCode errorCode;

    public SuggestException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public SuggestException(String message) {
        super(message);
    }

    public SuggestException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public SuggestException(String message, Throwable cause) {
        super(message, cause);
    }

    public SuggestException(Throwable cause) {
        super(cause);
    }

    public SuggestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
