package com.giggalpeople.backoffice.api.user.model.dto.request;

import com.giggalpeople.backoffice.common.enumtype.CrewGrade;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <h2><b>이용자 접속 및 요청 정보 상세 조회를 위한 DTO</b></h2>
 */

@Schema(description = "이용자 접속 및 요청 정보 상세 조회를 위한 DTO")
@Data
public class UserInfoDetailSearchRequestDTO {

	@Schema(description = "조회 대상 이용자 요청 정보 ID", nullable = false, example = "1")
	private String connectedUserRequestInfoID;

	@Schema(description = "Discord 사용자 등급", nullable = false, example = "TeamLeader(TL)")
	private CrewGrade crewGrade;
}
