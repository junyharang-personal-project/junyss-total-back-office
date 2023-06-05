package com.giggalpeople.backoffice.chatops.logback.appender.discord.object;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * <h2><b>Discord Embed Author</b>/h2>
 */
@RequiredArgsConstructor
@Getter
public class Author {
	private final String name;
	private final String url;
	private final String iconUrl;
}
