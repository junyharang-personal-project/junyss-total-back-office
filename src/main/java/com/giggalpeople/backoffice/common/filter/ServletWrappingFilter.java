package com.giggalpeople.backoffice.common.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jetbrains.annotations.NotNull;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.extern.slf4j.Slf4j;

/**
 * <h2><b>HttpServletRequest 객체를 CachedBodyRequestWrapper로 변환해주는 Filter</b></h2>
 */
@Slf4j
public class ServletWrappingFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		CachedBodyRequestWrapper cachedBodyRequestWrapper = new CachedBodyRequestWrapper(request);
		log.info("url : " + request.getRequestURI());
		filterChain.doFilter(cachedBodyRequestWrapper, response);
	}
}
