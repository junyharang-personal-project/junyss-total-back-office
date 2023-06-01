package com.giggalpeople.backoffice.api.crew.database.mapper;

import com.giggalpeople.backoffice.api.common.model.Criteria;
import com.giggalpeople.backoffice.api.crew.model.dto.request.CrewPeopleManagementSearchDTO;
import com.giggalpeople.backoffice.api.crew.model.dto.request.CrewSuggestPeopleManagementSearchDTO;
import com.giggalpeople.backoffice.api.crew.model.vo.JoinInfoVO;
import com.giggalpeople.backoffice.api.crew.model.vo.SuggestInfoVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * <h2><b>크루 합류 지원서 관리 Mybatis를 이용한 Data Base Handler</b></h2>
 */

@Mapper
@Repository
public interface CrewManagementMapper {

    /**
     * <b>Google Form을 통해 기깔나는 사람들 크루 합류 지원서가 등록되면 해당 Method를 통해 Data Base에 내용을 저장.</b>
     * @param suggestInfoVO 크루 합류 지원자 신청 정보
     * @return Long - 등록된 지원자 번호
     */

    Long newSuggestInfoSave(SuggestInfoVO suggestInfoVO);

    /**
     * <b>Google Form을 통해 기깔나는 사람들 크루 합류 지원서가 등록되면 suggest Method를 통해 Data Base에 내용을 저장하기 전에 해당 저장 값이 Data Base suggest Table에 있는지 확인하는 Method.</b>
     * @param crewNumber 크루 합류 지원자 크루 번호
     * @return Optional<Long> - 등록된 지원자 번호
     */

    @Select("select suggest_id from suggest where crew_number = #{crewNumber}")
    Optional<String> findByCrewNumberInCrewSuggestInfo(String crewNumber);

    /**
     * <b>Google Form을 통해 기깔나는 사람들 크루 합류 지원서가 등록되면 suggest Method를 통해 Data Base에 내용을 저장하기 전에 해당 저장 값이 Data Base Crew Table에 있는지 확인하는 Method.</b>
     * @param crewNumber 크루 합류 지원자 크루 번호
     * @return Optional<Long> - 등록된 지원자 번호
     */

    @Select("select c.crew_join_id " +
            "from crew as c " +
            "inner join suggest as s " +
            "on c.suggest_id = s.suggest_id " +
            "where s.crew_number = #{crewNumber} ")
    Optional<Long> findByCrewNumberInCrew(String crewNumber);

    /**
     * <b>Google Form을 통해 기깔나는 사람들 크루 합류가 결정된 크루 정보를 해당 Method를 통해 Data Base에 내용을 저장.</b>
     * @param crewJoinInfoVO 크루 정보
     * @return Long - 크루 합류 순서 번호
     */

    Long crewJoin(JoinInfoVO crewJoinInfoVO);

    /** <b>크루와 지원자 모두 검색 결과에 따른 개수 조회</b>
     * @return int - 등록된 지원자 명수
     */

    int totalSuggestCrewSearchCount(@Param("crewSuggestSearchDTO") CrewSuggestPeopleManagementSearchDTO crewSuggestSearchDTO, @Param("crewSuggestInfoSearchEncryption") String crewSuggestInfoSearchEncryption);

    /**
     * <b>Google Form을 통해 기깔나는 사람들 크루 합류 지원서가 등록되면 suggest Method를 통해 Data Base에 내용을 저장하기 전에 해당 저장 값이 Data Base suggest Table에 있는지 확인하는 Method.</b>
     * @param criteria - Paging 처리 정보
     * @param crewSuggestInfoSearchEncryption - 암호화 되어 저장 되어 있는 크루 지원자 정보를 검색하기 위한 암호문 검색어
     * @return CrewSuggestInfoVO - 등록된 지원자 목록 조회를 위한 정보
     */

    List<SuggestInfoVO> findByCrewSuggestInfoList(@Param("criteria") Criteria criteria, @Param("crewSuggestSearchDTO") CrewSuggestPeopleManagementSearchDTO crewSuggestSearchDTO, @Param("crewSuggestInfoSearchEncryption") String crewSuggestInfoSearchEncryption);

    /** <b>Google Form을 통해 기깔나는 사람들 크루 합류 지원서가 등록되면 해당 Method를 통해 Data Base에 지원자 정보 한 건 조회</b>
     * @param suggestId 지원 순서 번
     * @return Optional<CrewSuggestInfoVO> - 등록된 지원자 정보
     */

