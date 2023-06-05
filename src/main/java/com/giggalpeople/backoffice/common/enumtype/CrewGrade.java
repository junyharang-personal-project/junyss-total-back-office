package com.giggalpeople.backoffice.common.enumtype;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum CrewGrade {

	TOTAL_LEADER("대표", 0),
	PROJECT_MANAGER("ProjectManager(PM)", 1),
	PROJECT_LEADER("ProjectLeader(PL)", 2),
	TEAM_LEADER("TeamLeader(TL)", 3),
	GENERAL_CREW("CREW", 4);

	private String grade;
	private int gradeNum;

}
