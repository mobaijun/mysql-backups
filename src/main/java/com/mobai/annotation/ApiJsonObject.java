package com.mobai.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author mobai
 */
@Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiJsonObject {

    /**
     * 对象属性值
     *
     * @return ApiJsonProperty
     */
    ApiJsonProperty[] value();

    /**
     * 对象名称
     *
     * @return String
     */
    String name();
}
