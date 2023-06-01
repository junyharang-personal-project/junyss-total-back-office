package com.giggalpeople.backoffice.common.util;

import com.giggalpeople.backoffice.api.crew.model.dto.enumtype.CrewSuggestSearchType;
import com.giggalpeople.backoffice.api.crew.model.dto.request.SuggestRequestDTO;
import com.giggalpeople.backoffice.api.crew.model.vo.JoinInfoVO;
import com.giggalpeople.backoffice.api.crew.model.vo.SuggestInfoVO;
import com.giggalpeople.backoffice.api.log.model.dto.request.ErrorLogSearchDTO;
import com.giggalpeople.backoffice.api.log.model.vo.LogTotalInfoVO;
import com.giggalpeople.backoffice.api.user.model.dto.enumtype.UserInfoSearchType;
import com.giggalpeople.backoffice.api.user.model.dto.request.UserInfoSearchDTO;
import com.giggalpeople.backoffice.api.user.model.vo.ConnectedUserInfoVO;
import com.giggalpeople.backoffice.api.user.request_info.model.dto.request.ConnectedUserRequestInfoSaveRequestDTO;
import com.giggalpeople.backoffice.api.user.model.dto.request.ConnectedUserInfoSaveRequestDTO;
import com.giggalpeople.backoffice.common.exception.CryptoException;
import com.junyharang.datasecret.DataAesSecret;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Objects;

import static com.giggalpeople.backoffice.common.enumtype.ErrorCode.PARAMETER_NULL;

/**
 * <h2><b>Crew 지원자 및 Crew 정보 암/복호화를 위한 Class</b></h2><
 */

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Component
public class CryptoUtil {

    /**
     * <b>static 변수는 @Value 사용이 불가하기 때문에 getCipherKey로 받아온 값을 다시 넣고, static 변수로 활용하기 위한 변수</b>
     */

    private static String cipherKey;

    /**
     * <b>암호화시 사용될 Key 값을 application-xxx.yml에서 불러오기 위한 변수</b>
     */

    @Value("${aes.security.key}")
    private String getCipherKey;

    /**
     * <b>static 변수는 @Value 사용이 불가하기 때문에 getCipherKey로 받아온 값을 다시 넣고, static 변수로 활용하기 위하여 값을 이동 시켜주기 위한 Method</b>
     */

    @PostConstruct
    public void cipherKeyInit() {
        cipherKey = getCipherKey;
    }

