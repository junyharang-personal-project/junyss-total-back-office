package com.giggalpeople.backoffice.api.crew.model.dto.enumtype;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum WhetherType {
    NO("NO", 0),
    YES("YES", 1),
    UNKNOWN("미 작성", 3);

    private String input;
    private int value;
}
