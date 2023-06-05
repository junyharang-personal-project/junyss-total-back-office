package com.giggalpeople.backoffice.api.common.constant;

/**
 * <h2><b>API 주소를 관리하기 위한 Interface</b></h2>
 */
public interface APIUriInfo {

	String LOCAL_ENV_DEFAULT_URL = "http://localhost:9999";

	String DEV_ENV_DEFAULT_URL = "http://localhost:9998";

	String PROD_ENV_DEFAULT_URL = "http://localhost:8080";

	String API_PREFIX_URN = "/api";

	String API_CALLER_DISCORD_BOT = "/discord/bot";

	String API_VERSION = "/v1";

	String TEST = "/test";

	String WEB_HOOK = "/web-hook";

	// CREW 지원 및 합류자 관련
	String API_CREW = "/crews";

	String API_SUGGEST = "/suggests";

	String CONNECTED_USER = "/connected/user";

	String LOG = "/logs";
}
