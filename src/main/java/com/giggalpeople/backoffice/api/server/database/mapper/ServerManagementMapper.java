package com.giggalpeople.backoffice.api.server.database.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.giggalpeople.backoffice.api.server.model.vo.ServerInfoVo;

/**
 * <h2><b>기깔나는 사람들 보유 WAS 정보 관리를 위한 Mapper</b></h2>
 */

@Mapper
@Repository
public interface ServerManagementMapper {

	/**
	 * <b>기깔나는 사람들 보유 WAS 정보 저장을 위한 Method</b>
	 * @param serverInfoVo Server 정보를 담은 VO 객체
	 * @return Data Base 저장 뒤 WAS 순서 번호
	 */

	Long save(ServerInfoVo serverInfoVo);

	/**
	 * <b>기깔나는 사람들 보유 WAS 상세 조회를 위한 Method</b>
	 * @param serverIP WAS IP
	 * @return WAS 상세 정보
	 */
	@Select("select internal_server_id from server_info where server_ip = #{serverIP}")
	Long findByServerID(String serverIP);
}
