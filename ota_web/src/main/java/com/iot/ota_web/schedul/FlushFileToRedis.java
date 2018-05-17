package com.iot.ota_web.schedul;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.iot.ota_web.bean.PackageFile;
import com.iot.ota_web.bean.TerminalProperty;
import com.iot.ota_web.mapper.PackageFileMapper;
import com.iot.ota_web.service.FileCacheService;
import com.iot.ota_web.util.ExceptionUtil;

/**
 * 定时检查，把没有缓存到redis中的固件都缓存到redis中
 * @author tang
 *
 */
@Component
public class FlushFileToRedis {
	
	private static final Logger logger = LogManager.getLogger(FlushFileToRedis.class);
	
	@Autowired
	private FileCacheService fileCacheService;
	@Autowired
	private PackageFileMapper packageFileMapper;
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	@Autowired
	private TerminalProperty terminalProperty;
	
	private AtomicBoolean canFlush = new AtomicBoolean(true);
	
	/**
	 * 每分钟检查一次，看是否有文件需要被缓存到redis中
	 */
	//@Scheduled(cron="0 0 2 * * ?")
	@Scheduled(fixedRate=60000)
	public void flush(){
		//避免上一个定时任务没有跑完，下一个定时任务又开始
		if (!canFlush.compareAndSet(true, false)) {
			return;
		}
		
		String key = null;
		try {
			List<PackageFile> packageFiles = packageFileMapper.getPackageFiles(new HashMap<String, Object>());
			if (packageFiles != null) {
				for(PackageFile packageFile : packageFiles){
					key = packageFile.getTerminalId() + "_" + packageFile.getPackageId() + "_"  + packageFile.getPackageVersionId();
					if (!redisTemplate.hasKey(key)) {
						String fileRootPath = terminalProperty.getUpgradePackagePath();
						File fileDirectory = new File(fileRootPath + File.separator + packageFile.getTerminalId() + File.separator
								+ packageFile.getPackageId() + File.separator + packageFile.getPackageVersionId());
						File[] listFiles = fileDirectory.listFiles();
						if (listFiles == null || listFiles.length == 0) {
							logger.info("file -[" + key + "] is not exist, can not be flush to redis by schedule");
							continue;
						}
						logger.info("file -[" + key + "] flush to redis start by schedule");
						File flushFile = fileDirectory.listFiles()[0];
						fileCacheService.cacheProcess(key, flushFile);
						logger.info("file -[" + key + "] flush to redis end by schedule");
					}
				}
			}
		} catch (Exception e) {
			canFlush.set(true);
			ExceptionUtil.printExceptionToLog(logger, e);
			try {
				fileCacheService.removeFileFromRedis(key);
			} catch (Exception e1) {
				ExceptionUtil.printExceptionToLog(logger, e1);
			}
			
		}
		canFlush.set(true);
	}
}
