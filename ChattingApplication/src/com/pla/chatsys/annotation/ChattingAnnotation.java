package com.pla.chatsys.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;


@Target({ElementType.TYPE,ElementType.METHOD,ElementType.PARAMETER,ElementType.CONSTRUCTOR,ElementType.LOCAL_VARIABLE,ElementType.FIELD})
public @interface ChattingAnnotation {
	String feature();
	String type() default "property";
}
