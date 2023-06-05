package com.giggalpeople.backoffice.chatops.logback.appender.discord.object;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * <h2><b>Discord Embed Thumbnail Object</b>/h2>
 */
@RequiredArgsConstructor
@Getter
public class Thumbnail {
	private final String url;
}
