package com.giggalpeople.backoffice.chatops.logback.appender.discord.object;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * <h2><b>Discord Embed Field</b>/h2>
 */
@RequiredArgsConstructor
@Getter
public class Field {

	private final String name;
	private final String value;
	private final boolean inline;
}
