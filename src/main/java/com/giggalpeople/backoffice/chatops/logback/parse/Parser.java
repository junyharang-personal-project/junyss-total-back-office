package com.giggalpeople.backoffice.chatops.logback.parse;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.giggalpeople.backoffice.common.filter.CachedBodyRequestWrapper;

public abstract class Parser<T> {

	@Autowired
	public ObjectMapper mapper;

	public String parse(CachedBodyRequestWrapper cacheServletRequest) throws Exception {
		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(parseInfo(cacheServletRequest));
	}

	abstract T parseInfo(CachedBodyRequestWrapper cacheServletRequest) throws Exception;

	public abstract String parserKey();
}
