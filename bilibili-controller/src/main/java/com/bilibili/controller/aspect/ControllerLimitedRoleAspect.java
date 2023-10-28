package com.bilibili.controller.aspect;

import com.bilibili.controller.support.UserSupport;
import com.bilibili.exception.ConditionException;
import com.bilibili.pojo.annotation.ControllerLimitedRole;
import com.bilibili.pojo.auth.UserRole;
import com.bilibili.service.UserRoleService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.junit.jupiter.api.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Order(1)
@Component
@Aspect
public class ControllerLimitedRoleAspect {
    @Autowired
    private UserSupport userSupport;
    @Autowired
    private UserRoleService userRoleService;

    //切入点，指定注解标注的位置
    @Pointcut("@annotation(com.bilibili.pojo.annotation.ControllerLimitedRole)")
    public void check() {

    }

    //@Before 前置通知，在方法执行之前
    // 指定方法，获取角色编码
    @Before("check() && @annotation(controllerLimitedRole)")
    public void doBefore(JoinPoint joinPoint, ControllerLimitedRole controllerLimitedRole) {
        Long userId = userSupport.getCurrentUserId();
        //用户的角色集合
        List<UserRole> userRoleList = userRoleService.getUserRole(userId);
        //限制使用的角色集合
        String[] limitedRoleCodeList = controllerLimitedRole.limitedRoleCodeList();
        Set limitedRoleCodeSet = Arrays.stream(limitedRoleCodeList).collect(Collectors.toSet());
        Set roleCodeSet = userRoleList.stream().map(UserRole::getRoleCode).collect(Collectors.toSet());
        //角色编码取交集
        roleCodeSet.retainAll(limitedRoleCodeSet);
        if(roleCodeSet.size()>0){
            throw new ConditionException("权限不足");
        }
    }
}
