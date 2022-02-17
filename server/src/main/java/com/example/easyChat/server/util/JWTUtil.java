package com.example.easyChat.server.util;

import com.example.easyChat.server.model.User;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
public class JWTUtil {
    private static String signJWT = "@easy_chat###";
    private static long times_expires = 1000*60;
    private static List<String> tokens = new ArrayList<>();

    public static String create_Token(User user) {
        JwtBuilder jwtBuilder = Jwts.builder();

        String token = jwtBuilder
                .setHeaderParam("typ","JWT")
                .setHeaderParam("alg","HS256")
                .claim("userId",user.getUId())
                .claim("username",user.getMobile())
                .setExpiration(new Date(System.currentTimeMillis()+times_expires))
                .signWith(SignatureAlgorithm.HS256,signJWT)
                .compact();//合成token
        tokens.add(token);
        return token;
    }

    /**
     * 检查token的真实性
     * @param token
     * @return
     */
    public static boolean checkToken(String token){
        if(token == null){
            log.info("传入的token为空");
            return false;
        }
        return tokens.contains(token);
    }
}
