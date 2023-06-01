package com.giggalpeople.backoffice.common.enumtype;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessCode {
    SUCCESS(200, "성공"),
    CREATE(201, "생성 성공");

    private final Integer statusCode;
    private final String message;
}
