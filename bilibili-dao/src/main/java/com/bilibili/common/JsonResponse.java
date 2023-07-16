package com.bilibili.common;
//json实体类
import lombok.Data;
@Data
public class JsonResponse<T> {
    private String code;
    private String msg;
    private T data;

    public JsonResponse(String code, String msg) {
        this.msg = msg;
        this.code = code;
    }

    public JsonResponse(T data) {
        this.data = data;
        this.msg = "成功";
        this.code = "0";
    }

    public static JsonResponse<String> success() {
        return new JsonResponse<>(null);
    }

    public static JsonResponse<String> success(String data) {
        return new JsonResponse<>(data);
    }


    //通用失败
    public static JsonResponse<String> fail(){
        return new JsonResponse<>("1","失败");
    }
    //定制失败
    public static JsonResponse<String> fail(String code,String msg){
        return new JsonResponse<>(code,msg);
    }
}