package com.giggalpeople.backoffice.chatops.logback.parse;

import com.fasterxml.jackson.databind.JsonNode;
import com.giggalpeople.backoffice.chatops.logback.parse.enumtype.ParseType;
import com.giggalpeople.backoffice.common.filter.CachedBodyRequestWrapper;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.nio.charset.StandardCharsets;

@Component
public class BodyParser extends Parser<JsonNode> {

    @Override
    JsonNode parseInfo(CachedBodyRequestWrapper cacheServletRequest) throws Exception {
        return mapper.readTree(StreamUtils.copyToString(cacheServletRequest.getInputStream(), StandardCharsets.UTF_8));
    }

    @Override
    public String parserKey() {
        return ParseType.BODY.name();
    }
}
