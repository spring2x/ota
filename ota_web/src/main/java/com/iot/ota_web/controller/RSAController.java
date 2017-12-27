package com.iot.ota_web.controller;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.binary.Hex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.iot.ota_web.util.ExceptionUtil;
import com.iot.ota_web.util.RSAUtil;

@RestController
@RequestMapping("/ota/rsa")
public class RSAController extends BasicController{
	
	private static Logger logger = LogManager.getLogger(RSAController.class);
	
	@Autowired
	private ValueOperations<String, Object> valueOperations;
	
	@Value("${rsa.privateKey.expire}")
	private int rsaPrivateKeyExpire;
	
	@RequestMapping(value="/publicKey", method={RequestMethod.GET})
	public String getPublicKey(){
		JSONObject result = generateResult();
        try {
            // 获取密钥对
            KeyPair keypair = RSAUtil.generateKeyPair();
            RSAPublicKey publicKey = (RSAPublicKey) keypair.getPublic();
            // 系数
            String modulus = new String(Hex.encodeHex(publicKey.getModulus().toByteArray()));
            // 指数
            String exponent = new String(Hex.encodeHex(publicKey.getPublicExponent().toByteArray()));
            result.put("modulus", modulus);
            result.put("exponent", exponent);
            // 生成一个密钥记录ID
            String privateKeyId = UUID.randomUUID().toString();
            // 将记录缓存10分钟
            valueOperations.set(privateKeyId, JSONObject.toJSONString((RSAPrivateKey)keypair.getPrivate()), rsaPrivateKeyExpire, TimeUnit.MINUTES);
            result.put("privateKeyId", privateKeyId);
        } catch (Exception e) {
        	ExceptionUtil.printExceptionToLog(logger, e);
            result.put("code", "0001");
            result.put("message", "服务器错误");
        }
        // 生成密钥队
        return result.toJSONString();
	}
}
