package com.giggalpeople.backoffice.common.database;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * <h2><b>공통으로 사용될 Data Base 관리를 위한 Mapper</b></h2>
 */

@Mapper
@Repository
public interface DataBaseManagerMapper {

	/**
	 * <b>DAO, Mapper Test 시 JPA deleteAll과 같이 Table 초기화를 위한 Method</b>
	 * <b>Repository(DAO, Mapper) TEST Code에서 Table 초기화시만 사용해 주세요!</b>
	 * @param tableName Test Code에서 Table PK 값 초기화를 위해 해당 Table 이름 전달
	 */
	@Update("ALTER TABLE ${tableName} AUTO_INCREMENT = 1")
	void initializeAutoIncrement(@Param("tableName") String tableName);
}
