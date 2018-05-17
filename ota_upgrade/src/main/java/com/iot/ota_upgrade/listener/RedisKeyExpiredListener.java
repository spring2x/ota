package com.iot.ota_upgrade.listener;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import com.iot.ota_upgrade.util.FileCacheUtil;

/**
 * redis中，数据库0中的key过期时，会通知处理
 * @author tang
 *
 */
@Service
public class RedisKeyExpiredListener implements MessageListener{

	@Override
	public void onMessage(Message message, byte[] pattern) {
		StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
		String key = stringRedisSerializer.deserialize(message.getBody());
		//key过期，删除java内存中的文件缓存
        if (key.endsWith("_expireTool")) {
			String fileKey = key.substring(0, key.indexOf("_expireTool"));
			FileCacheUtil.fileCacheLocalMap.remove(fileKey + "_data");
		}
	}
}
