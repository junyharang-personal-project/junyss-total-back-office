package com.giggalpeople.backoffice.api.crew.database.dao.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.giggalpeople.backoffice.api.common.model.Criteria;
import com.giggalpeople.backoffice.api.crew.database.dao.CrewManagementDAO;
import com.giggalpeople.backoffice.api.crew.database.mapper.CrewManagementMapper;
import com.giggalpeople.backoffice.api.crew.model.dto.request.CrewPeopleManagementSearchDTO;
import com.giggalpeople.backoffice.api.crew.model.dto.request.CrewSuggestPeopleManagementSearchDTO;
import com.giggalpeople.backoffice.api.crew.model.dto.request.SuggestRequestDTO;
import com.giggalpeople.backoffice.api.crew.model.vo.JoinInfoVO;
import com.giggalpeople.backoffice.api.crew.model.vo.SuggestInfoVO;

import lombok.RequiredArgsConstructor;

/**
 * <h2><b>크루 합류 지원서 관리 Mybatis를 이용한 Data Access Object 구현체</b></h2>
 */

@RequiredArgsConstructor
@Repository
public class CrewManagementDAOImpl implements CrewManagementDAO {

	/**
	 * <b>Data Base Handling을 위한 Mapper Interface final 주입</b>
	 */
	private final CrewManagementMapper crewManagementMapper;

	/**
	 * <b>Google Form을 통해 기깔나는 사람들 크루 합류 지원서가 등록되면 해당 Method를 통해 Data Base에 내용을 저장.</b>
	 * @param suggestRequestDTO 크루 합류 지원자 신청 정보
	 * @return Long - 등록된 지원자 번호
	 */

	@Transactional
	@Override
	public Long newSuggestInfoSave(SuggestRequestDTO suggestRequestDTO) {
		return crewManagementMapper.newSuggestInfoSave(SuggestInfoVO.toVO(suggestRequestDTO));
	}

	/**
	 * <b>Google Form을 통해 기깔나는 사람들 크루 합류 지원서가 등록되면 suggest Method를 통해 Data Base에 내용을 저장하기 전에 해당 저장 값이 Data Base crew_suggest_info Table에 있는지 확인하는 Method.</b>
	 * @param crewNumber 크루 합류 지원자 크루 번호
	 * @return Optional<Long> - 등록된 지원자 번호
	 */

	@Transactional(readOnly = true)
	@Override
	public Optional<String> findByCrewNumberInCrewSuggestInfo(String crewNumber) {
		return crewManagementMapper.findByCrewNumberInCrewSuggestInfo(crewNumber);
	}

	/**
	 * <b>Google Form을 통해 기깔나는 사람들 크루 합류 지원서가 등록되면 suggest Method를 통해 Data Base에 내용을 저장하기 전에 해당 저장 값이 Data Base crew_suggest_info Table에 있는지 확인하는 Method.</b>
	 * @param crewNumber 크루 합류 지원자 크루 번호
	 * @return Optional<Long> - 등록된 지원자 번호
	 */

	@Transactional(readOnly = true)
	@Override
	public Optional<Long> findByCrewNumberInCrew(String crewNumber) {
		return crewManagementMapper.findByCrewNumberInCrew(crewNumber);
	}

	/**
	 * <b>Google Form을 통해 기깔나는 사람들 크루 합류가 결정된 크루 정보를 해당 Method를 통해 Data Base에 내용을 저장.</b>
	 * @param crewJoinInfoVO 크루 정보
	 * @return Long - 크루 합류 순서 번호
	 */

	@Transactional
	@Override
	public Long crewJoin(JoinInfoVO crewJoinInfoVO) {
		return crewManagementMapper.crewJoin(crewJoinInfoVO);
	}

	/**
	 * <b>Google Form을 통해 기깔나는 사람들 크루 합류 지원서가 등록되면 suggest Method를 통해 Data Base에 내용을 저장하기 전에 해당 저장 값이 Data Base crew_suggest_info Table에 있는지 확인하는 Method.</b>
	 * @param - 추후 인증/인가 개발 시 추가
	 * @return CrewSuggestInfoVO - 등록된 지원자 목록 조회를 위한 정보
	 */

	@Transactional(readOnly = true)
	@Override
	public List<SuggestInfoVO> findByCrewSuggestInfoList(Criteria criteria,
		CrewSuggestPeopleManagementSearchDTO crewSuggestSearchDTO, String crewSuggestInfoSearchEncryption) {
		return crewManagementMapper.findByCrewSuggestInfoList(criteria, crewSuggestSearchDTO,
			crewSuggestInfoSearchEncryption);
	}

	/** <b>Google Form을 통해 기깔나는 사람들 크루 합류 지원서가 등록되면 해당 Method를 통해 Data Base에 지원자 정보 한 건 조회</b>
	 * @param suggestId 지원 순서 번호
	 * @return Optional<CrewSuggestInfoVO> - 등록된 지원자 정보
	 */

	@Transactional(readOnly = true)
	@Override
	public Optional<SuggestInfoVO> detailSuggestInfoFind(String suggestId) {
		return crewManagementMapper.detailSuggestInfoFind(suggestId);
	}

	/** <b>크루 합류 지원 전체 개수 조회</b>
	 * @return int - 등록된 지원자 명수
	 */

