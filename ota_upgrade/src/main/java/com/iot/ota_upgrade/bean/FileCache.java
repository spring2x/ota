package com.iot.ota_upgrade.bean;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 文件缓存
 * @author tang
 *
 */
public class FileCache {

	/**
	 * 一个分片的大小，单位为字节。
	 */
	private int cacheSize;
	
	/**
	 * 当前分片的读写锁
	 */
	private static ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	
	/**
	 * 统计有多少个终端正在请求当前分片的数据。
	 */
	private volatile int count;
	
	/**
	 * 缓存的分片数据。
	 */
	private byte[] data;
	
	/**
	 * 当前分片编号。
	 */
	private int currentSplitNum;
}
