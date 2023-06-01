package com.giggalpeople.backoffice.api.crew.model.dto.enumtype;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum AgreeType {

    AGREE("동의", true),
    DISAGREE("미동의", false);

    private String description;
    private boolean checkAgree;
}
