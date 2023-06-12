package com.giggalpeople.backoffice.common.exception;

import static com.giggalpeople.backoffice.common.enumtype.ErrorCode.API_PROCESSING_TAKES_LONG;
import static com.giggalpeople.backoffice.common.enumtype.ErrorCode.BAD_REQUEST;
import static com.giggalpeople.backoffice.common.enumtype.ErrorCode.INTERNAL_SERVER_ERROR;
import static com.giggalpeople.backoffice.common.enumtype.ErrorCode.PARAMETER_INVALID;
import static com.giggalpeople.backoffice.common.enumtype.ErrorCode.PARAMETER_LONG;
import static com.giggalpeople.backoffice.common.enumtype.ErrorCode.PARAMETER_NULL;
import static com.giggalpeople.backoffice.common.enumtype.ErrorCode.PARAMETER_SHORT;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import net.dv8tion.jda.api.exceptions.InvalidTokenException;

import com.giggalpeople.backoffice.api.crew.exception.CrewException;
import com.giggalpeople.backoffice.api.crew.exception.SuggestException;
import com.giggalpeople.backoffice.api.record.exception.ErrorRecordException;
import com.giggalpeople.backoffice.chatops.discord.chatbot.exception.APICallException;
import com.giggalpeople.backoffice.chatops.discord.chatbot.exception.DiscordBotException;
import com.giggalpeople.backoffice.chatops.logback.appender.exception.ErrorLogAppenderException;
import com.giggalpeople.backoffice.common.constant.DefaultResponse;
import com.giggalpeople.backoffice.common.enumtype.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalAPIExceptionAdvisor {
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({IllegalArgumentException.class, HttpMessageNotReadableException.class, IOException.class,
		IllegalStateException.class})
	public DefaultResponse<Void> handleIllegalArgumentException(Exception exception) {
		log.error("{}", exception);
		return DefaultResponse.error(ErrorCode.PARAMETER_INVALID, ErrorCode.PARAMETER_INVALID.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(DiscordBotException.class)
	public DefaultResponse<Void> handleDiscordBotBadRequestException(Exception exception) {
		log.warn("{}", exception);
		return DefaultResponse.error(BAD_REQUEST, BAD_REQUEST.getMessage());
	}

	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(InvalidTokenException.class)
	public DefaultResponse<Void> handleInvalidTokenException(Exception exception) {
		log.warn("{}", exception);
		return DefaultResponse.error(ErrorCode.INVALID_TOKEN, ErrorCode.INVALID_TOKEN.getMessage());
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler({NoSuchElementException.class, SuggestException.class, CrewException.class,
		ErrorRecordException.class})
	public DefaultResponse<Void> handleSuggestException(Exception exception) {
		log.warn("{}", exception);
		return DefaultResponse.error(ErrorCode.NOT_FOUND, ErrorCode.NOT_FOUND.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(BindException.class)
	public DefaultResponse<Void> handleMethodArgumentNotValidException(BindException exception) {
		try {
			BindingResult bindingResult = exception.getBindingResult();
			ErrorCode errorCode = INTERNAL_SERVER_ERROR;
			String errorField = "";

			if (bindingResult.hasErrors()) {
				String bindResultCode = Objects.requireNonNull(
					Objects.requireNonNull(bindingResult.getFieldError()).getCode());
				errorField = bindingResult.getFieldError().getField();

				switch (bindResultCode) {
					case "NotNull":
						errorCode = PARAMETER_NULL;
						break;
					case "Min":
						errorCode = PARAMETER_SHORT;
						break;
					case "Max":
						errorCode = PARAMETER_LONG;
						break;
					default:
						errorCode = PARAMETER_INVALID;
				}
			}

			log.error("{}", exception);
			return DefaultResponse.error(errorCode, errorField);
		} catch (NullPointerException nullPointerException) {
			throw new NullPointerException("GlobalAPIException에서 BAD_REQUEST로 인한 BindException 처리 간 NPE가 발생하였습니다.");
		}
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(APICallException.class)
	public DefaultResponse<Void> handleAPICallException(Exception exception) {
		log.error("{}", exception);
		return DefaultResponse.error(ErrorCode.DISCORD_BOT_API_CALL_FAILURE,
			ErrorCode.DISCORD_BOT_API_CALL_FAILURE.getMessage());
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler({TimeTraceException.class})
	public DefaultResponse<Void> handleTimeTraceException(Exception exception) {
		log.warn("{}", exception);
		return DefaultResponse.error(API_PROCESSING_TAKES_LONG, API_PROCESSING_TAKES_LONG.getMessage());
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler({NullPointerException.class, Exception.class, ErrorLogAppenderException.class})
	public DefaultResponse<Void> handleInternalServerException(Exception exception) {
		log.error("{}", exception);
		return DefaultResponse.error(INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR.getMessage());
	}
}
