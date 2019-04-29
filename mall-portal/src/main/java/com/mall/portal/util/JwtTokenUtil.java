package com.mall.portal.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;

/**
 * JwtToken生成的工具类
 * JWT token的格式：header.payload.signature
 * header的格式（算法、token的类型）：
 * {"alg": "HS512","typ": "JWT"}
 * payload的格式（用户名、创建时间、生成时间）：
 * {"sub":"wang","created":1489079981393,"exp":1489684781}
 * signature的生成算法：
 * HMACSHA512(base64UrlEncode(header) + "." +base64UrlEncode(payload),secret)
 * Created by macro on 2018/4/26.
 */
@Component
public class JwtTokenUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenUtil.class);
    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_CREATED = "created";
    @Value("${jwt.secret}")
    private String secret; //= "mySecret";
    @Value("${jwt.expiration}")
    private Long expiration;


    /**
     * 根据负责生成JWT的token
     */
    public String generateToken(Map paramMap) {
//        Map<String, Object> claims = new HashMap<>();
//        claims.put(CLAIM_KEY_USERNAME, paramMap.get("username").toString());
//        claims.put(CLAIM_KEY_CREATED, new Date());

        Algorithm algorithm = Algorithm.HMAC256(secret);

        Map<String, Object> map = new HashMap<>();
        Date nowDate = new Date();
        Date expireDate = getAfterDate(nowDate,0,0,0,2,0,0);//2小时过期
        map.put("alg", "HS256");
        map.put("typ", "JWT");
        String token = JWT.create()
                /*设置头部信息 Header*/
                .withHeader(map)
                /*设置 载荷 Payload*/
                .withClaim("loginName", paramMap.get("username").toString())
                .withIssuer("SERVICE")//签名是有谁生成 例如 服务器
                .withSubject("this is my token")//签名的主题
                //.withNotBefore(new Date())//定义在什么时间之前，该jwt都是不可用的.
                .withAudience("APP")//签名的观众 也可以理解谁接受签名的
                .withIssuedAt(nowDate) //生成签名的时间
                .withExpiresAt(expireDate)//签名过期的时间
                /*签名 Signature */
                .sign(algorithm);
        return token;
//        return JWT.builder()
//                .setClaims(claims)
//                .setExpiration(generateExpirationDate())
//                .signWith(SignatureAlgorithm.HS512, secret)
//                .compact();
    }

    /**
     * 返回一定时间后的日期
     * @param date 开始计时的时间
     * @param year 增加的年
     * @param month 增加的月
     * @param day 增加的日
     * @param hour 增加的小时
     * @param minute 增加的分钟
     * @param second 增加的秒
     * @return
     */
    public  Date getAfterDate(Date date, int year, int month, int day, int hour, int minute, int second){
        if(date == null){
            date = new Date();
        }

        Calendar cal = new GregorianCalendar();

        cal.setTime(date);
        if(year != 0){
            cal.add(Calendar.YEAR, year);
        }
        if(month != 0){
            cal.add(Calendar.MONTH, month);
        }
        if(day != 0){
            cal.add(Calendar.DATE, day);
        }
        if(hour != 0){
            cal.add(Calendar.HOUR_OF_DAY, hour);
        }
        if(minute != 0){
            cal.add(Calendar.MINUTE, minute);
        }
        if(second != 0){
            cal.add(Calendar.SECOND, second);
        }
        return cal.getTime();
    }

    public void verifyToken(String token) {
        try {
            System.out.println(token);
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("SERVICE")
                    .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(token);
            String subject = jwt.getSubject();
            Map<String, Claim> claims = jwt.getClaims();
            Claim claim = claims.get("loginName");
            System.out.println(claim.asString());
            List<String> audience = jwt.getAudience();
            System.out.println(subject);
            System.out.println(audience.get(0));
        } catch (JWTVerificationException exception){
            exception.printStackTrace();
        }
    }


    public static void main(String[] args) {
        JwtTokenUtil demo = new JwtTokenUtil();
        Map<String,String> paramMap = new HashMap();
        paramMap.put("username","张三");
        //String createToken = demo.createToken();
        String createTokenWithClaim = demo.generateToken(paramMap);
        demo.verifyToken(createTokenWithClaim);
    }

    /**
     * 从token中获取JWT中的负载
     */
//    private Claims getClaimsFromToken(String token) {
//        Claims claims = null;
//        try {
//            claims = Jwts.parser()
//                    .setSigningKey(secret)
//                    .parseClaimsJws(token)
//                    .getBody();
//        } catch (Exception e) {
//            LOGGER.info("JWT格式验证失败:{}",token);
//        }
//        return claims;
//    }

    /**
     * 生成token的过期时间
     */
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    /**
     * 从token中获取登录用户名
     */
//    public String getUserNameFromToken(String token) {
//        String username;
//        try {
//            Claims claims = getClaimsFromToken(token);
//            username =  claims.getSubject();
//        } catch (Exception e) {
//            username = null;
//        }
//        return username;
//    }

    /**
     * 验证token是否还有效
     *
     * @param token       客户端传入的token
     * @param userDetails 从数据库中查询出来的用户信息
     */
//    public boolean validateToken(String token, UserDetails userDetails) {
//        String username = getUserNameFromToken(token);
//        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
//    }

    /**
     * 判断token是否已经失效
     */
//    private boolean isTokenExpired(String token) {
//        Date expiredDate = getExpiredDateFromToken(token);
//        return expiredDate.before(new Date());
//    }

    /**
     * 从token中获取过期时间
     */
//    private Date getExpiredDateFromToken(String token) {
//        Claims claims = getClaimsFromToken(token);
//        return claims.getExpiration();
//    }

    /**
     * 根据用户信息生成token
     */
//    public String generateToken(Map paramMap) {
//        Map<String, Object> claims = new HashMap<>();
//        claims.put(CLAIM_KEY_USERNAME, paramMap.get("username").toString());
//        claims.put(CLAIM_KEY_CREATED, new Date());
//        return generateToken(claims);
//    }

    /**
     * 判断token是否可以被刷新
     */
//    public boolean canRefresh(String token) {
//        return !isTokenExpired(token);
//    }

    /**
     * 刷新token
     */
//    public String refreshToken(String token) {
//        Claims claims = getClaimsFromToken(token);
//        claims.put(CLAIM_KEY_CREATED, new Date());
//        return generateToken(claims);
//    }
}
