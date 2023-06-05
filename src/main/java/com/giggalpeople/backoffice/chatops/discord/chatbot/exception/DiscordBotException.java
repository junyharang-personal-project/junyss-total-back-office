package com.giggalpeople.backoffice.chatops.discord.chatbot.exception;

import com.giggalpeople.backoffice.common.enumtype.ErrorCode;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class DiscordBotException extends RuntimeException {

	private ErrorCode errorCode;

	public DiscordBotException(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	public DiscordBotException(String message) {
		super(message);
	}

	public DiscordBotException(ErrorCode errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	public DiscordBotException(String message, Throwable cause) {
		super(message, cause);
	}

	public DiscordBotException(Throwable cause) {
		super(cause);
	}

	public DiscordBotException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
