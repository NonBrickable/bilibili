package com.bilibili.pojo.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 限制数据权限
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
@Component
public @interface DataLimited {

}
