package com.giggalpeople.backoffice.api.webhook.test;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.giggalpeople.backoffice.api.common.constant.APIUriInfo;
import com.giggalpeople.backoffice.common.annotaion.ExecutionTimeCheck;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Log Back Web Hook TEST API", description = "Log Back을 통한 Web Hook으로 SNS 전달 Test용 API 입니다.")
@Slf4j
@RequiredArgsConstructor
@RequestMapping(APIUriInfo.API_PREFIX_URN + APIUriInfo.TEST)
@RestController
public class WebHookTestController {

	@ExecutionTimeCheck
	@GetMapping(value = APIUriInfo.WEB_HOOK, produces = "text/plain;charset=UTF-8")
	public String logbackTest() {

		String messagePrefix = "Discord 및 Console LogBack Test 중 입니다! 출력 Error 단계를 확인 하겠습니다! \n";

		log.debug(messagePrefix + "Debug 단계 Log 출력입니다!");
		log.info(messagePrefix + "Info 단계 Log 출력입니다! ");
		log.trace(messagePrefix + "trace 단계 Log 출력 입니다!");
		log.warn(messagePrefix + "warning 단계 Log 출력 입니다!");
		log.error(messagePrefix + "error 단계 Log 출력 입니다!");

		return "Local 환경에서 해당 API 호출 시 IDE Console 확인해 주세요. DEV, Operation 환경에서 API 호출 시 Discord 확인 해 주세요.";
	}

	@ExecutionTimeCheck
	@GetMapping(value = "/test")
	public ResponseEntity<Void> testWebHook() {
		String npeValue = null;
		if (npeValue.equals("test")) {
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.badRequest().build();
	}

	/**
	 * <b>무중단 배포 시 Docker 정상 기동 여부 확인을 위한 Profile Method</b>
	 * @return 현재 기동중인 Application Profile 환경 이름
	 */
	@GetMapping(value = "/profile")
	public String profile() {
		return System.getProperty("spring.profiles.active");
	}
}
