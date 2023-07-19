package com.bilibili.common;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor
//分页结果类
public class PageResult<T> {
    //总条数
    private Integer total;
    //当前页的数据
    private List<T> list;
}
