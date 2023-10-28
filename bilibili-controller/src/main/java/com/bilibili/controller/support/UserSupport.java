package com.bilibili.controller.support;

import com.bilibili.exception.ConditionException;
import com.bilibili.util.TokenUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
public class UserSupport {

    /**
     * 抓取前端请求，验证token并返回userId
     * @return
     */
    public long getCurrentUserId(){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String token = request.getHeader("token");
        long userId = TokenUtil.verifyToken(token);
        if(userId < 0){
            throw new ConditionException("非法用户");
        }
        return userId;
    }
}
