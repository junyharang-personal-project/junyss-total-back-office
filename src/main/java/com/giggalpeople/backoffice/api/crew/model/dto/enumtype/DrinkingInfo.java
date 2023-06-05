package com.giggalpeople.backoffice.api.crew.model.dto.enumtype;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum DrinkingInfo {

	ONE_BOTTLE("소주 기준 1병 이하"),
	ONE_AND_THREE_BOTTLES("소주 기준 1 ~ 3병 미만"),
	THREE_OR_MORE_BOTTLES("소주 기준 3병 이상"),
	NOPE("마시지 않음"),
	NONE("미 작성"),
	ETC("기타");

	private String isKOREANCheck;
}
