package com.playtube.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class FreshTokenDetail {
    private Long id;
    private String refreshToken;
    private Long userId;
    private Date createTime;
    private Date updateTime;
}
