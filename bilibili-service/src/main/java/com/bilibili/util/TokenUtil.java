package com.bilibili.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bilibili.exception.ConditionException;

import java.util.Calendar;
import java.util.Date;


//获取用户令牌
public class TokenUtil {
    private  static final  String ISSUER="签发者";
    public static String generateToken(Long userId) throws  Exception{
        Algorithm algorithm=Algorithm.RSA256(RSAUtil.getPublicKey(),RSAUtil.getPrivateKey());
        Calendar calender=Calendar.getInstance();//设置过期时间
        calender.setTime(new Date());
        calender.add(Calendar.HOUR,1);//设置过期时间
        return JWT.create().withKeyId(String.valueOf(userId))
                .withIssuer(ISSUER)
                .withExpiresAt(calender.getTime())
                .sign(algorithm);
    }
    //验证token
    public static Long verifyToken(String token) {
        try {
            Algorithm algorithm=Algorithm.RSA256(RSAUtil.getPublicKey(),RSAUtil.getPrivateKey());
            JWTVerifier verifier=JWT.require(algorithm).build();
            DecodedJWT jwt= verifier.verify(token);
            String userid= jwt.getKeyId();
            return Long.valueOf(userid);
        }catch (TokenExpiredException e){
              throw  new ConditionException("555","Token过期！");
        }catch (Exception e){
            throw new ConditionException("非法用户Token");
        }
    }

    public static String generateRefreshToken(Long userId) throws  Exception{
        Algorithm algorithm=Algorithm.RSA256(RSAUtil.getPublicKey(),RSAUtil.getPrivateKey());
        Calendar calender=Calendar.getInstance();//设置过期时间
        calender.setTime(new Date());
        calender.add(Calendar.DAY_OF_MONTH,7);
        return JWT.create().withKeyId(String.valueOf(userId))
                .withIssuer(ISSUER)
                .withExpiresAt(calender.getTime())
                .sign(algorithm);
    }
}
