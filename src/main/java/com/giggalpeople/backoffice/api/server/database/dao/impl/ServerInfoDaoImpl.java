package com.giggalpeople.backoffice.api.server.database.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.giggalpeople.backoffice.api.server.database.dao.ServerInfoDao;
import com.giggalpeople.backoffice.api.server.database.mapper.ServerManagementMapper;
import com.giggalpeople.backoffice.api.server.model.vo.ServerInfoVo;

import lombok.RequiredArgsConstructor;

/**
 * <h2><b>기깔나는 사람들 보유 WAS 정보 관리를 위한 DAO 구현체</b></h2>
 */

@RequiredArgsConstructor
@Repository
public class ServerInfoDaoImpl implements ServerInfoDao {

	private final ServerManagementMapper serverManagementMapper;

	/**
	 * <b>기깔나는 사람들 보유 WAS 정보 저장을 위한 Method</b>
	 * @param serverInfoVO Server 정보를 담은 VO 객체
	 * @return Data Base 저장 뒤 WAS 순서 번호
	 */
	@Transactional
	@Override
	public Long save(ServerInfoVo serverInfoVO) {
		return serverManagementMapper.save(serverInfoVO);
	}

	/**
	 * <b>기깔나는 사람들 보유 WAS 상세 조회를 위한 Method</b>
	 * @param serverIP WAS IP
	 * @return WAS 상세 정보
	 */
	@Transactional(readOnly = true)
	@Override
	public Long findByServerID(String serverIP) {
		return serverManagementMapper.findByServerID(serverIP);
	}
}
