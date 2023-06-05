package com.giggalpeople.backoffice.api.crew.model.dto.enumtype;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum Participation {
	CHECK("검토 중", "검토"),
	JOIN("참가", "참가"),
	TEST("테스트 중", "테스트"),
	MEETING_EXPECTED("대면 만남 예정", "대면만남예정"),
	JOIN_GIVE_UP("합류 포기", "합류포기"),
	MID_WAY_GIVE_UP("중도 포기", "중도포기"),
	FORCED_EXIT("강제 퇴출", "퇴출"),
	TEAM_LEADER_JOIN_REFUSAL("팀장 합류 거부", "팀장합류거부"),
	PROJECT_LEADER_JOIN_REFUSAL("PM 합류 거부", "PM합류거부"),
	BOSS_JOIN_REFUSAL("대표 합류 거부", "대표합류거부");

	private String inParticipationInfo;
	private String saveParticipationInfo;
}
