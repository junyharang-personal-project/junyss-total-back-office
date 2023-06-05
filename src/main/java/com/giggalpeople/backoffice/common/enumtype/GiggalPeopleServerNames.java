package com.giggalpeople.backoffice.common.enumtype;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum GiggalPeopleServerNames {

	GIGGAL_TOTAL_BACK_OFFICE("Giggal-Total-Back-Office", "통합관리서버");

	private String description;
	private String searchCommand;
}
