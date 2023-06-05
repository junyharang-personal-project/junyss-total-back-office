package com.giggalpeople.backoffice.chatops.logback.appender.discord.object;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * <h2><b>Discord Embed Footer Object</b></h2>
 */
@RequiredArgsConstructor
@Getter
public class Footer {
	private final String text;
	private final String iconUrl;
}
