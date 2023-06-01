package com.giggalpeople.backoffice.api.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <h2><b>목록 조회 Query에 전달될 Paging 처리 매개 변수를 담을 Class</b></h2>
 */

@Schema(description = "Client에서 Paging 관련 값을 받기 위한 Class")
@Getter
@Setter
@ToString
@AllArgsConstructor
public class Criteria {

    @Schema(description = "현재 이용자 Paging 위치값", nullable = true, example = "1", defaultValue = "1")
    private int page;

    @Schema(description = "한 Page에 보여줄 Data 개수", nullable = true, example = "10", defaultValue = "10")
    private int perPageNum;

    @Schema(description = "Paging 처리 시 화면에 보여질 Page Button 개수", nullable = true, example = "10", defaultValue = "10")
    private int displayPageNum;

    /**
     * <b>기본 생성자</b>
     */

    public Criteria() {
        if (page <= 0) {
            this.page = 1;
        }

        if (perPageNum <= 0) {
            this.perPageNum = 10;
        }

        if (displayPageNum <= 0) {
            this.displayPageNum = 10;
        }
    }

    /**
     * <b>특정 Page의 게시글 시작 번호, 게시글 시작 행 번호를 계산하는 Method</b>
     * <b>현재 Page의 게시글 시작 번호 = (현재 Page 번호 - 1) * Page 당 보여줄 게시글 개수</b>
     * @return int 계산된 결과값
     */
    @JsonIgnore
    public int getPageStart() {
        if (perPageNum <= 0) {
            perPageNum = 10;
        }

        int tmpPageStartNum = (page - 1) * perPageNum;

        if (tmpPageStartNum <= 0) {
            return 1;
        } else {
            return tmpPageStartNum;
        }
    }

    /**
     * <b>이용자 Paging 위치를 계산하기 위한 Setter</b>
     * @param page 이용자 Paging 위치값
     */

    public void setPage(int page) {
        if (page <= 0) {                        // 매개 변수로 입력된 이용자 위치가 0보다 작거나 같다면?
            this.page = 1;                      // 이용자 위치가 1보다 작을 수 없기 때문에 1로 변경
        } else {                                // 매개 변수로 입력된 이용자 위치가 0보다 크다면?
            this.page = page;                   // 해당 위치 번호를 그대로 사용
        }
    }

    /**
     * <b>Paging 처리 시 화면에 보여질 Page Button 개수를 계산하기 위한 Setter</b>
     * @param displayPageNum Client에서 요청 받은 화면에 보여질 Page Button 개수
     */

    public void setDisplayPageNum(int displayPageNum) {
        if (displayPageNum <= 0) {
            this.displayPageNum = 10;
        } else {
            this.displayPageNum = displayPageNum;
        }
    }
}
