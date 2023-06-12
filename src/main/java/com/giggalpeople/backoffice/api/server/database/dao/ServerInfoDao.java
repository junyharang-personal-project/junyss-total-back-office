package com.giggalpeople.backoffice.api.server.database.dao;

import com.giggalpeople.backoffice.api.server.model.vo.ServerInfoVo;

/**
 * <h2><b>기깔나는 사람들 보유 WAS 정보 관리를 위한 DAO</b></h2>
 */

public interface ServerInfoDao {

	/**
	 * <b>기깔나는 사람들 보유 WAS 정보 저장을 위한 Method</b>
	 * @param serverInfoVO Server 정보를 담은 VO 객체
	 * @return Data Base 저장 뒤 WAS 순서 번호
	 */

	Long save(ServerInfoVo serverInfoVO);

	/**
	 * <b>기깔나는 사람들 보유 WAS 상세 조회를 위한 Method</b>
	 * @param serverIP WAS IP
	 * @return WAS 상세 정보
	 */

	Long findByServerID(String serverIP);
}
