package com.lyflexi.springbootpure.annotation;

import java.lang.annotation.*;

/**
 * 参数级别的注解
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ReqParamAnno {
    String value() default "";
}
