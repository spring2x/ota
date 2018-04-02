package com.iot.ota_web.service;

import java.io.File;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.iot.ota_web.util.FileUtil;

/**
 * 文件缓存服务
 * @author tang
 *
 */
@Service
public class FileCacheService {

	
	private static Logger logger = LogManager.getLogger(FileCacheService.class);
	
	@Autowired
	RedisTemplate<String, Object> redisTemplate;
	
	/**
	 * 文件数据分片的基本大小为512个字节，不能改变
	 */
	public static final short FILE_SPLIT_UTIL_LENGTH = 512;
	
	/**
	 * 1024个分片时往redis放一次数据;
	 */
	public static final short FILE_SPLIT_NUM_TO_REDIS = 1024;
	
	/**
	 * 文件缓存到redis服务入口
	 * @param key
	 * @param file
	 * @throws Exception
	 */
	public void cacheProcess(String key, File file) throws Exception{
		if (redisTemplate.hasKey(key)) {
			return;
		}
		try {
			logger.info("file-[" + key + "] calculation crcCode start.....");
			String crcCode = FileUtil.getFileCRCCode(file);
			logger.info("file-[" + key + "] calculation crcCode end, crcCode-[" + crcCode + "]");
			logger.info("file-[" + key + "] calculation md5Code start.....");
			String md5Code = FileUtil.getFileMD5Code(file);
			logger.info("file-[" + key + "] calculation md5Code end, md5Code-[" + md5Code + "]");
			logger.info("file-[" + key + "] calculation sha1Code start.....");
			String sha1Code = FileUtil.getFileSHA1Code(file);
			logger.info("file-[" + key + "] calculation sha1Code end, sha1Code-[" + sha1Code + "]");
			redisTemplate.opsForHash().put(key, "crcCode", crcCode);
			redisTemplate.opsForHash().put(key, "md5Code", md5Code);
			redisTemplate.opsForHash().put(key, "sha1Code", sha1Code);
			redisTemplate.opsForHash().put(key, "filePath", file.getAbsolutePath());
			putFileToRedisCache(key, file, FILE_SPLIT_NUM_TO_REDIS);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Async
	public void ayncCacheProcess(String key, File file) throws Exception{
		cacheProcess(key, file);
	}
	
	/**
	 * 将文件分片缓存到redis中
	 * @param key
	 * @param file
	 * @param splitNumToRedis 多少个分片时往redis写一次
	 * @return
	 * @throws Exception 
	 */
	public void putFileToRedisCache(String key, File file, int splitNumToRedis) throws Exception{
		try {
			byte[] fileBytes = FileUtil.getFileBytes(file);
			int fileSplitNum = fileBytes.length % FILE_SPLIT_UTIL_LENGTH == 0
					? fileBytes.length / FILE_SPLIT_UTIL_LENGTH
					: fileBytes.length / FILE_SPLIT_UTIL_LENGTH + 1;
			logger.info("file-[" + key + "] split num-[" + fileSplitNum + "]");
			Map<String, byte[]> fileSplitMap = new ConcurrentHashMap<>();
			logger.info("file-[" + key + "] save to redis start....");
			long saveStartTime = System.currentTimeMillis();
			for(int index = 1; index <= fileSplitNum; index ++){
				int from = (index - 1) * FILE_SPLIT_UTIL_LENGTH;
				int to = index * FILE_SPLIT_UTIL_LENGTH >= fileBytes.length ? 
						fileBytes.length : index * FILE_SPLIT_UTIL_LENGTH;
				byte[] splitData = Arrays.copyOfRange(fileBytes, from, to);
				fileSplitMap.put(String.valueOf(index), splitData);
				if (index % splitNumToRedis ==0) {
					redisTemplate.opsForHash().putAll(key, fileSplitMap);
					fileSplitMap.clear();
				}
			}
			redisTemplate.opsForHash().putAll(key, fileSplitMap);
			long saveEndTime = System.currentTimeMillis();
			logger.info("file-[" + key + "] save to redis end, spend time-[" + (saveEndTime - saveStartTime) + "] millis");
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 从redis中删除缓存的文件
	 * @param key
	 * @return
	 */
	public void removeFileFromRedis(String key) throws Exception{
		try {
			redisTemplate.delete(key);
		} catch (Exception e) {
			throw e;
		}
	}


}
