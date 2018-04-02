package com.iot.ota_upgrade.bean;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.data.redis.core.RedisTemplate;

/**
 * 当文件比较大时，如果每个请求都从redis读取数据，由于redis是单线程，所以当并发高时，请求时间会出现线性增长。
 * 本类主要用于分片缓存文件数据，避免高频率从redis读取数据
 * @author tang
 *
 */
public class FileCacheTool {
	/**
	 * 当前总共缓存了多少字节的数据，当这个值超过一定大小时，将淘汰cout最小的缓存。
	 */
	private volatile int totalCacheSize;
	
	/**
	 * 文件缓存
	 */
	private static ConcurrentHashMap<String, FileCache> fileCacheMap = new ConcurrentHashMap<>();
	
	
	public static void cache(RedisTemplate<String, Object> redisTemplate, String key){
		
	}
	
	
}
