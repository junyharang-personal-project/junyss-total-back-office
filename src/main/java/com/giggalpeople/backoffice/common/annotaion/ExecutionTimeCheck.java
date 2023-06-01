package com.giggalpeople.backoffice.common.annotaion;

import org.springframework.context.annotation.Profile;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <h2><b>@Target 으로 Method에서 사용하겠다고 정의하고, @retention으로 해당 Annotation이 Runtime까지 유지되도록 설정하여 시간 측정을 원하는 Method에 사용하기 위한 Annotation </b></h2>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExecutionTimeCheck {}
