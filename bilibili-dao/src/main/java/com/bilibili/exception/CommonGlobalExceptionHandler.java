package com.bilibili.exception;

import com.bilibili.exception.ConditionException;
import com.bilibili.common.JsonResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

//全局异常处理,返回数据错误码给前端

//@ControllerAdvice注解是Spring MVC中一个全局的异常处理注解。它可以应用到类上，
// 用来处理控制器中所有带有@RequestMapping注解的方法抛出的异常，
// 这样可以避免在每个控制器中都进行重复的异常处理。
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)//处理等级最高
public class CommonGlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public JsonResponse<String> commonExceptionHandler(HttpServletRequest request,Exception e){
        String errorMsg=e.getMessage();
        if(e instanceof ConditionException){
            String errorCode=((ConditionException)e).getCode();
            return new JsonResponse<>(errorCode,errorMsg);
        }else{
            return new JsonResponse<>("500",errorMsg);
        }
    }
}
