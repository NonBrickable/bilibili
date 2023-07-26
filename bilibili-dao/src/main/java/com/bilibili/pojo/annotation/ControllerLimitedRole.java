package com.bilibili.pojo.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 限制调用api
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
@Component
public @interface ControllerLimitedRole {
    /**
     * 限制角色：例如Lv0编码的角色不能调用
     *
     * @return
     */
    String[] limitedRoleCodeList() default {};
}
