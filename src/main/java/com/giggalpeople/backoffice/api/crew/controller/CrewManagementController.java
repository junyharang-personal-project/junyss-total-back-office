package com.giggalpeople.backoffice.api.crew.controller;

import com.giggalpeople.backoffice.api.common.constant.APIUriInfo;
import com.giggalpeople.backoffice.common.annotaion.ExecutionTimeCheck;
import com.giggalpeople.backoffice.common.annotaion.UserAccessInfoCheck;
import com.giggalpeople.backoffice.common.constant.DefaultListResponse;
import com.giggalpeople.backoffice.common.constant.DefaultResponse;
import com.giggalpeople.backoffice.api.common.model.Criteria;
import com.giggalpeople.backoffice.api.crew.model.dto.request.CrewJoinRequestDTO;
import com.giggalpeople.backoffice.api.crew.model.dto.request.CrewPeopleManagementSearchDTO;
import com.giggalpeople.backoffice.api.crew.model.dto.request.SuggestRequestDTO;
import com.giggalpeople.backoffice.api.crew.model.dto.request.CrewSuggestPeopleManagementSearchDTO;
import com.giggalpeople.backoffice.api.crew.model.dto.response.CrewDetailResponseDTO;
import com.giggalpeople.backoffice.api.crew.model.dto.response.CrewListResponseDTO;
import com.giggalpeople.backoffice.api.crew.model.dto.response.CrewSuggestDetailResponseDTO;
import com.giggalpeople.backoffice.api.crew.model.dto.response.CrewSuggestListResponseDTO;
import com.giggalpeople.backoffice.api.crew.service.CrewManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <h2><b>크루 합류 지원서 관리 API</b></h2>
 */

@Tag(name = "크루 합류 API", description = "크루 합류 지원 관리 API 입니다.")
@Slf4j
@RequiredArgsConstructor
@RequestMapping(APIUriInfo.API_PREFIX_URN + APIUriInfo.API_VERSION)
@RestController
public class CrewManagementController {

    private final CrewManagementService crewManagementService;

    @Operation(summary = "모든 크루 지원자 정보 저장", description = "모든 크루 지원자 지원서 정보 자동으로 저장하기 위한 API Method.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "등록 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @UserAccessInfoCheck
    @ExecutionTimeCheck
    @PostMapping(APIUriInfo.API_SUGGEST)
    public DefaultResponse<List<Long>> allSuggestInfoSave(@Valid @RequestBody List<SuggestRequestDTO> suggestRequestDTO) {
        return crewManagementService.allSuggestInfoSave(suggestRequestDTO);
    }

    @Operation(summary = "크루 합류자 정보 저장", description = "크루 합류 확정 인원 자동으로 저장하기 위한 API Method.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "등록 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @UserAccessInfoCheck
    @ExecutionTimeCheck
    @PostMapping(APIUriInfo.API_CREW)
    public DefaultResponse<List<Long>> join(@Valid @RequestBody List<CrewJoinRequestDTO> crewJoinRequestDTO) {
        return crewManagementService.join(crewJoinRequestDTO);
    }

    @Operation(summary = "크루 지원자 정보 목록 조회", description = "모든 크루 지원자 지원서 정보 목록 조회 위한 API Method.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "작업 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "404", description = "요청 정보 찾을 수 없음")
    })
    @UserAccessInfoCheck
    @ExecutionTimeCheck
    @GetMapping(APIUriInfo.API_SUGGEST)
    public DefaultListResponse<List<CrewSuggestListResponseDTO>> allSuggestInfoFind(
            @ModelAttribute("criteria") Criteria criteria,
            @Valid @ModelAttribute("crewSuggestSearchDTO") CrewSuggestPeopleManagementSearchDTO crewSuggestSearchDTO) {
        return crewManagementService.allSuggestInfoFind(criteria, crewSuggestSearchDTO);
    }

    @Operation(summary = "크루 지원자 정보 상세 조회", description = "크루 지원자 지원서 정보 상세 조회 위한 API Method.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "작업 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "404", description = "요청 정보 찾을 수 없음")
    })
    @UserAccessInfoCheck
    @ExecutionTimeCheck
    @GetMapping(APIUriInfo.API_SUGGEST + "/{suggestId}")
    public DefaultResponse<CrewSuggestDetailResponseDTO> detailSuggestInfoFind(@PathVariable("suggestId") String suggestId) {
        return crewManagementService.detailSuggestInfoFind(suggestId);
    }

    @Operation(summary = "합류 크루 정보 목록 조회", description = "합류 크루 정보 목록 조회 위한 API Method.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "작업 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "404", description = "요청 정보 찾을 수 없음")
    })
    @UserAccessInfoCheck
    @ExecutionTimeCheck
    @GetMapping(APIUriInfo.API_CREW)
    public DefaultListResponse<List<CrewListResponseDTO>> allCrewInfoFind(
            @ModelAttribute("criteria") Criteria criteria,
            @ModelAttribute("crewSearchDTO") CrewPeopleManagementSearchDTO crewSearchDTO) {
        return crewManagementService.allCrewInfoFind(criteria, crewSearchDTO);
    }

    @Operation(summary = "합류 크루 정보 상세 조회", description = "합류 크루 정보 상세 조회 위한 API Method.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "작업 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "404", description = "요청 정보 찾을 수 없음")
    })
    @UserAccessInfoCheck
    @ExecutionTimeCheck
    @GetMapping(APIUriInfo.API_CREW + "/{crewNumber}")
    public DefaultResponse<CrewDetailResponseDTO> detailCrewInfoFind(@PathVariable("crewNumber") String crewNumber) {
        return crewManagementService.detailCrewInfoFind(crewNumber);
    }

    @Operation(summary = "합류 크루 탈회", description = "합류 크루 탈회로 인한 정보 크루 정보 삭제 API Method.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "작업 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "404", description = "요청 정보 찾을 수 없음")
    })
    @UserAccessInfoCheck
    @ExecutionTimeCheck
    @DeleteMapping(APIUriInfo.API_CREW + "/{crewNumber}")
    public DefaultResponse<Long> deleteCrewInfo(@RequestParam("crewNumber") String crewNumber) {
        return crewManagementService.deleteCrewInfo(crewNumber);
    }
}