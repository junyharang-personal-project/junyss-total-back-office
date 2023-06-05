package com.giggalpeople.backoffice.common.env;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * <h2><b>기깔나는 사람들 보유 서버 종류</b></h2>
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ServerType {

	@Schema(description = "통합 Back Office", nullable = true, example = "TOTAL_BACK_OFFICE")
	TOTAL_BACK_OFFICE("Giggal-Total-Back-Office", "통합관리서버");

	private String description;
	private String searchCommand;
}
