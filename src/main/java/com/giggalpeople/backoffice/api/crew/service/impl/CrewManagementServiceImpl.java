package com.giggalpeople.backoffice.api.crew.service.impl;

import static com.giggalpeople.backoffice.common.enumtype.ErrorCode.*;
import static com.giggalpeople.backoffice.common.enumtype.SuccessCode.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.giggalpeople.backoffice.api.common.model.Criteria;
import com.giggalpeople.backoffice.api.common.model.Pagination;
import com.giggalpeople.backoffice.api.crew.database.dao.CrewManagementDAO;
import com.giggalpeople.backoffice.api.crew.exception.CrewException;
import com.giggalpeople.backoffice.api.crew.exception.SuggestException;
import com.giggalpeople.backoffice.api.crew.model.dto.request.CrewJoinRequestDTO;
import com.giggalpeople.backoffice.api.crew.model.dto.request.CrewPeopleManagementSearchDTO;
import com.giggalpeople.backoffice.api.crew.model.dto.request.CrewSuggestPeopleManagementSearchDTO;
import com.giggalpeople.backoffice.api.crew.model.dto.request.SuggestRequestDTO;
import com.giggalpeople.backoffice.api.crew.model.dto.response.CrewDetailResponseDTO;
import com.giggalpeople.backoffice.api.crew.model.dto.response.CrewListResponseDTO;
import com.giggalpeople.backoffice.api.crew.model.dto.response.CrewSuggestDetailResponseDTO;
import com.giggalpeople.backoffice.api.crew.model.dto.response.CrewSuggestListResponseDTO;
import com.giggalpeople.backoffice.api.crew.model.vo.JoinInfoVO;
import com.giggalpeople.backoffice.api.crew.model.vo.SuggestInfoVO;
import com.giggalpeople.backoffice.api.crew.service.CrewManagementService;
import com.giggalpeople.backoffice.common.constant.DefaultListResponse;
import com.giggalpeople.backoffice.common.constant.DefaultResponse;
import com.giggalpeople.backoffice.common.util.BcryptEncryptionUtil;
import com.giggalpeople.backoffice.common.util.CryptoUtil;
import com.giggalpeople.backoffice.common.util.DataTypeChangerUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <h2><b>크루 합류 지원서 관리 business Logic 구현체</b></h2>
 */

@Slf4j
@RequiredArgsConstructor
@Service
public class CrewManagementServiceImpl implements CrewManagementService {

	private final CrewManagementDAO crewManagementDAO;

	/**
	 * <b>Google Form을 통해 기깔나는 사람들 크루 합류 지원서가 등록되면 해당 Method를 통해 Data Base에 내용을 저장.</b>
	 *
	 * @param suggestRequestDTO 크루 합류 지원자 신청 정보
	 * @return Long - 등록된 지원자 번호
	 */

	@Override
	public DefaultResponse<List<Long>> allSuggestInfoSave(List<SuggestRequestDTO> suggestRequestDTO) {
		List<Long> crewSuggestIndexArray = new ArrayList<>();

		if (suggestRequestDTO == null) {
			throw new SuggestException(PARAMETER_NULL, PARAMETER_NULL.getMessage());
		}

		for (SuggestRequestDTO crewSuggest : suggestRequestDTO) {
			Optional<String> byCrewNumberInCrewSuggestInfo = crewManagementDAO.findByCrewNumberInCrewSuggestInfo(
				crewSuggest.getCrewNumber());

			if (byCrewNumberInCrewSuggestInfo.isEmpty()) {
				crewSuggestIndexArray.add(
					crewManagementDAO.newSuggestInfoSave(CryptoUtil.crewSuggestInfoEncrypt(crewSuggest)));
			}
		}
		return DefaultResponse.response(CREATE.getStatusCode(), CREATE.getMessage(), crewSuggestIndexArray);
	}

	/**
	 * <b>Google Form을 통해 기깔나는 사람들 크루 합류가 결정된 크루 정보를 해당 Method를 통해 Data Base에 내용을 저장.</b>
	 *
	 * @param crewJoinRequestDTO 크루 내부 사용 정보가 담긴 List
	 * @return Long - 크루 합류 순서 번호
	 */

