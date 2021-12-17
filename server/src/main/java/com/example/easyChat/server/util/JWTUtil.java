package com.example.easyChat.server.util;

import com.example.easyChat.server.model.User;
import io.jsonwebtoken.*;

import java.util.Date;

public class JWTUtil {
    private static String signJWT = "@easy_chat###";
    private static long times_expires = 1000*60;

    public static String create_Token(User user) {
        JwtBuilder jwtBuilder = Jwts.builder();

        String token = jwtBuilder
                .setHeaderParam("typ","JWT")//头部  可以不写
                .setHeaderParam("alg","HS256")
                .claim("userId",user.getId())//数据
                .claim("username",user.getMobile())
                .setExpiration(new Date(System.currentTimeMillis()+times_expires))//有效期
                .signWith(SignatureAlgorithm.HS256,signJWT)//加密
                .compact();//合成token
        return token;
    }

    //检查token
    public static boolean checkToken(String token){
        if(token == null){
            return false;
        }
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(signJWT).parseClaimsJws(token);//获得token中的数据，没有就说明过期或者假的
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
