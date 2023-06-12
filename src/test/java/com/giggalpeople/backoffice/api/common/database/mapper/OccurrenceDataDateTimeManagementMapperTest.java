package com.giggalpeople.backoffice.api.common.database.mapper;

import static org.assertj.core.api.Assertions.*;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.giggalpeople.backoffice.api.common.model.dto.request.DataCreatedDateTimeRequestDto;
import com.giggalpeople.backoffice.api.common.model.vo.DataCreatedDateTimeVo;

// JUnit 5 사용 시 사용, MyBatisTest 2.0.1 Version 이상에서 생략 가능
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@MybatisTest
// 실제 데이터베이스에 연결 시 필요한 어노테이션
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
// Test 완료 뒤 Rollback을 위해 명시
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:/init/database/schema.sql")        // Data Base 초기화를 위한 설정
class OccurrenceDataDateTimeManagementMapperTest {

	@Autowired
	OccurrenceDataDateTimeManagementMapper occurrenceDataDateTimeManagementMapper;

	DataCreatedDateTimeVo dataCreatedDateTimeVO;

	@BeforeEach
	void beforeTestSetup() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String[] splitNowDateTime = simpleDateFormat.format(new Date(System.currentTimeMillis())).split(" ");

		dataCreatedDateTimeVO = DataCreatedDateTimeVo.toVO(
			DataCreatedDateTimeRequestDto.builder()
				.createdDate(splitNowDateTime[0])
				.createdTime(splitNowDateTime[1])
				.build());
	}

	@Test
	@DisplayName("공통 발생 시각 저장 테스트")
	@Order(0)
	void save() {
		//given
		// when
		Long saveID = occurrenceDataDateTimeManagementMapper.save(dataCreatedDateTimeVO);

		// then
		assertThat(saveID).isEqualTo(dataCreatedDateTimeVO.getDataCreatedDateTimeID());
	}

	@Test
	@DisplayName("공통 발생 시각 저장 값 확인 테스트")
	@Order(1)
	void findByOccurrenceInfoDateTime() {
		//given
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String[] splitNowDateTime = simpleDateFormat.format(new Date(System.currentTimeMillis())).split(" ");

		occurrenceDataDateTimeManagementMapper.save(DataCreatedDateTimeVo.toVO(
			DataCreatedDateTimeRequestDto.builder()
				.createdDate(splitNowDateTime[0])
				.createdTime(splitNowDateTime[1])
				.build()));

		// when
		Long saveID = occurrenceDataDateTimeManagementMapper.findByOccurrenceInfoDateTime(splitNowDateTime[0],
			splitNowDateTime[1]);

		// then
		assertThat(saveID).isEqualTo(1L);
	}
}