package com.giggalpeople.backoffice.common.env.resource;

import java.util.List;

public interface ServerResourceCheck {

	/**
	 * <b>외부에서 Server 자원 정보 조회 시 호출하여 모든 정보를 가져오는 Method</b>
	 * @param responseCount 초당 조회 횟수
	 * @return Server 자원 관련 조회 정보(CPU, Memory, Disk)
	 */
	List<String> getServerResourcesInfo(int countPerMilliSecond, int responseCount);

	/**
	 * <b>Application Disk 용량 정보 가져오기 위한 Method</b>
	 * @return Disk 총 용량과 사용량 정보를 담은 List
	 */

	StringBuilder getDiskSpaceInfo();

	/**
	 * <b>Application CPU 사용량 정보 가져오기 위한 Method</b>
	 * @return CPU 사용량 정보를 담은 문자열
	 */

	String getCPUProcessInfo();

	/**
	 * <b>Application Memory 사용량 정보 가져오기 위한 Method</b>
	 * @return Heap Memory, Non Heap Memory, 총 Memory 중 사용 가능 Memory 용량을 뺀 정보를 담은 List
	 */

	StringBuilder getMemoryInfo();
}
