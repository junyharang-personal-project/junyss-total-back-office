package com.giggalpeople.backoffice.common.constant;

import com.giggalpeople.backoffice.api.common.model.Pagination;
import com.giggalpeople.backoffice.common.enumtype.ErrorCode;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * <h2><b>Controller에서 Response를 최종적으로 만들 때, Custom 하게 Response 값을 전달 및 Paging 정보 전달 하기 위한 Class</b></h2>
 */

@Schema(description = "Controller에서 Response를 최종적으로 만들 때, Custom 하게 Response 값을 전달 및 Paging 정보 전달 하기 위한 Class")
@Data
@AllArgsConstructor
@Builder
public class DefaultListResponse<T> {

	@Schema(description = "API 처리 결과 상태 코드", example = "200")
	private Integer statusCode;

	@Schema(description = "API 처리 결과 부가 설명 (한글)", example = "처리 완료")
	private String message;

	@Schema(description = "API Paging 처리 결과 응답 데이터", example = "\"pagination\": {\n" +
		"\"criteria\": {\n" +
		"\"page\": 1,\n" +
		"\"perPageNum\": 20,\n" +
		"\"displayPageNum\": 10\n" +
		"},\n" +
		"\"startPage\": 0,\n" +
		"\"endPage\": 0,\n" +
		"\"totalCount\": 0,\n" +
		"\"prev\": true,\n" +
		"\"next\": true\n" +
		"}")
	private Pagination pagination;

	@Schema(description = "API 처리 결과 응답 데이터", example = "Success")
	private T data;

	/**
	 * <b>상태 코드 + 부가 설명 반환</b>
	 *
	 * @param statusCode Http Status Code Number
	 * @param message Response Body에 응답할 Message
	 * @return <T> DefaultResponse<T> Response Body 응답값
	 */

	public static <T> DefaultListResponse<T> response(final Integer statusCode, final String message) {
		return (DefaultListResponse<T>)DefaultListResponse.builder()
			.statusCode(statusCode)
			.message(message)
			.build();
	}

	/**
	 * <b>상태 코드 + 부가 설명 + 응답 데이터 반환</b>
	 *
	 * @param statusCode Http Status Code Number
	 * @param message Response Body에 응답할 Message
	 * @param data 연산 뒤 처리 결과값 객체
	 * @return <T> DefaultResponse<T> Response Body 응답값
	 */

	public static <T> DefaultListResponse<T> response(final Integer statusCode, final String message,
		final Pagination pagination, final T data) {
		return (DefaultListResponse<T>)DefaultListResponse.builder()
			.statusCode(statusCode)
			.message(message)
			.pagination(pagination)
			.data(data)
			.build();
	}

	/**
	 * <b>에러 코드 </b>
	 *
	 * @param errorCode  errorCode
	 * @return <T> DefaultResponse<T> Response Body 응답값
	 */

	public static <T> DefaultListResponse<T> error(ErrorCode errorCode) {
		return (DefaultListResponse<T>)DefaultListResponse.builder()
			.statusCode(errorCode.getStatusCode())
			.build();
	}

	/**
	 * <b>에러 코드 </b>
	 *
	 * @param errorCode  errorCode
	 * @param args  message의 치환자와 교체될 필드명
	 * @return <T> DefaultResponse<T> Response Body 응답값
	 */

	public static <T> DefaultListResponse<T> error(ErrorCode errorCode, String... args) {
		return (DefaultListResponse<T>)DefaultListResponse.builder()
			.statusCode(errorCode.getStatusCode())
			.message(errorCode.getMessage(args))
			.build();
	}
}
