package com.bilibili.pojo.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 限制调用api
 */
//用于标注这个注解放在什么地方，类上，方法上，构造器上
@Target(ElementType.METHOD)
//用于说明这个注解的生命周期
@Retention(RetentionPolicy.RUNTIME)
//将注解信息添加到文本中
@Documented
@Component
public @interface ControllerLimitedRole {
    /**
     * 限制调用角色的唯一编码列表
     *
     * @return
     */
    String[] limitedRoleCodeList() default {};
}
