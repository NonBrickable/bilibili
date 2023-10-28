package com.bilibili.exception;

import lombok.Data;

//异常实体类，补充RuntimeException中没有的内容，可以自定义异常返回内容
@Data
public class ConditionException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private String code;

    public ConditionException(String code, String name) {
        super(name);
        this.code = code;
    }

    public ConditionException(String name) {
        super(name);
        code = "500";
    }

}
