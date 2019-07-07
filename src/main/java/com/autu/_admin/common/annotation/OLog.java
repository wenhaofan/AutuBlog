package com.autu._admin.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OLog {

  
	public OTypeEnum type() default OTypeEnum.ADD;
	
	public String moduleName() default "";
	
	
}
