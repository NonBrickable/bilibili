package com.bilibili.controller.aspect;

import com.bilibili.common.AuthRoleConstant;
import com.bilibili.controller.support.UserSupport;
import com.bilibili.exception.ConditionException;
import com.bilibili.pojo.UserMoments;
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
public class DataLimitedAspect {
    @Autowired
    private UserSupport userSupport;
    @Autowired
    private UserRoleService userRoleService;

    //切点，指定注解
    @Pointcut("@annotation(com.bilibili.pojo.annotation.DataLimited)")
    public void check() {

    }

    @Before("check()")
    public void doBefore(JoinPoint joinPoint) {
        Long userId = userSupport.getCurrentUserId();
        List<UserRole> userRoleList = userRoleService.getUserRole(userId);
        Set roleCodeSet = userRoleList.stream().map(UserRole::getRoleCode).collect(Collectors.toSet());
        Object[] args = joinPoint.getArgs();
        for (Object arg : args){
            if(arg instanceof UserMoments){
                UserMoments userMoments = (UserMoments)arg;
                String type = userMoments.getType();
                //如果权限等级不够，不能访问某些数据
                if(roleCodeSet.contains(AuthRoleConstant.ROLE_LV0) && !"0".equals(type)){
                    throw new ConditionException("参数异常");
                }
            }
        }

    }
}