	@Transactional(readOnly = true)
	@Override
	public int listTotalCount() {
		return crewManagementMapper.listTotalCount();
	}

	/** <b>합류 크루 목록 조회</b>
	 * @param criteria Paging 처리 정보
	 * @param crewSearchDTO 검색 조건
	 * @return List<CrewJoinInfoVO> - 합류 크루 정보
	 */

	@Transactional(readOnly = true)
	@Override
	public List<JoinInfoVO> findByCrewInfoList(Criteria criteria, CrewPeopleManagementSearchDTO crewSearchDTO,
		String crewSearchEncryptionValue) {
		return crewManagementMapper.findByCrewInfoList(criteria, crewSearchDTO, crewSearchEncryptionValue);
	}

	/**
	 * <b>크루 목록 검색을 통해 조회 시 결과가 하나일 경우 해당 값을 찾기 위한 Method</b>
	 * @param crewSearchDTO 검색 종류와 검색어가 들어 있는 DTO
	 * @param crewSearchEncryptionValue 암호화 되어 저장 되어 있는 크루 정보를 검색하기 위한 암호문 검색어
	 * @return Optional<CrewJoinInfoVO> - 조회된 크루 상세 정보
	 */
	@Override
	public Optional<JoinInfoVO> findByCrewInfoSearchOneThing(CrewPeopleManagementSearchDTO crewSearchDTO,
		String crewSearchEncryptionValue) {
		return crewManagementMapper.findByCrewInfoSearchOneThing(crewSearchDTO, crewSearchEncryptionValue);
	}

	/** <b>크루 합류 인원 전체 명수 조회</b>
	 * @return int - 등록된 지원자 명수
	 */

	@Transactional(readOnly = true)
	@Override
	public int totalCrewCount() {
		return crewManagementMapper.totalCrewCount();
	}

	/** <b>크루 합류 검색 결과에 따른 개수 조회</b>
	 * @return int - 등록된 지원자 명수
	 */

	@Transactional(readOnly = true)
	@Override
	public int totalCrewSearchCount(CrewPeopleManagementSearchDTO crewSearchDTO, String crewSearchEncryptionValue) {
		return crewManagementMapper.totalCrewSearchCount(crewSearchDTO, crewSearchEncryptionValue);
	}

	/** <b>크루 지원자 검색 결과에 따른 개수 조회</b>
	 * @return int - 등록된 지원자 명수
	 */

	@Transactional(readOnly = true)
	@Override
	public int totalSuggestCrewSearchCount(CrewSuggestPeopleManagementSearchDTO crewSuggestSearchDTO,
		String crewSuggestInfoSearchEncryption) {
		return crewManagementMapper.totalSuggestCrewSearchCount(crewSuggestSearchDTO, crewSuggestInfoSearchEncryption);
	}

	/**
	 * <b>크루 지원자 목록 조회 시 결과가 하나일 경우 해당 값을 찾기 위한 Method</b>
	 * @param crewSuggestSearchDTO 검색 종류와 검색어가 들어 있는 DTO
	 * @param crewSuggestInfoSearchEncryption 암호화 되어 저장 되어 있는 크루 정보를 검색하기 위한 암호문 검색어
	 * @return Optional<CrewSuggestInfoVO> - 조회된 지원자 정보
	 */

	@Transactional(readOnly = true)
	@Override
	public Optional<SuggestInfoVO> findByCrewSuggestInfoSearchOneThing(
		CrewSuggestPeopleManagementSearchDTO crewSuggestSearchDTO, String crewSuggestInfoSearchEncryption) {
		return crewManagementMapper.findByCrewSuggestInfoSearchOneThing(crewSuggestSearchDTO,
			crewSuggestInfoSearchEncryption);
	}

	/**
	 * <b>Google Form을 통해 기깔나는 사람들 크루 합류 지원서가 등록되면 해당 Method를 통해 Data Base에 크루 정보 한 건 조회</b>
	 * @param crewNumber Google App Script에서 지원자 지원 시 생성하는 크루 번호
	 * @return Optional<CrewJoinInfoVO> - 크루 상세 정보
	 */

	@Transactional(readOnly = true)
	@Override
	public Optional<JoinInfoVO> detailCrewInfoFind(String crewNumber) {
		return crewManagementMapper.detailCrewInfoFind(crewNumber);
	}

	/**
	 * <b>크루 합류 시 해당 크루의 지원 정보를 조회하기 위한 Method</b>
	 * @param crewNumber 크루 지원서 작성 시 구글 App Script를 통해 부여된 지원자 혹은 크루 번호
	 * @return Data Base에서 조회된 크루의 지원서 상세 정보
	 */

	@Transactional(readOnly = true)
	@Override
	public Optional<SuggestInfoVO> joinCrewBySuggestInfo(String crewNumber) {
		return crewManagementMapper.joinCrewBySuggestInfo(crewNumber);
	}

	/**
	 * <b>크루 중 중도 포기, 강제 퇴출 등으로 인해 탈회 처리가 되면 크루 정보 삭제</b>
	 * @param crewJoinId 크루 합류 시 합류 순서 번호
	 * @return Long - 삭제 성공 시엔 1 실패 시 0을 반환
	 */

	@Transactional
	@Override
	public Long deleteCrewInfo(Long crewJoinId) {
		return crewManagementMapper.deleteCrewInfo(crewJoinId);
	}
}
