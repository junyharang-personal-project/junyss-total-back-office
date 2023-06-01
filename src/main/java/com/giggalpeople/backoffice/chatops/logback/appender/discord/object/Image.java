package com.giggalpeople.backoffice.chatops.logback.appender.discord.object;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * <h2><b>Discord Image Object</b></h2>
 */
@RequiredArgsConstructor
@Getter
public class Image {
    private final String url;
}
