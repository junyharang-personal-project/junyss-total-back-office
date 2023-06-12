package com.giggalpeople.backoffice.api.common.database.dao.impl;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.giggalpeople.backoffice.api.common.database.dao.OccurrenceDataDateTimeManagementDao;
import com.giggalpeople.backoffice.api.common.model.dto.request.DataCreatedDateTimeRequestDto;
import com.giggalpeople.backoffice.api.common.model.vo.DataCreatedDateTimeVo;

// JUnit 5 사용 시 사용, MyBatisTest 2.0.1 Version 이상에서 생략 가능
@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OccurrenceDataDateTimeManagementDaoImplTest {

	@Autowired
	OccurrenceDataDateTimeManagementDao occurrenceDataDateTimeManagementDao;

	DataCreatedDateTimeVo dataCreatedDateTimeVo;

	@BeforeEach
	@Transactional
	void beforeTestSetup() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String[] splitNowDateTime = simpleDateFormat.format(new Date(System.currentTimeMillis())).split(" ");

		dataCreatedDateTimeVo = DataCreatedDateTimeVo.toVO(
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
		Long saveID = occurrenceDataDateTimeManagementDao.save(dataCreatedDateTimeVo);

		// then
		assertThat(saveID).isEqualTo(1L);
	}

	@Test
	@DisplayName("공통 발생 시각 저장 값 확인 테스트")
	@Order(1)
	void findByOccurrenceInfoDateTime() {
		//given
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String[] splitNowDateTime = simpleDateFormat.format(new Date(System.currentTimeMillis())).split(" ");

		occurrenceDataDateTimeManagementDao.save(DataCreatedDateTimeVo.toVO(
			DataCreatedDateTimeRequestDto.builder()
				.createdDate(splitNowDateTime[0])
				.createdTime(splitNowDateTime[1])
				.build()));

		// when
		Long saveID = occurrenceDataDateTimeManagementDao.findByOccurrenceInfoDateTime(splitNowDateTime[0],
			splitNowDateTime[1]);

		// then
		assertThat(saveID).isEqualTo(2L);
	}
}