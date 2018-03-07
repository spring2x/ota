package com.iot.oauth.service;

import static org.apache.commons.codec.digest.DigestUtils.md5Hex;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.iot.oauth.bean.DeviceToken;
import com.iot.oauth.bean.PackageFile;
import com.iot.oauth.bean.PlatformProperty;
import com.iot.oauth.mapper.DeviceTokenMapper;
import com.iot.oauth.mapper.PackageFileMapper;
import com.iot.oauth.mapper.PackageVersionMapper;
import com.iot.oauth.mapper.PlatformTokenMapper;
import com.iot.oauth.util.DateUtil;
import com.iot.oauth.util.TokenUtil;

import io.jsonwebtoken.Claims;


@Service("deviceTokenService")
public class DeviceTokenService {
	
	Logger logger = LogManager.getLogger(DeviceTokenService.class.getName());
	@Autowired
	DeviceTokenMapper deviceTokenMapper;
	@Autowired
	PlatformTokenMapper platformTokenMapper;
	@Autowired
	PackageFileMapper packageFileMapper;
	
	@Autowired
	PlatformProperty platformProperty;
	
	@Autowired
	PackageVersionMapper packageVersionMapper;
	
	@Autowired
	private ValueOperations<String, Object> valueOperations;
	/**
	 * 增加设备的token
	 * @param params
	 * @param result
	 * @throws Exception
	 */
	public void addDeviceToken(JSONObject params, JSONObject result) throws Exception {
		
		if (!params.containsKey("mark") || null == params.get("mark") || "".equals(params.get("mark"))
				|| !params.containsKey("type") || null == params.get("type") || "".equals(params.get("type"))
				) {
			result.put("code", "0001");
			result.put("message", "请求参数不完整");
			return;
		}
		
		String deviceMark = (String) params.get("mark");
		String uuid = (String) valueOperations.get(deviceMark);
		if (uuid != null) {
			result.put("code", "0001");
			result.put("message", "设备标识已存在");
			return;
		}
		
		JSONObject payload = new JSONObject();
		payload.put("mark", deviceMark);	//设备标识（设备组编号或单个设备）
		payload.put("type", params.get("type"));	//设备类型（设备组或者单个设备）
		long ttlMillis = platformProperty.getDeviceTokenExpiredTime() * 60 * 1000;
		String token = TokenUtil.createToken(payload, ttlMillis, deviceMark);
		Claims claims = TokenUtil.parseJWT(token, new JSONObject());
		uuid = claims.getId();
		valueOperations.set(deviceMark, uuid, platformProperty.getDeviceTokenExpiredTime(), TimeUnit.MINUTES);
		valueOperations.set(uuid, token, platformProperty.getDeviceTokenExpiredTime(), TimeUnit.MINUTES);
		result.put("token", uuid);
		result.put("expire_time", DateUtil.DateFormat(claims.getExpiration().getTime()));
		
		/*String uuid = generateUUid(params);
		int expireTime = platformProperty.getDeviceTokenExpiredTime();
		// 设置token过期时间
		Calendar calendar = Calendar.getInstance();
		Date expiredDate = new Date(System.currentTimeMillis());
		calendar.setTime(expiredDate);
		calendar.add(Calendar.MINUTE, expireTime);
		DeviceToken deviceToken = new DeviceToken(null, params.getString("mark"), uuid, params.getInteger("type"),
				new Timestamp(calendar.getTimeInMillis()));
		JSONObject _params = new JSONObject();
		_params.put("mark", deviceToken.getMark());
		try {
			List<DeviceToken> deviceTokens = deviceTokenMapper.getDeviceTokens(_params);
			if (deviceTokens != null && !deviceTokens.isEmpty()) {
				String org_token = deviceTokens.get(0).getUuid();
				JSONObject param = new JSONObject();
				param.put("org_token", org_token);
				updateDeviceToken(param, result);
				return;
			}
			deviceTokenMapper.addDeviceToken(deviceToken);
			result.put("token", deviceToken.uuid);
			result.put("expire_time", deviceToken.expireTime);
		} catch (Exception e) {
			logger.error("add device token err   " + e.getMessage());
			throw e;
		}*/
	}
	