	@Override
	public DefaultResponse<List<Long>> join(List<CrewJoinRequestDTO> crewJoinRequestDTO) {
		List<Long> crewIndexArray = new ArrayList<>();

		for (CrewJoinRequestDTO crewInfo : crewJoinRequestDTO) {
			if (crewInfo == null) {
				throw new CrewException(PARAMETER_NULL, PARAMETER_NULL.getMessage());
			}

			Optional<String> crewSuggestId = crewManagementDAO.findByCrewNumberInCrewSuggestInfo(
				crewInfo.getCrewNumber());

			if (crewSuggestId.isEmpty()) {
				throw new CrewException(NOT_EXIST_SUGGEST, NOT_EXIST_SUGGEST.getMessage());
			}

			Optional<JoinInfoVO> crewJoinInfoVO = crewManagementDAO.detailCrewInfoFind(crewInfo.getCrewNumber());

			if (crewJoinInfoVO.isEmpty()) {
				CrewJoinRequestDTO changeCrewInfo = crewInfoChange(crewInfo);

				crewIndexArray.add(crewManagementDAO.crewJoin(JoinInfoVO.toVO(changeCrewInfo, crewSuggestId.get())));
			}
		}
		return DefaultResponse.response(CREATE.getStatusCode(), CREATE.getMessage(), crewIndexArray);
	}

	/**
	 * <b>Google Form을 통해 기깔나는 사람들 크루 합류 지원서가 등록되면 해당 Method를 통해 Data Base에 지원자 정보 목록 조회</b>
	 *
	 * @param criteria             페이징 처리를 위한 정보
	 * @param crewSuggestSearchDTO 검색어(검색 조건) Request 객체
	 * @return CrewSuggestListResponseDTO - 지원자 정보
	 */

	@Override
	public DefaultListResponse<List<CrewSuggestListResponseDTO>> allSuggestInfoFind(Criteria criteria,
		CrewSuggestPeopleManagementSearchDTO crewSuggestSearchDTO) {
		List<CrewSuggestListResponseDTO> responseDTOList = new ArrayList<>();

		String crewSuggestInfoSearchEncryption = CryptoUtil.crewSuggestInfoSearchEncryption(
			crewSuggestSearchDTO.getSearchType(), crewSuggestSearchDTO.getSearchWord());

		DataTypeChangerUtil.checkAgreeType(crewSuggestSearchDTO);

		int searchCount = crewManagementDAO.totalSuggestCrewSearchCount(crewSuggestSearchDTO,
			crewSuggestInfoSearchEncryption);

		if (searchCount <= 0) {
			throw new SuggestException(NOT_EXIST_SUGGEST, NOT_EXIST_SUGGEST.getMessage());

		} else if (searchCount == 1) {
			Optional<SuggestInfoVO> byCrewSuggestInfoSearchOneThing = crewManagementDAO.findByCrewSuggestInfoSearchOneThing(
				crewSuggestSearchDTO, crewSuggestInfoSearchEncryption);

			byCrewSuggestInfoSearchOneThing.ifPresent(crewSuggestInfoVO ->
				responseDTOList.add(
					CrewSuggestListResponseDTO.toDTO(
						CryptoUtil.crewSuggestListInfoDecrypt(crewSuggestInfoVO))));

			return DefaultListResponse.response(SUCCESS.getStatusCode(), SUCCESS.getMessage(),
				createPaging(criteria, searchCount), responseDTOList);

		} else {
			return DefaultListResponse.response(
				SUCCESS.getStatusCode(),
				SUCCESS.getMessage(),
				createPaging(criteria, searchCount),
				crewManagementDAO.findByCrewSuggestInfoList(criteria, crewSuggestSearchDTO,
						crewSuggestInfoSearchEncryption)
					.stream()
					.filter(Objects::nonNull)
					.map(CryptoUtil::crewSuggestListInfoDecrypt)
					.map(crewSuggestInfoVO -> CrewSuggestListResponseDTO.builder()
						.suggestId(crewSuggestInfoVO.getSuggestId())
						.crewNumber(crewSuggestInfoVO.getCrewNumber())
						.suggestDate(crewSuggestInfoVO.getSuggestDate())
						.howKnowInfo(crewSuggestInfoVO.getHowKnowInfo())
						.name(crewSuggestInfoVO.getName())
						.sex(crewSuggestInfoVO.getSex())
						.email(crewSuggestInfoVO.getEmail())
						.ageInfo(crewSuggestInfoVO.getAgeInfo())
						.mbti(crewSuggestInfoVO.getMbti())
						.jobInfo(crewSuggestInfoVO.getJobInfo())
						.lastEducational(crewSuggestInfoVO.getLastEducational())
						.stationName(crewSuggestInfoVO.getStationName())
						.suggestPart(crewSuggestInfoVO.getSuggestPart())
						.participation(crewSuggestInfoVO.getParticipation())
						.meetDate(crewSuggestInfoVO.getMeetDate())
						.build())
					.collect(Collectors.toList()));
		}
	}

