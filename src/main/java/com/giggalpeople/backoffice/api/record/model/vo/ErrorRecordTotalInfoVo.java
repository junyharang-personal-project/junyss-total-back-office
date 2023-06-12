package com.giggalpeople.backoffice.api.record.model.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * <h2><b>Data Base용 Error Log Value Object</b></h2>
 */

@Getter
@NoArgsConstructor
public class ErrorRecordTotalInfoVo {
	private Long logId;
	private String dataCreatedDate;
	private String dataCreatedTime;
	private String level;
	private String serverName;
	private String serverVmInfo;
	private String serverOsInfo;
	private String serverip;
	private String serverEnvironment;
	private String userIP;
	private String userEnvironment;
	private String userLocation;
	private String requestHeader;
	private String userCookies;
	private String requestParameter;
	private String requestBody;
	private String exceptionBrief;
	private String exceptionDetail;

	/**
	 * <b>생성자</b>
	 * @param userIP 이용자 IP 주소
	 * @param userEnvironment 이용자 환경 정보 (브라우저 정보 등)
	 * @param userLocation 이용자 IP 기반 접속 위치
	 * @param requestHeader 이용자가 보낸 요청 Header 정보
	 * secFetchMode 요청 헤더 정보 중 하나로 브라우저가 자원을 가져오는 방식
	 * referer 요청 헤더 정보 중 하나로 이전 웹 페이지의 URL 정보
	 * secChUa 요청 헤더 정보 중 하나로 브라우저 정보
	 * secChUaMobile 요청 헤더 정보 중 요청 장비가 Mobil 인지에 대한 정보
	 * secFetchSite 요청 헤더 정보 중 요청이 동일한 출처, 동일 Site, 혹은 다른 Site에서 오는지 혹은 사용자가 시작한 요청인지 여부
	 * acceptLanguage 요청 헤더 정보 중 Client 언어 환경에 대한 정보(이용자가 사용하는 언어 및 지역)
	 * secChUaPlatform 요청 헤더 정보 중 Client Platform 또는 OS 정보
	 * host 요청 헤더 정보 중 Client가 요청 보낸 Server 정보
	 * connection 요청 헤더 정보 중 Client와 Server간 HTTP 연결 유지 방식 정보
	 * acceptEncoding 요청 헤더 정보 중 Client가 Server로 부터 수신할 수 있는 Contents Encoding 방식
	 * accept 요청 헤더 정보 중 Client가 이해 가능한 Contents(Midia Type) 정의
	 * secFetchDest 요청 헤더 정보 중 브라우저가 자원에 요청을 보낼 때, 목적지(대상) 정보
	 * @param userCookies 이용자가 보낸 요청 쿠키 정보
	 * @param requestParameter 요청 Parameter 정보
	 * @param requestBody 요청 Body 정보
	 * @param exceptionBrief Exeception 간략 정보
	 * @param exceptionDetail Exception 상세 정보
	 */
	public ErrorRecordTotalInfoVo(Long logId,
		String dataCreatedDate,
		String dataCreatedTime,
		String level,
		String serverName,
		String serverVmInfo,
		String serverOsInfo,
		String serverip,
		String serverEnvironment,
		String userIP,
		String userEnvironment,
		String userLocation,
		String requestHeader,
		String userCookies,
		String requestParameter,
		String requestBody,
		String exceptionBrief,
		String exceptionDetail) {

		this.logId = logId;
		this.dataCreatedDate = dataCreatedDate;
		this.dataCreatedTime = dataCreatedTime;
		this.level = level;
		this.serverName = serverName;
		this.serverVmInfo = serverVmInfo;
		this.serverOsInfo = serverOsInfo;
		this.serverip = serverip;
		this.serverEnvironment = serverEnvironment;
		this.userIP = userIP;
		this.userEnvironment = userEnvironment;
		this.userLocation = userLocation;
		this.requestHeader = requestHeader;
		this.userCookies = userCookies;
		this.requestParameter = requestParameter;
		this.requestBody = requestBody;
		this.exceptionBrief = exceptionBrief;
		this.exceptionDetail = exceptionDetail;
	}
}
