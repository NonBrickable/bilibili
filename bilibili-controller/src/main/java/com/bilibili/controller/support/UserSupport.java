package com.bilibili.controller.support;

import com.bilibili.exception.ConditionException;
import com.bilibili.util.TokenUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class UserSupport {

    //抓取前端请求，验证token并返回userId
    public long getCurrentUserId(){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        String token = requestAttributes.getRequest().getHeader("token");
        System.out.println(token);
        long userId = TokenUtil.verifyToken(token);
        System.out.println(userId);
        if(userId < 0){
            throw new ConditionException("非法用户");
        }
        return userId;
    }
}
