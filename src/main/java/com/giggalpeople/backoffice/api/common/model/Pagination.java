package com.giggalpeople.backoffice.api.common.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <h2><b>목록 조회 Paging 처리 Class</b></h2>
 * <b>Paging 관련 Button 기능을 반환하기 위한 Class</b>
 */
@Schema(description = "Paging 관련 계산을 위한 Class")
@Getter
@Setter
@ToString
public class Pagination {

    @Schema(description = "목록 조회 Query에 전달될 Paging 처리 매개 변수를 담을 객체")
    private Criteria criteria;

    @Schema(description = "첫번째 Page 번호(시작 Page 번호)")
    private int startPage;

    @Schema(description = "마지막 Page 번호")
    private int endPage;

    @Schema(description = "총 Data 개수")
    private int totalCount;

    @Schema(description = "이전으로 이동하기 Button 활성화 여부")
    private boolean prev;

    @Schema(description = "다음으로 이동하기 Button 활성화 여부")
    private boolean next;

    /**
     * <b>Paging 처리를 위해 계산하기 위한 생성자</b>
     * @param criteria Client에게 입력 받은 Paging 처리에 필요한 값
     * @param totalCount 조회하고자 하는 Data 총 개수
     */
    public Pagination(Criteria criteria, int totalCount) {
        this.criteria = criteria;
        this.totalCount = totalCount;

        this.endPage = (int) (Math.ceil(criteria.getPage() / (double) criteria.getDisplayPageNum()) * criteria.getDisplayPageNum());

        this.startPage = (endPage - criteria.getDisplayPageNum()) + 1;

        if (startPage <= 0) {
            startPage = 1;
        }

        this.prev = criteria.getPage() > this.startPage;

        int realEndPage = (int) (Math.ceil((totalCount * 1.0) / criteria.getPerPageNum()));

        this.endPage = realEndPage <= endPage ? realEndPage : endPage;
        this.next = criteria.getPage() < endPage && totalCount >= criteria.getDisplayPageNum();
    }
}