	/**
	 * <b>Google Form을 통해 기깔나는 사람들 크루 합류 지원서가 등록되면 해당 Method를 통해 Data Base에 지원자 정보 한 건 조회</b>
	 *
	 * @param suggestId 지원 순서 번호
	 * @return CrewSuggestDetailResponseDTO - 지원자 상세 정보
	 */

	@Override
	public DefaultResponse<CrewSuggestDetailResponseDTO> detailSuggestInfoFind(String suggestId) {
		Optional<SuggestInfoVO> crewSuggestInfoVO = crewManagementDAO.detailSuggestInfoFind(suggestId);

		if (crewSuggestInfoVO.isEmpty()) {
			throw new SuggestException(NOT_EXIST_SUGGEST, NOT_EXIST_SUGGEST.getMessage());
		}

		return DefaultResponse.response(SUCCESS.getStatusCode(), SUCCESS.getMessage(),
			CrewSuggestDetailResponseDTO.toDTO(CryptoUtil.crewSuggestDetailInfoDecrypt(crewSuggestInfoVO.get())));
	}

	/**
	 * <b>Google Form을 통해 기깔나는 사람들 크루 합류 지원서가 등록되면 해당 Method를 통해 Data Base에 크루 정보 목록 조회</b>
	 *
	 * @param criteria      페이징 처리를 위한 정보
	 * @param crewSearchDTO 검색어(검색 조건) Request 객체
	 * @return CrewListResponseDTO - 등록된 지원자 정보
	 */

	@Override
	public DefaultListResponse<List<CrewListResponseDTO>> allCrewInfoFind(Criteria criteria,
		CrewPeopleManagementSearchDTO crewSearchDTO) {
		String crewSearchEncryptionValue = CryptoUtil.crewSuggestInfoSearchEncryption(crewSearchDTO.getSearchType(),
			crewSearchDTO.getSearchWord());

		int searchCount = crewManagementDAO.totalCrewSearchCount(crewSearchDTO, crewSearchEncryptionValue);

		if (searchCount <= 0) {
			throw new CrewException(NOT_EXIST_CREW, NOT_EXIST_CREW.getMessage());

		} else if (searchCount == 1) {
			List<CrewListResponseDTO> responseDTOList = new ArrayList<>();

			Optional<JoinInfoVO> byCrewInfoSearchOneThing = crewManagementDAO.findByCrewInfoSearchOneThing(
				crewSearchDTO, crewSearchEncryptionValue);

			byCrewInfoSearchOneThing.ifPresent(crewJoinInfoVO -> responseDTOList.add(
				CrewListResponseDTO.toDTO(CryptoUtil.crewListInfoDecrypt(crewJoinInfoVO))));

			return DefaultListResponse.response(SUCCESS.getStatusCode(), SUCCESS.getMessage(),
				createPaging(criteria, searchCount), responseDTOList);

		} else {
			return DefaultListResponse.response(
				SUCCESS.getStatusCode(),
				SUCCESS.getMessage(),
				createPaging(criteria, searchCount),
				crewManagementDAO.findByCrewInfoList(criteria, crewSearchDTO, crewSearchEncryptionValue)
					.stream()
					.filter(Objects::nonNull)
					.map(CryptoUtil::crewListInfoDecrypt)
					.map(crewJoinInfoVO -> CrewListResponseDTO.builder()
						.suggestId(crewJoinInfoVO.getSuggestId())
						.crewJoinId(crewJoinInfoVO.getCrewJoinId())
						.crewNumber(crewJoinInfoVO.getCrewNumber())
						.suggestDate(crewJoinInfoVO.getSuggestDate())
						.joinDate(crewJoinInfoVO.getJoinDate())
						.name(crewJoinInfoVO.getName())
						.sex(crewJoinInfoVO.getSex())
						.email(crewJoinInfoVO.getEmail())
						.birthDate(crewJoinInfoVO.getBirthDate())
						.ageInfo(crewJoinInfoVO.getAgeInfo())
						.mbti(crewJoinInfoVO.getMbti())
						.stationName(crewJoinInfoVO.getStationName())
						.phoneNumber(crewJoinInfoVO.getPhoneNumber())
						.suggestPart(crewJoinInfoVO.getSuggestPart())
						.crewAlias(crewJoinInfoVO.getCrewAlias())
						.build())
					.collect(Collectors.toList()));
		}
	}

	/**
	 * <b>Google Form을 통해 기깔나는 사람들 크루 합류 지원서가 등록되면 해당 Method를 통해 Data Base에 크루 정보 한 건 조회</b>
	 *
	 * @param crewNumber Google App Script에서 지원자 지원 시 생성하는 크루 번호
	 * @return CrewDetailResponseDTO - 크루 상세 정보
	 */

