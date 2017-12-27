package com.iot.ota_web.util;

import java.security.Key;
import java.util.Date;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.UUID;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.iot.ota_web.bean.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

/**
 * token相关的生成，校验等
 * @author tangliang
 *
 */
public class TokenUtil {
	
	private static Logger logger = LogManager.getLogger(TokenUtil.class);
	
	//当前用户相关的token,存在本地内存。后期移到redis
	public static final ConcurrentHashMap<String, String> TOKEN_MAP = new ConcurrentHashMap<>();

	//secret
	private static final String SECRET="ZYWLW_CD@XXJSZX_ota.fda156246GGfaP!";
	
	//授权码过期消息
	public static final String TOKEN_EXPIRED_MESSAGE = "权限过期";
	//授权码非法消息
	public static final String TOKEN_INVALIDE_MESSAGE = "非法请求";
	
	/**
	 * 创建token
	 * @param payload	有效载荷
	 * @param ttlMillis	jwt过期时间	毫秒
	 * @param user		当前用户
	 * @return
	 */
	public static String createToken(JSONObject payload, long ttlMillis, User user){
		//The JWT signature algorithm we will be using to sign the token
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		 
		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);
		 
		//We will sign our JWT with our ApiKey secret
		byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET);
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
		 
		//Let's set the JWT Claims
		Claims claims = Jwts.claims().setSubject(user.getName());
		if (payload != null && !payload.isEmpty()) {
			for(Entry<String, Object> entry : payload.entrySet()) {
				claims.put(entry.getKey(), entry.getValue());
			}
		}
		String uuid = UUID.randomUUID().toString();
		JwtBuilder builder = Jwts.builder()
										.setClaims(claims)
										.setId(uuid)	//设置jwt的id,该值必须唯一
										.setIssuedAt(now)						//生成jwt的时间
		                                .setIssuer("ota_manager")				//设置jwt的发布者
		                                .signWith(signatureAlgorithm, signingKey);
		 
		//if it has been specified, let's add the expiration
		if (ttlMillis >= 0) {
		    long expMillis = nowMillis + ttlMillis;
		    Date exp = new Date(expMillis);
		    builder.setExpiration(exp);
		}
		 
		//Builds the JWT and serializes it to a compact, URL-safe string
		String jwt = builder.compact();
		TOKEN_MAP.put(uuid, jwt);
		return jwt;
	}
	
	/**
	 * 转换token
	 * @param jwt
	 */
	public static Claims parseJWT(String jwt, JSONObject result) {
		try {
			Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(jwt).getBody();
			return claims;
		} catch (ExpiredJwtException e) {
            // 在解析JWT字符串时，如果‘过期时间字段’已经早于当前时间，将会抛出ExpiredJwtException异常，说明本次请求已经失效
			//授权码过期时，从内存中将其删除
			String key = null;
			for(Entry<String, String> entry : TokenUtil.TOKEN_MAP.entrySet()){
				if (entry.getValue().equals(jwt)) {
					key = entry.getKey();
					break;
				}
			}
			if (key != null) {
				TokenUtil.TOKEN_MAP.remove(key);
			}
			result.put("code", "0001");
			result.put("message", TOKEN_EXPIRED_MESSAGE);
			return null;
        } catch (SignatureException e) {
            // 在解析JWT字符串时，如果密钥不正确，将会解析失败，抛出SignatureException异常，说明该JWT字符串是伪造的
        	result.put("code", "0001");
			result.put("message", TOKEN_INVALIDE_MESSAGE);
			return null;
        } catch (Exception e) {
			ExceptionUtil.printExceptionToLog(logger, e);
			throw e;
		}
		
	}
	
	public static void main(String[] args) {
		/*User user = new User();
		user.setName("tangliang");
		
		JSONObject payload = new JSONObject();
		payload.put("lastLogin", new Date());
		payload.put("userId", 1);
		String jwt = createToken(payload, 100000, user);
		System.out.println(jwt);*/
		//parseJWT("aa.bbb");
	}
	
}
