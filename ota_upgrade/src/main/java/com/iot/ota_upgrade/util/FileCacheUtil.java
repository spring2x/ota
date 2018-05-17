package com.iot.ota_upgrade.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.redis.core.RedisTemplate;


public class FileCacheUtil {
	
	private static Logger logger = LogManager.getLogger(FileCacheUtil.class);
	
	/**
	 * 文件缓存到本地
	 */
	public static ConcurrentHashMap<String, byte[]> fileCacheLocalMap = new ConcurrentHashMap<>();
	
	/**
	 * 获取分片数据
	 * @param redisTemplate
	 * @param key
	 * @param pos
	 * @param len
	 * @param fileSplitSize
	 * @return
	 * @throws Exception
	 */
	public static byte[] getFileBytes(RedisTemplate< String, Object> redisTemplate, 
			String key, int pos, int len, int fileSplitSize) throws Exception{
		byte[] resultBytes = null;
		if (fileCacheLocalMap.containsKey(key)) {
			resultBytes = getFileBytesFromLocal(key, pos, len);
		}else {
			//resultBytes = getFileBytesFromRedis(redisTemplate, key, pos, len, fileSplitSize);
			throw new Exception("down load file-[" + key + "] time is to long, cache data is removed!!!");
		}
		
		return resultBytes;
	}
	
	/**
	 * 从本地缓存中获取分片数据
	 * @param key
	 * @param pos 开始读取的位置
	 * @param len 读取的字节长度
	 * @return
	 * @throws Exception 
	 */
	public static byte[] getFileBytesFromLocal(String key, int pos, int len) throws Exception {
		byte[] cachedData = fileCacheLocalMap.get(key);
		if (cachedData == null) {
			throw new Exception("down load file-[" + key + "] time is to long, cache data is removed!!!");
		}
		long fileLength = cachedData.length;
		if (len + pos >= fileLength) {
			len = (int) (fileLength - pos);
		}
		byte[] fileBytes = new byte[len];
		for (int index = 0; index < len; index++) {
			fileBytes[index] = cachedData[index + pos];
		}
		return fileBytes;
	}
	
	/**
	 * 从redis中获取分片数据
	 * @param redisTemplate
	 * @param key	
	 * @param pos
	 * @param len
	 * @param fileSplitSize
	 * @return
	 * @throws Exception
	 */
	public static byte[] getFileBytesFromRedis(RedisTemplate< String, Object> redisTemplate, 
			String key, int pos, int len, int fileSplitSize) throws Exception {
		int startKey = pos / fileSplitSize + 1;
		int endKey = startKey + len / fileSplitSize - 1;
		
		if (!redisTemplate.opsForHash().hasKey(key, String.valueOf(startKey))) {
			Exception exception = new Exception("request file-[" + key + "] pos-[" + pos + "] startKey-[" + startKey + "] is not cached in redis");
			throw exception;
		}
		
		List<Object> fileSplitKeys = new ArrayList<>();
		for(int keyIndex = startKey; keyIndex <= endKey; keyIndex ++){
			fileSplitKeys.add(String.valueOf(keyIndex));
		}
		
		List<Object> fileSplitObjectList = redisTemplate.opsForHash().multiGet(key, fileSplitKeys);
		byte[] resultBytes = covertObjectResultsToBytes(fileSplitObjectList, fileSplitSize);
		return resultBytes;
	}
	
	/**
	 * 将小文件缓存到本地，避免频繁读取redis。
	 * @param redisTemplate
	 * @param key
	 * @param fileSplitSize
	 */
	public static void cacheFileToLocal(RedisTemplate< String, Object> redisTemplate, String key, int fileSplitSize){
		String fileDataKey = key + "_data";
		if (fileCacheLocalMap.containsKey(fileDataKey)) {
			return;
		}
		int fileSize = new Long((long) redisTemplate.opsForHash().get(key, "fileSize")).intValue();
		int fileSplitNum = fileSize % fileSplitSize == 0 ? fileSize / fileSplitSize : fileSize / fileSplitSize + 1;
		List<Object> fileSplitKeys = new ArrayList<>();
		for(int keyIndex = 1; keyIndex <= fileSplitNum; keyIndex ++){
			fileSplitKeys.add(String.valueOf(keyIndex));
		}
		
		List<Object> fileSplitObjectList = redisTemplate.opsForHash().multiGet(fileDataKey, fileSplitKeys);
		byte[] resultBytes = covertObjectResultsToBytes(fileSplitObjectList, fileSplitSize);
		fileCacheLocalMap.put(fileDataKey, resultBytes);
	}
	
	/**
	 * 将redis返回的object的list转换为byte[]
	 * @param fileSplitObjectList
	 * @param fileSplitSize
	 * @return
	 */
	private static byte[] covertObjectResultsToBytes(List<Object> fileSplitObjectList, int fileSplitSize) {
		List<byte[]> fileSplitBytsList = new ArrayList<>();
		int resultLength = 0;
		for (int index = 0; index < fileSplitObjectList.size(); index++) {
			Object object = fileSplitObjectList.get(index);
			if (object != null) {
				fileSplitBytsList.add(index, (byte[]) object);
				resultLength += fileSplitBytsList.get(index).length;
			} else {
				break;
			}
		}
		byte[] resultBytes = new byte[resultLength];
		for (int index = 0; index < fileSplitBytsList.size(); index++) {
			System.arraycopy(fileSplitBytsList.get(index), 0, resultBytes, index * fileSplitSize,
					fileSplitBytsList.get(index).length);
		}

		return resultBytes;
	}
	
}
