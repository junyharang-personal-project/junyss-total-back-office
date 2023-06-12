package com.giggalpeople.backoffice.chatops.logback.appender.discord.append;

import java.awt.Color;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;

import com.giggalpeople.backoffice.chatops.logback.appender.database.append.DataBaseAppender;
import com.giggalpeople.backoffice.chatops.logback.appender.discord.config.DiscordWebHook;
import com.giggalpeople.backoffice.chatops.logback.appender.discord.object.EmbedObject;
import com.giggalpeople.backoffice.chatops.logback.appender.exception.ErrorLogAppenderException;
import com.giggalpeople.backoffice.chatops.logback.appender.util.MDCUtil;
import com.giggalpeople.backoffice.common.entity.ServerInfo;
import com.giggalpeople.backoffice.common.env.Environment;
import com.giggalpeople.backoffice.common.util.StringUtil;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import io.micrometer.core.instrument.util.StringEscapeUtils;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * <h2><b>발생된 Error의 Level이 WARN 혹은 Error인 경우 이를 Discord로 보내기 위한 Class</b></h2>
 */

@Setter
@Slf4j
public class DiscordAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {

	private String discordWebhookURL;

	private static Color getLevelColor(ILoggingEvent eventObject) {
		String level = eventObject.getLevel().levelStr;
		if (level.equals("WARN")) {
			return Color.yellow;
		} else if (level.equals("ERROR")) {
			return Color.red;
		}

		return Color.blue;
	}

	@Override
	protected void append(ILoggingEvent eventObject) {
		DiscordWebHook discordWebhook = new DiscordWebHook(discordWebhookURL);
		DataBaseAppender dataBaseAppender = new DataBaseAppender();
		ServerInfo serverInfo;
		Map<String, String> mdcPropertyMap = eventObject.getMDCPropertyMap();
		Color messageColor = getLevelColor(eventObject);

		String level = eventObject.getLevel().levelStr;
		String exceptionBrief = "";
		String exceptionDetail = "";
		IThrowableProxy throwable = eventObject.getThrowableProxy();

		if (throwable != null) {
			exceptionBrief = throwable.getClassName() + ": " + throwable.getMessage();
		}

		if (exceptionBrief.equals("")) {
			exceptionBrief = "EXCEPTION 정보가 남지 않았습니다.";
		}

		serverInfo = Environment.getServerInfo();

		discordWebhook.addEmbed(new EmbedObject()
			.setTitle("[" + level + " - 문제 간략 내용]")
			.setColor(messageColor)
			.setDescription(exceptionBrief)
			.addField("[" + "Exception Level" + "]",
				StringEscapeUtils.escapeJson(level),
				true)
			.addField("[문제 발생 시각]",
				LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
				false)
			.addField("[" + MDCUtil.SERVER_APPLICATION_NAME_MDC + "]",
				StringEscapeUtils.escapeJson(Objects.requireNonNull(serverInfo).getServerName()),
				false)
			.addField("[" + MDCUtil.SERVER_VM_INFO_MDC + "]",
				StringEscapeUtils.escapeJson(Objects.requireNonNull(serverInfo).getVmInfo()),
				false)
			.addField("[" + MDCUtil.SERVER_OS_INFO_MDC + "]",
				StringEscapeUtils.escapeJson(Objects.requireNonNull(serverInfo).getOsInfo()),
				false)
			.addField("[" + MDCUtil.SERVER_IP_MDC + "]",
				StringEscapeUtils.escapeJson(Objects.requireNonNull(serverInfo).getServerIP()),
				false)
			.addField("[" + MDCUtil.SERVER_ENVIRONMNET_MDC + "]",
				StringEscapeUtils.escapeJson(Objects.requireNonNull(serverInfo).getServerEnvironment()),
				false)
			.addField(
				"[" + MDCUtil.REQUEST_URI_MDC + "]",
				StringEscapeUtils.escapeJson(mdcPropertyMap.get(MDCUtil.REQUEST_URI_MDC)),
				false)
			.addField(
				"[" + MDCUtil.USER_IP_MDC + "]",
				StringEscapeUtils.escapeJson(mdcPropertyMap.get(MDCUtil.USER_IP_MDC)),
				false)
			.addField("[" + MDCUtil.USER_AGENT_DETAIL_MDC + "]",
				StringEscapeUtils.escapeJson(mdcPropertyMap.get(MDCUtil.USER_AGENT_DETAIL_MDC)),
				false)
			.addField(
				"[" + MDCUtil.USER_LOCATION_MDC + "]",
				StringEscapeUtils.escapeJson(
					mdcPropertyMap.get(MDCUtil.USER_LOCATION_MDC).replaceAll("[\\{\\{\\}]", "")),
				false)
			.addField(
				"[" + MDCUtil.HEADER_MAP_MDC + "]",
				StringEscapeUtils.escapeJson(mdcPropertyMap.get(MDCUtil.HEADER_MAP_MDC).replaceAll("[\\{\\{\\}]", "")),
				true)
			.addField(
				"[" + MDCUtil.USER_REQUEST_COOKIES + "]",
				StringEscapeUtils.escapeJson(
					mdcPropertyMap.get(MDCUtil.USER_REQUEST_COOKIES).replaceAll("[\\{\\{\\}]", "")),
				false)
			.addField(
				"[" + MDCUtil.PARAMETER_MAP_MDC + "]",
				StringEscapeUtils.escapeJson(
					mdcPropertyMap.get(MDCUtil.PARAMETER_MAP_MDC).replaceAll("[\\{\\{\\}]", "")),
				false)
			.addField("[" + MDCUtil.BODY_MDC + "]",
				StringEscapeUtils.escapeJson(StringUtil.translateEscapes(mdcPropertyMap.get(MDCUtil.BODY_MDC))),
				false)
		);

		if (throwable != null) {
			exceptionDetail = ThrowableProxyUtil.asString(throwable);
			String exception = exceptionDetail.substring(0, 4000);
			discordWebhook.addEmbed(
				new EmbedObject()
					.setTitle("[Exception 상세 내용]")
					.setColor(messageColor)
					.setDescription(StringEscapeUtils.escapeJson(exception))
			);
		}

		try {
			discordWebhook.execute();
			dataBaseAppender.inDBInsert(level, exceptionBrief, exceptionDetail, serverInfo, mdcPropertyMap);
		} catch (IOException ioException) {
			throw new ErrorLogAppenderException(ioException.getMessage(), ioException);
		}
	}
}