    /**
     * <b>지원자 및 Crew 정보 암호화하여 저장하기 위한 Method</b>
     * @param suggestRequestDTO 크루 합류 지원자 신청 정보
     * @return CrewSuggestRequestDTO - 일부 암호화 된 크루 합류 지원자 신청 정보
     */
    public static SuggestRequestDTO crewSuggestInfoEncrypt(SuggestRequestDTO suggestRequestDTO) {
        suggestRequestDTO.setEmail(DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), suggestRequestDTO.getEmail(), 1));
        suggestRequestDTO.setName(DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), suggestRequestDTO.getName(), 1));
        suggestRequestDTO.setJobInfo(DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), suggestRequestDTO.getJobInfo(), 1));
        suggestRequestDTO.setLastEducational(DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), suggestRequestDTO.getLastEducational(), 1));
        suggestRequestDTO.setSchoolName(DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), suggestRequestDTO.getSchoolName(), 1));
        suggestRequestDTO.setPhoneNumber(DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), suggestRequestDTO.getPhoneNumber(), 1));
        suggestRequestDTO.setTistory(DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), suggestRequestDTO.getTistory(), 1));
        suggestRequestDTO.setFigma(DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), suggestRequestDTO.getFigma(), 1));
        suggestRequestDTO.setNotion(DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), suggestRequestDTO.getNotion(), 1));
        suggestRequestDTO.setBlogUrl(DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), suggestRequestDTO.getBlogUrl(), 1));
        suggestRequestDTO.setTechStack(DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), suggestRequestDTO.getTechStack(), 1));

        if (suggestRequestDTO.getPortfolio() != null) {
            suggestRequestDTO.setPortfolio(DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), suggestRequestDTO.getPortfolio(), 1));
        }
        if (suggestRequestDTO.getEtc() != null) {
            suggestRequestDTO.setEtc(DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), suggestRequestDTO.getEtc(), 1));
        }
        return suggestRequestDTO;
    }

    /**
     * <b>지원자 정보 목록 조회 시 복호화하여 반환하기 위한 Method</b>
     * @param suggestInfoVO 크루 합류 지원자 Data Base 내 일부 암호화 된 신청 정보 중 일부
     * @return CrewSuggestInfoVO - 일부 복호화 된 크루 합류 지원자 신청 정보
     */

    public static SuggestInfoVO crewSuggestListInfoDecrypt(SuggestInfoVO suggestInfoVO) {
        suggestInfoVO.setEmail(DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), suggestInfoVO.getEmail(), 2));
        suggestInfoVO.setName(DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), suggestInfoVO.getName(), 2));
        suggestInfoVO.setJobInfo(DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), suggestInfoVO.getJobInfo(), 2));
        suggestInfoVO.setLastEducational(DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), suggestInfoVO.getLastEducational(), 2));

        return suggestInfoVO;
    }

    /**
     * <b>지원자 정보 상세 조회 시 복호화하여 반환하기 위한 Method</b>
     * @param suggestInfoVO 크루 합류 지원자 Data Base 내 일부 암호화 된 신청 정보 중 일부
     * @return crewSuggestInfoVO - 일부 복호화 된 크루 합류 지원자 신청 정보
     */

    public static SuggestInfoVO crewSuggestDetailInfoDecrypt(SuggestInfoVO suggestInfoVO) {
        suggestInfoVO.setEmail(DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), suggestInfoVO.getEmail(), 2));
        suggestInfoVO.setName(DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), suggestInfoVO.getName(), 2));
        suggestInfoVO.setJobInfo(DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), suggestInfoVO.getJobInfo(), 2));
        suggestInfoVO.setLastEducational(DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), suggestInfoVO.getLastEducational(), 2));
        suggestInfoVO.setSchoolName(DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), suggestInfoVO.getSchoolName(), 2));
        suggestInfoVO.setPhoneNumber(DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), suggestInfoVO.getPhoneNumber(), 2));
        suggestInfoVO.setTistory(DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), suggestInfoVO.getTistory(), 2));
        suggestInfoVO.setFigma(DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), suggestInfoVO.getFigma(), 2));
        suggestInfoVO.setNotion(DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), suggestInfoVO.getNotion(), 2));
        suggestInfoVO.setBlogUrl(DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), suggestInfoVO.getBlogUrl(), 2));
        suggestInfoVO.setTechStack(DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), suggestInfoVO.getTechStack(), 2));

        if (suggestInfoVO.getPortfolio() != null) {
            suggestInfoVO.setPortfolio(DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), suggestInfoVO.getPortfolio(), 2));
        }
        if (suggestInfoVO.getEtc() != null) {
            suggestInfoVO.setEtc(DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), suggestInfoVO.getEtc(), 2));
        }
        return suggestInfoVO;
    }

    /**
     * <b>Crew 정보 목록 조회 시 복호화하여 반환하기 위한 Method</b>
     * @param crewJoinInfoVO 크루 Data Base 내 일부 암호화 된 신청 정보 중 일부
     * @return CrewJoinInfoVO - 일부 복호화 된 크루 정보
     */

    public static JoinInfoVO crewListInfoDecrypt(JoinInfoVO crewJoinInfoVO) {

        crewJoinInfoVO.setEmail(DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), crewJoinInfoVO.getEmail(), 2));
        crewJoinInfoVO.setName(DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), crewJoinInfoVO.getName(), 2));
        crewJoinInfoVO.setPhoneNumber(DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), crewJoinInfoVO.getPhoneNumber(), 2));

        return crewJoinInfoVO;
    }

    /**
     * <b>Crew 정보 상세 조회 시 복호화하여 반환하기 위한 Method</b>
     * @param crewJoinInfoVO 크루 Data Base 내 일부 암호화 된 신청 정보 중 일부
     * @return CrewJoinInfoVO - 일부 복호화 된 크루 합류 지원자 신청 정보
     */

    public static JoinInfoVO crewDetailInfoDecrypt(JoinInfoVO crewJoinInfoVO) {
        crewJoinInfoVO.setEmail(DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), crewJoinInfoVO.getEmail(), 2));
        crewJoinInfoVO.setName(DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), crewJoinInfoVO.getName(), 2));
        crewJoinInfoVO.setJobInfo(DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), crewJoinInfoVO.getJobInfo(), 2));
        crewJoinInfoVO.setLastEducational(DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), crewJoinInfoVO.getLastEducational(), 2));
        crewJoinInfoVO.setSchoolName(DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), crewJoinInfoVO.getSchoolName(), 2));
        crewJoinInfoVO.setPhoneNumber(DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), crewJoinInfoVO.getPhoneNumber(), 2));
        crewJoinInfoVO.setTistory(DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), crewJoinInfoVO.getTistory(), 2));
        crewJoinInfoVO.setFigma(DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), crewJoinInfoVO.getFigma(), 2));
        crewJoinInfoVO.setNotion(DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), crewJoinInfoVO.getNotion(), 2));
        crewJoinInfoVO.setBlogUrl(DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), crewJoinInfoVO.getBlogUrl(), 2));
        crewJoinInfoVO.setPortfolio(DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), crewJoinInfoVO.getPortfolio(), 2));
        crewJoinInfoVO.setTechStack(DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), crewJoinInfoVO.getTechStack(), 2));
        crewJoinInfoVO.setEtc(DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), crewJoinInfoVO.getEtc(), 2));

        return crewJoinInfoVO;
    }

    /**
     * <b>기깔나는 사람들 서비스에 접속한 이용자 정보를 암호화 하여 Data Base에 저장하기 위한 Method</b>
     * @param connectedUserInfoSaveRequestDTO 이용자 IP, 접속 위치, 접속 환경 정보를 담은 DTO 객체
     * @return 암호화된 이용자 정보 DTO 객체
     */
    public static ConnectedUserInfoSaveRequestDTO userInfoEncrypt(ConnectedUserInfoSaveRequestDTO connectedUserInfoSaveRequestDTO) {
        connectedUserInfoSaveRequestDTO.setUserIP(DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), connectedUserInfoSaveRequestDTO.getUserIP(), 1));
        connectedUserInfoSaveRequestDTO.setUserLocation(DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), connectedUserInfoSaveRequestDTO.getUserLocation(), 1));
        connectedUserInfoSaveRequestDTO.setUserEnvironment(DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), connectedUserInfoSaveRequestDTO.getUserEnvironment(), 1));

        return connectedUserInfoSaveRequestDTO;
    }

    /**
     * <b>기깔나는 사람들 서비스에 접속한 이용자 정보를 암호화 하여 Data Base에 저장되어 있기 때문에 검색 시 암호화 하여 Data Base에 검색하기 위한 Method</b>
     * @param errorLogSearchDTO Client가 검색을 위해 입력한 내용을 담은 객체
     * @return 암호화된 이용자 검색 요청 DTO 객체
     */

    public static ErrorLogSearchDTO errorLogSearchEncrypt(ErrorLogSearchDTO errorLogSearchDTO) {
        if (errorLogSearchDTO == null) {
            throw new CryptoException(PARAMETER_NULL, PARAMETER_NULL.getMessage());
        }

        if (errorLogSearchDTO.getSearchType().equals(UserInfoSearchType.USER_IP.getDescription())) {
            errorLogSearchDTO.setSearchWord(DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), errorLogSearchDTO.getSearchWord(), 1));
        }
        return errorLogSearchDTO;
    }

    /**
     * <b>기깔나는 사람들 서비스에 접속한 이용자 정보를 암호화 하여 Data Base에 저장되어 있기 때문에 검색 시 암호화 하여 Data Base에 검색하기 위한 Method</b>
     * @param userInfoSearchDTO Client가 검색을 위해 입력한 내용을 담은 객체
     * @return 암호화된 이용자 검색 요청 DTO 객체
     */
    
    public static UserInfoSearchDTO UserInfoSearchEncrypt(UserInfoSearchDTO userInfoSearchDTO) {
        if (userInfoSearchDTO == null) {
            throw new CryptoException(PARAMETER_NULL, PARAMETER_NULL.getMessage());
        }
        
        if (userInfoSearchDTO.getSearchType().equals(UserInfoSearchType.USER_IP.getDescription())) {
            userInfoSearchDTO.setSearchWord(DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), userInfoSearchDTO.getSearchWord(), 1));
        }
        return userInfoSearchDTO;
    }

    /**
     * <b>Log 발생 시 이용자 요청 정보 암호화 하여 Data Base에 저장하기 위한 Method</b>
     * @param connectedUserRequestInfoSaveRequestDTO 이용자 서비스 이용을 위한 요청 정보를 담은 DTO 객체
     * @return 암호화된 이용자 서비스 이용을 위한 요청 정보를 담은 DTO 객체
     */

    public static ConnectedUserRequestInfoSaveRequestDTO userRequestInfoEncrypt(ConnectedUserRequestInfoSaveRequestDTO connectedUserRequestInfoSaveRequestDTO) {
        connectedUserRequestInfoSaveRequestDTO.setRequestHeader(DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), connectedUserRequestInfoSaveRequestDTO.getRequestHeader(), 1));
        connectedUserRequestInfoSaveRequestDTO.setUserCookies(DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), Arrays.toString(connectedUserRequestInfoSaveRequestDTO.getUserCookiesArray()), 1));
        connectedUserRequestInfoSaveRequestDTO.setRequestParameter(DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), connectedUserRequestInfoSaveRequestDTO.getRequestParameter(), 1));
        connectedUserRequestInfoSaveRequestDTO.setRequestBody(DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), connectedUserRequestInfoSaveRequestDTO.getRequestBody(), 1));

        return connectedUserRequestInfoSaveRequestDTO;
    }

    /**
     * <b>Data Base에 저장된 암호화된 이용자 요청 정보를 복호화 하기 위한 Method</b>
     *
     * @param logTotalInfoVO Error Log의 모든 정보를 Data Base에서 가져온 VO 객체
     */
    public static LogTotalInfoVO forGeneralCrewErrorLogDetailRequestInfoDecrypt(LogTotalInfoVO logTotalInfoVO) {
        return new LogTotalInfoVO(
                logTotalInfoVO.getLogId(), logTotalInfoVO.getDataCreatedDate(), logTotalInfoVO.getDataCreatedTime(),
                logTotalInfoVO.getLevel(), logTotalInfoVO.getServerName(), logTotalInfoVO.getServerVmInfo(),
                logTotalInfoVO.getServerOSInfo(), logTotalInfoVO.getServerIP(), logTotalInfoVO.getServerEnvironment(),
                "", "", "", "", "", "", "",
                logTotalInfoVO.getExceptionBrief(), logTotalInfoVO.getExceptionDetail());
    }

    /**
     * <b>Error Log 상세 조회 시 Data Base에 암호화 되어 저장 되어 있는 이용자 접속 및 요청 정보 복호화 하기 위한 Method</b>
     * @param logTotalInfoVO Error Log 관련 모든 정보 담고 있는 VO
     * @return 복호화 된 Error Log 관련 모든 정보 담고 있는 VO
     */
    public static LogTotalInfoVO errorLogDetailUserTotalInfoDecrypt(LogTotalInfoVO logTotalInfoVO) {

        if(logTotalInfoVO.getRequestBody() != null) {
            return new LogTotalInfoVO(
                    logTotalInfoVO.getLogId(),
                    logTotalInfoVO.getDataCreatedDate(),
                    logTotalInfoVO.getDataCreatedTime(),
                    logTotalInfoVO.getLevel(),
                    logTotalInfoVO.getServerName(),
                    logTotalInfoVO.getServerVmInfo(),
                    logTotalInfoVO.getServerOSInfo(),
                    logTotalInfoVO.getServerIP(),
                    logTotalInfoVO.getServerEnvironment(),
                    DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), logTotalInfoVO.getUserIP(), 2),
                    DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), logTotalInfoVO.getUserEnvironment(), 2),
                    DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), logTotalInfoVO.getUserLocation(), 2),
                    DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), logTotalInfoVO.getRequestHeader(), 2),
                    DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), logTotalInfoVO.getUserCookies(), 2),
                    DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), logTotalInfoVO.getRequestParameter(), 2),
                    DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), logTotalInfoVO.getRequestBody(), 2),
                    logTotalInfoVO.getExceptionBrief(),
                    logTotalInfoVO.getExceptionDetail());

        } else {
            return new LogTotalInfoVO(
                    logTotalInfoVO.getLogId(),
                    logTotalInfoVO.getDataCreatedDate(),
                    logTotalInfoVO.getDataCreatedTime(),
                    logTotalInfoVO.getLevel(),
                    logTotalInfoVO.getServerName(),
                    logTotalInfoVO.getServerVmInfo(),
                    logTotalInfoVO.getServerOSInfo(),
                    logTotalInfoVO.getServerIP(),
                    logTotalInfoVO.getServerEnvironment(),
                    DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), logTotalInfoVO.getUserIP(), 2),
                    DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), logTotalInfoVO.getUserEnvironment(), 2),
                    DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), logTotalInfoVO.getUserLocation(), 2),
                    DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), logTotalInfoVO.getRequestHeader(), 2),
                    DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), logTotalInfoVO.getUserCookies(), 2),
                    DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), logTotalInfoVO.getRequestParameter(), 2),
                    logTotalInfoVO.getRequestBody(),
                    logTotalInfoVO.getExceptionBrief(),
                    logTotalInfoVO.getExceptionDetail());
        }
    }

    /**
     * <b>이용자 접속 및 요청 정보 상세 조회 시 Data Base에 암호화 되어 저장 되어 있는 이용자 접속 및 요청 정보 복호화 하기 위한 Method</b>
     * @param connectedUserInfoVO 이용자 접속 및 요청 정보 담고 있는 VO
     * @return 복호화 된 이용자 접속 및 요청 정보 담고 있는 VO
     */
    public static ConnectedUserInfoVO userInfoDecrypt(ConnectedUserInfoVO connectedUserInfoVO) {
        if(connectedUserInfoVO.getRequestBody() != null) {
            return new ConnectedUserInfoVO(
                    connectedUserInfoVO.getConnectedUserRequestInfoID(),
                    connectedUserInfoVO.getDataCreatedDate(),
                    connectedUserInfoVO.getDataCreatedTime(),
                    connectedUserInfoVO.getServerName(),
                    connectedUserInfoVO.getServerVmInfo(),
                    connectedUserInfoVO.getServerOSInfo(),
                    connectedUserInfoVO.getServerIP(),
                    connectedUserInfoVO.getServerEnvironment(),
                    DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), connectedUserInfoVO.getUserIP(), 2),
                    DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), connectedUserInfoVO.getUserEnvironment(), 2),
                    DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), connectedUserInfoVO.getUserLocation(), 2),
                    DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), connectedUserInfoVO.getRequestHeader(), 2),
                    DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), connectedUserInfoVO.getUserCookies(), 2),
                    DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), connectedUserInfoVO.getRequestParameter(), 2),
                    DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), connectedUserInfoVO.getRequestBody(), 2));
        } else {
            return new ConnectedUserInfoVO(
                    connectedUserInfoVO.getConnectedUserRequestInfoID(),
                    connectedUserInfoVO.getDataCreatedDate(),
                    connectedUserInfoVO.getDataCreatedTime(),
                    connectedUserInfoVO.getServerName(),
                    connectedUserInfoVO.getServerVmInfo(),
                    connectedUserInfoVO.getServerOSInfo(),
                    connectedUserInfoVO.getServerIP(),
                    connectedUserInfoVO.getServerEnvironment(),
                    DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), connectedUserInfoVO.getUserIP(), 2),
                    DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), connectedUserInfoVO.getUserEnvironment(), 2),
                    DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), connectedUserInfoVO.getUserLocation(), 2),
                    DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), connectedUserInfoVO.getRequestHeader(), 2),
                    DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), connectedUserInfoVO.getUserCookies(), 2),
                    DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), connectedUserInfoVO.getRequestParameter(), 2),
                    connectedUserInfoVO.getRequestBody());
        }
    }

    /**
     * <b>Exception 발생으로 Error Log Data Base 저장 시 이용자 정보가 Data Base에 저장 되어 있는지 확인하기 위해 이용자 IP를 사용하는데, Data Base에 암호화 되어 저장 되어 있기 때문에 암호화 하기 위한 Method</b>
     * @param userIP Exception 발생 시 발생 이용자 IP 평문 정보
     * @return 암호화 된 이용자 IP
     */
    public static String encryptUserIP(String userIP) {
        if (userIP != null) {
            return DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), userIP, 1);
        }
        throw new CryptoException(PARAMETER_NULL, PARAMETER_NULL.getMessage());
    }

    /**
     * <b>이용자 IP 복호화 Method</b>
     * @param userIP Data Base에 암호화 되어 저장된 이용자 IP 주소
     * @return 복호화 된 이용자 IP 주소
     */
    public static String userInfoIPDecrypt(String userIP) {
        if (userIP != null) {
            return DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), userIP, 2);
        }
        throw new CryptoException(PARAMETER_NULL, PARAMETER_NULL.getMessage());
    }

    /**
     * <b>목록 조회 시 검색 기능에서 평문을 통해 Data Base에 검색값을 넣으면 검색이 되지 않기 때문에 검색어를 암호화 하기 위한 Method</b>
     * @param searchType 검색 기능에서 검색하고자 하는 카테고리
     * @param searchWord 검색어
     * @return String - if절에 조건이 맞으면 암호화된 검색어가 반환되고, 아니라면 평문 검색어가 반환
     */

    public static String crewSuggestInfoSearchEncryption(String searchType, String searchWord) {
        if (Objects.equals(searchType, CrewSuggestSearchType.NAME.getDescription())
                || (Objects.equals(searchType, CrewSuggestSearchType.LAST_EDUCATIONAL.getDescription())
                || (Objects.equals(searchType, CrewSuggestSearchType.SCHOOL_NAME.getDescription())
                || (Objects.equals(searchType, "phoneNumber")
                || (Objects.equals(searchType, "tistory")
                || (Objects.equals(searchType, "figma")
                || (Objects.equals(searchType, "notion")
                || (Objects.equals(searchType, "blogUrl")
                || (Objects.equals(searchType, "techStack")
                || (Objects.equals(searchType, "portfolio")
                || (Objects.equals(searchType, "etc")))))))))))) {

            return DataAesSecret.aesSecret(256, DataAesSecret.base64Encoder(cipherKey), searchWord, 1);
        } else {
            return searchWord;
        }
    }
}