	/**
	 * 刷新设备token
	 * @param params
	 * @param result
	 * @throws Exception
	 */
	public void updateDeviceToken(JSONObject params, JSONObject result) throws Exception{
		if (!params.containsKey("org_token") || null == params.get("org_token") || "".equals(params.get("org_token"))) {
			result.put("code", "0001");
			result.put("message", "请求参数不完整");
			return;
		}
		
		JSONObject _params = new JSONObject();
		_params.put("uuid", params.getString("org_token"));
		try {
			List<DeviceToken> deviceTokens = deviceTokenMapper.getDeviceTokens(_params);
			if (deviceTokens == null || deviceTokens.isEmpty()) {
				result.put("code", "0001");
				result.put("message", "需要更新的token不存在");
				return;
			}
			String uuid = generateUUid(params);
			int expireTime = platformProperty.getDeviceTokenExpiredTime();
			// 设置token过期时间
			Calendar calendar = Calendar.getInstance();
			Date expiredDate = new Date(System.currentTimeMillis());
			calendar.setTime(expiredDate);
			calendar.add(Calendar.MINUTE, expireTime);
			params.put("uuid", uuid);
			params.put("expire_time", new Timestamp(calendar.getTimeInMillis()));
			params.put("id", deviceTokens.get(0).getId());
			deviceTokenMapper.updateDeviceToken(params);
			result.put("token", uuid);
			result.put("expire_time", params.get("expire_time"));
		} catch (Exception e) {
			logger.error("update token err   " + e.getMessage());
			throw e;
		}
	}
	
	@Transactional(readOnly=true)
	public void getNewVerson(Map<String, Object> params, JSONObject result){
		
		if (!params.containsKey("token") || null == params.get("token")
				|| !params.containsKey("terminal_id") || null == params.get("terminal_id")
				|| !params.containsKey("package_id") || null == params.get("package_id")
				) {
			result.put("code", "0001");
			result.put("message", "请求参数不完整");
			return;
		}
		
		JSONObject jsonParam = new JSONObject();
		jsonParam.put("uuid", params.get("token"));
		List<Map<String, Object>> tokenInfos = platformTokenMapper.getPlatformToken(jsonParam);
		
		if (tokenInfos != null && !tokenInfos.isEmpty()) {
			Map<String, Object> tokenInfo = tokenInfos.get(0);
			long terminalId = (long) tokenInfo.get("terminal_id");
			if (terminalId != Integer.parseInt(params.get("terminal_id").toString())) {
				result.put("code", "403");
				result.put("message", "平台还没有权限");
				return;
			}
			
			Timestamp expireTime = (Timestamp) tokenInfo.get("expireTime");
			if (System.currentTimeMillis() > expireTime.getTime()) {
				result.put("code", "403");
				result.put("message", "token已过期");
				return;
			}
			List<PackageFile> packageFiles = packageFileMapper.getNewPackageFiles(params);
			if (packageFiles == null || packageFiles.isEmpty()) {
				result.put("code", "0001");
				result.put("message", "未找到资源文件");
				return;
			}
			
			PackageFile packageFile = packageFiles.get(0);
			result.put("terminal_id", packageFile.getTerminalId());
			result.put("package_id", packageFile.getPackageId());
			result.put("version_id", packageFile.getPackageVersionId());
			
			params.put("version_id", packageFile.getPackageVersionId());
			result.put("introduce", packageVersionMapper.getPackageVersions(params).get(0).getIntroduce());
		}else {
			result.put("code", "403");
			result.put("message", "平台还没有权限");
			return;
		}
	}
	
	/**
	 * 验证设备的token是否有效
	 * @param params
	 * @param result
	 * @throws Exception
	 */
	public boolean validDeviceToken(JSONObject params, JSONObject result) throws Exception{
		try {
			List<DeviceToken> deviceTokens = deviceTokenMapper.getDeviceTokens(params);
			if (deviceTokens == null || deviceTokens.isEmpty()) {
				result.put("code", "0001");
				result.put("message", "token is invalid");
				return false;
			}
			return true;
		} catch (Exception e) {
			logger.error("valid device token err   " + e.getMessage());
			result.put("code", "0001");
			result.put("message", "server err");
			throw e;
		}
	}
	
	
	public String generateUUid(JSONObject params) throws Exception{
		try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            
            for(Entry<String, Object> entry : params.entrySet()){
            	 dos.write(entry.getValue().toString().getBytes());
            }
            dos.writeLong(System.currentTimeMillis());
            dos.flush();
            return md5Hex(baos.toByteArray()).substring(8, 24);
        } catch (IOException e) {
        	logger.error("Failed to generate access token!!!" + e.getMessage());
        	throw e;
        }
	}
}
