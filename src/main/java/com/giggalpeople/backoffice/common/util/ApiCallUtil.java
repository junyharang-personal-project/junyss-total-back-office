package com.giggalpeople.backoffice.common.util;

import static com.giggalpeople.backoffice.common.enumtype.ErrorCode.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.giggalpeople.backoffice.chatops.discord.chatbot.exception.APICallException;
import com.giggalpeople.backoffice.chatops.discord.chatbot.exception.DiscordBotException;
import com.giggalpeople.backoffice.chatops.logback.appender.discord.object.JsonObject;
import com.giggalpeople.backoffice.common.env.Environment;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <h2><b>Discord Bot을 통해 API를 호출할 때, 필요한 기능을 모아 놓은 Class</b></h2>
 */
@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiCallUtil {

	/**
	 * <b>Discord Bot을 통해 API를 이용하여 정보를 얻어올 때 API 호출을 하여 값을 가져오기 위한 Method</b>
	 * @param url 호출하고자 하는 API URL
	 * @return API를 호출하여 얻어온 JSON Type의 조회 값
	 * @throws IOException API 호출 시 입력, 출력 관련 문제 발생 관련 Exception
	 */
	public static JSONObject callDiscordBotGetAPI(URL url) throws IOException {

		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		connection.setRequestMethod("GET");
		connection.setConnectTimeout(5000);
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setRequestProperty("Connection", "keep-alive");
		connection.setDoOutput(true);

		if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
			try (BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(connection.getInputStream()))) {
				JSONObject jsonObject = new JSONObject();
				String line;

				while ((line = bufferedReader.readLine()) != null) {
					jsonObject = new JSONObject(line);
				}
				return jsonObject;
			} catch (IOException ioException) {
				throw new DiscordBotException(API_RESPONSE_NOT_FOUND, API_RESPONSE_NOT_FOUND.getMessage());
			}
		}
		return new JSONObject();
	}

	/**
	 * <b>Http Method POST를 이용하여 API 호출을 위한 Method</b>
	 *
	 * @param suffixURL URL 중 접미사로 처리될 URL
	 * @param object    Request Body로 보낼 객체
	 * @throws IOException URL 처리 중 발생한 Exception
	 */

	public static void callDiscordBotPostAPI(StringBuilder suffixURL, Object object) throws IOException {
		StringBuilder defaultAPICallURLInfo = Environment.initPrefixAPIURL();
		URL defaultAPICallUrl = new URL(defaultAPICallURLInfo.append(suffixURL).toString());
		ObjectMapper objectMapper = new ObjectMapper();
		String requestBodyJsonData = "";

		if (object != null) {   /* DTO 객체를 JSON Type으로 바꾸기 전 DTO가 들어왔는지 확인하기 위한 분기문 */
			requestBodyJsonData = objectMapper.writeValueAsString(object);
		}

		HttpURLConnection connection = (HttpURLConnection)defaultAPICallUrl.openConnection();
		connection.setRequestMethod("POST");
		connection.setConnectTimeout(5000);
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setRequestProperty("Accept", "application/json");
		connection.setDoOutput(true);

		try (OutputStream outputStream = connection.getOutputStream()) {
			byte[] requestData = requestBodyJsonData.getBytes(StandardCharsets.UTF_8);
			outputStream.write(requestData);
			outputStream.flush();
		} catch (IOException ioException) {
			throw new APICallException(ioException.getMessage(), ioException);
		}

		if (connection.getResponseCode() == HttpURLConnection.HTTP_CREATED) {
			try (BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(connection.getInputStream()))) {
				JSONObject jsonObject = new JSONObject();
				String line;

				while ((line = bufferedReader.readLine()) != null) {
					jsonObject = new JSONObject(line);
				}
			} catch (IOException ioException) {
				log.error("통합 Back Office API에 Log 저장을 위한 처리 중 문제가 발생하였습니다." + ioException.getMessage());
			}
		}
		new JSONObject();
	}

	public static void callDiscordAppenderPostAPI(String urlString, JsonObject json) throws IOException {
		URL url = new URL(urlString);
		HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
		connection.addRequestProperty("Content-Type", "application/json");
		connection.addRequestProperty("User-Agent", "Java-DiscordWebhook-BY-Gelox_");
		connection.setDoOutput(true);
		connection.setRequestMethod("POST");

		try (OutputStream stream = connection.getOutputStream()) {
			stream.write(json.toString().getBytes());
			stream.flush();

			connection.getInputStream().close();
			connection.disconnect();

		} catch (IOException ioException) {
			throw new APICallException(ioException.getMessage(), ioException);
		}
	}
}