	@Override
	public DefaultResponse<CrewDetailResponseDTO> detailCrewInfoFind(String crewNumber) {
		Optional<JoinInfoVO> crewJoinInfoVO = crewManagementDAO.detailCrewInfoFind(crewNumber);

		if (crewJoinInfoVO.isEmpty()) {
			throw new CrewException(NOT_EXIST_CREW, NOT_EXIST_CREW.getMessage());
		}
		return DefaultResponse.response(SUCCESS.getStatusCode(), SUCCESS.getMessage(),
			CrewDetailResponseDTO.toDTO(CryptoUtil.crewDetailInfoDecrypt(crewJoinInfoVO.get())));
	}

	/**
	 * <b>크루 중 중도 포기, 강제 퇴출 등으로 인해 탈회 처리가 되면 크루 정보 삭제</b>
	 *
	 * @param crewNumber Google App Script에서 지원자 지원 시 생성하는 크루 번호
	 * @return Long - 삭제된 crew_join_id
	 */

	@Override
	public DefaultResponse<Long> deleteCrewInfo(String crewNumber) {
		String checkCrewNumber = DataTypeChangerUtil.changeCrewNumber(crewNumber);
		Optional<JoinInfoVO> crewJoinInfoVO = crewManagementDAO.detailCrewInfoFind(checkCrewNumber);

		if (crewJoinInfoVO.isEmpty()) {
			throw new CrewException(NOT_EXIST_CREW, NOT_EXIST_CREW.getMessage());
		}

		Long deleteSuccessCheck = crewManagementDAO.deleteCrewInfo(crewJoinInfoVO.get().getCrewJoinId());

		if (deleteSuccessCheck == 1) {
			return DefaultResponse.response(SUCCESS.getStatusCode(), SUCCESS.getMessage(),
				crewJoinInfoVO.get().getCrewJoinId());
		}
		return DefaultResponse.error(INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR.getMessage());
	}

	/**
	 * <b>각 Method 상황 별 Paging 객체를 생성하기 위한 Method.</b>
	 *
	 * @param criteria             Client가 요청한 Paging 처리 정보
	 * @param totalPagingDataCount Data Base에서 조회된 Data 총 개수
	 * @return Pagination - Paging 처리 결과를 담은 객체
	 */
	private Pagination createPaging(Criteria criteria, int totalPagingDataCount) {
		return new Pagination(criteria, totalPagingDataCount);
	}

	/**
	 * <b>Client에서 들어온 크루 지원자 정보를 이용하여 합류한 크루일 경우 필요한 설정을 위한 Method</b>
	 *
	 * @param crewJoinRequestDTO 크루 지원 정보
	 * @return CrewSuggestRequestDTO - 변경 된 크루 지원 정보
	 */

	private CrewJoinRequestDTO crewInfoChange(CrewJoinRequestDTO crewJoinRequestDTO) {
		Optional<Long> byCrewNumberInCrew = crewManagementDAO.findByCrewNumberInCrew(
			crewJoinRequestDTO.getCrewNumber().replace("-", ""));

		if (byCrewNumberInCrew.isEmpty()) {

			crewJoinRequestDTO.setCrewNumber(crewJoinRequestDTO.getCrewNumber().replace("-", ""));

			createInsideCrewInfo(crewJoinRequestDTO);
		}
		return crewJoinRequestDTO;
	}