    @Select("select * from suggest where suggest_id = #{suggestId}")
    Optional<SuggestInfoVO> detailSuggestInfoFind(@Param("suggestId") String suggestId);

    /** <b>크루 합류 지원 전체 개수 조회</b>
     * @return int - 등록된 지원자 명수
     */

    @Select("select count(suggest_id) from suggest")
    int listTotalCount();

    /** <b>합류 크루 목록 조회</b>
     * @param criteria Paging 처리 정보
     * @param crewSearchDTO 검색 조건
     * @return List<CrewJoinInfoVO> - 합류 크루 정보
     */

    List<JoinInfoVO> findByCrewInfoList(@Param("criteria") Criteria criteria, @Param("crewSearchDTO") CrewPeopleManagementSearchDTO crewSearchDTO, @Param("crewSearchEncryptionValue") String crewSearchEncryptionValue);

    /**
     * <b>크루 목록 조회 시 결과가 하나일 경우 해당 값을 찾기 위한 Method</b>
     * @param crewSearchDTO 검색 종류와 검색어가 들어 있는 DTO
     * @param crewSearchEncryptionValue 암호화 되어 저장 되어 있는 크루 정보를 검색하기 위한 암호문 검색어
     * @return Optional<CrewJoinInfoVO> - 조회된 크루 상세 정보
     */

    Optional<JoinInfoVO> findByCrewInfoSearchOneThing(@Param("crewSearchDTO") CrewPeopleManagementSearchDTO crewSearchDTO, @Param("crewSearchEncryptionValue") String crewSearchEncryptionValue);


    /** <b>크루 합류 인원 전체 명수 조회</b>
     * @return int - 등록된 지원자 명수
     */

    @Select("select count(crew_join_id) from crew")
    int totalCrewCount();

    /** <b>크루 합류 검색 결과에 따른 개수 조회</b>
     * @return int - 등록된 지원자 명수
     */

    int totalCrewSearchCount(@Param("crewSearchDTO") CrewPeopleManagementSearchDTO crewSearchDTO, @Param("crewSearchEncryptionValue") String crewSearchEncryptionValue);

    /**
     * <b>Google Form을 통해 기깔나는 사람들 크루 합류 지원서가 등록되면 해당 Method를 통해 Data Base에 크루 정보 한 건 조회</b>
     * @param crewNumber Google App Script에서 지원자 지원 시 생성하는 크루 번호
     * @return Optional<CrewJoinInfoVO> - 크루 상세 정보
     */

    @Select("select c.crew_join_id as crew_join_id, s.*, c.* " +
            "from crew as c " +
            "inner join suggest as s " +
            "on c.suggest_id = s.suggest_id " +
            "where s.crew_number = #{crewNumber} ")
    Optional<JoinInfoVO> detailCrewInfoFind(String crewNumber);

    /**
     * <b>크루 합류 시 해당 크루의 지원 정보를 조회하기 위한 Method</b>
     * @param crewNumber 크루 지원서 작성 시 구글 App Script를 통해 부여된 지원자 혹은 크루 번호
     * @return Data Base에서 조회된 크루의 지원서 상세 정보
     */

    @Select("select * from suggest where crew_number = #{crewNumber} ")
    Optional<SuggestInfoVO> joinCrewBySuggestInfo(String crewNumber);

    /**
     * <b>크루 목록 검색을 통해 조회 시 결과가 하나일 경우 해당 값을 찾기 위한 Method</b>
     * @param crewSuggestSearchDTO 검색 종류와 검색어가 들어 있는 DTO
     * @param crewSuggestInfoSearchEncryption 암호화 되어 저장 되어 있는 크루 정보를 검색하기 위한 암호문 검색어
     * @return Optional<CrewJoinSuggestListVO> - 조회된 지원자 정보
     */

    Optional<SuggestInfoVO> findByCrewSuggestInfoSearchOneThing(@Param("crewSuggestSearchDTO") CrewSuggestPeopleManagementSearchDTO crewSuggestSearchDTO, @Param("crewSuggestInfoSearchEncryption") String crewSuggestInfoSearchEncryption);

    /**
     * <b>크루 중 중도 포기, 강제 퇴출 등으로 인해 탈회 처리가 되면 크루 정보 삭제</b>
     * @param crewJoinId 크루 합류 시 합류 순서 번호
     * @return Long - 삭제 성공 시엔 1 실패 시 0을 반환
     */

    @Delete("delete from crew where crew_join_id = #{crewJoinId}")
    Long deleteCrewInfo(Long crewJoinId);
}