	/**
	 * <b>Client에서 들어온 크루 지원자 정보를 이용하여 합류한 크루일 경우 계정, 비밀번호, 크루 별칭, 크루 직책(역할) 설정을 위한 Method</b>
	 *
	 * @param crewJoinRequestDTO 합류할 크루의 정보를 담은 DTO
	 */
	private void createInsideCrewInfo(CrewJoinRequestDTO crewJoinRequestDTO) {
		if (Objects.equals(crewJoinRequestDTO.getUserId(), "") || crewJoinRequestDTO.getUserId() == null) {
			crewJoinRequestDTO.setUserId("u" + crewJoinRequestDTO.getCrewNumber().replace("-", ""));
		}

		if (crewJoinRequestDTO.getPassword() == null || Objects.equals(crewJoinRequestDTO.getPassword(), "")) {
			crewJoinRequestDTO.setPassword(BcryptEncryptionUtil.hashPassword(
				"Giggal" + crewJoinRequestDTO.getCrewNumber().replace("-", "") + "!#%"));
		}

		if (crewJoinRequestDTO.getCrewAlias() == null) {
			if (!crewJoinRequestDTO.getCrewNumber().contains("-")) {
				createCrewNumberInDash(crewJoinRequestDTO);
			}

			Optional<SuggestInfoVO> detailCrewBySuggestInfo = crewManagementDAO.joinCrewBySuggestInfo(
				crewJoinRequestDTO.getCrewNumber());

			if (detailCrewBySuggestInfo.isEmpty()) {
				throw new SuggestException(NOT_EXIST_SUGGEST, NOT_EXIST_SUGGEST.getMessage());
			}

			CrewSuggestDetailResponseDTO decryptCrewBySuggestInfo = CrewSuggestDetailResponseDTO.toDTO(
				CryptoUtil.crewSuggestDetailInfoDecrypt(detailCrewBySuggestInfo.get()));

			String crewPart = changePartForCrewAlias(decryptCrewBySuggestInfo.getSuggestPart());

			if (crewJoinRequestDTO.getCrewRole().equals("Crew")) {
				crewJoinRequestDTO.setCrewAlias("[" + crewPart + "]" + decryptCrewBySuggestInfo.getName());
			} else {
				crewJoinRequestDTO.setCrewAlias(
					"[" + changeRoleForCrewAlias(crewJoinRequestDTO.getCrewRole()) + "&" + crewPart + "]"
						+ decryptCrewBySuggestInfo.getName());
			}
		}
	}

	private void createCrewNumberInDash(CrewJoinRequestDTO crewJoinRequestDTO) {
		StringBuilder sb = new StringBuilder();
		sb.append(crewJoinRequestDTO.getCrewNumber());
		sb.insert(4, "-");
		sb.insert(8, "-");

		crewJoinRequestDTO.setCrewNumber(sb.toString());
	}

	/**
	 * <b>Client에서 들어온 크루 직책(역할)을 이용하여 Crew Alias에 적절한 값을 붙히기 위하여 문자열 가공을 위한 Method</b>
	 *
	 * @param crewRole Client에 등록된 크루 직책(역할)
	 * @return String - 크루 별칭으로 붙힐 크루 직책(역할)
	 */

	private String changeRoleForCrewAlias(String crewRole) {
		String crewRoleForCrewAlias = "";

		if (crewRole.contains("Team Leader(TL)")) {
			crewRoleForCrewAlias = "TL";
		} else if (crewRole.contains("Project Leader(PL)")) {
			crewRoleForCrewAlias = "PL";
		} else if (crewRole.contains("Project Manager(PM)")) {
			crewRoleForCrewAlias = "PM";
		}
		return crewRoleForCrewAlias;
	}

	/**
	 * <b>Client에서 들어온 크루 참여 팀을 이용하여 Crew Alias에 적절한 값을 붙히기 위하여 문자열 가공을 위한 Method</b>
	 *
	 * @param suggestPart Client에 등록된 크루 소속 팀
	 * @return String - 크루 별칭으로 붙힐 크루 소속 팀
	 */

	private String changePartForCrewAlias(String suggestPart) {
		String crewAliasPartName;

		if (Objects.equals(suggestPart, "Project Manager(PM)")) {
			crewAliasPartName = "PM";
		} else if (Objects.equals(suggestPart, "서비스 기획")) {
			crewAliasPartName = "기획";
		} else if (Objects.equals(suggestPart, "WEB / APP 디자이너")) {
			crewAliasPartName = "디자인";
		} else if (Objects.equals(suggestPart, "FrontEnd 개발자")) {
			crewAliasPartName = "프엔";
		} else if (Objects.equals(suggestPart, "BackEnd 개발자")) {
			crewAliasPartName = "백엔";
		} else if (Objects.equals(suggestPart, "Android 개발자")) {
			crewAliasPartName = "안드";
		} else if (Objects.equals(suggestPart, "IOS 개발자")) {
			crewAliasPartName = "애플";
		} else if (Objects.equals(suggestPart, "정보 보안 기술자")) {
			crewAliasPartName = "해커";
		} else if (Objects.equals(suggestPart, "AI 개발자")) {
			crewAliasPartName = "AI";
		} else if (Objects.equals(suggestPart, "DMSO(DevMlSecOps)")) {
			crewAliasPartName = "DMSO";
		} else {
			crewAliasPartName = "Lawyer";
		}
		return crewAliasPartName;
	}

	public List<SuggestInfoVO> findByCrewSuggestInfoList(Criteria criteria,
		CrewSuggestPeopleManagementSearchDTO crewSuggestSearchDTO, String crewSuggestInfoSearchEncryption) {
		return crewManagementDAO.findByCrewSuggestInfoList(criteria, crewSuggestSearchDTO,
			crewSuggestInfoSearchEncryption);
	}
}