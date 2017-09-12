package com.iot.ota_upgrade.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

import org.springframework.web.multipart.MultipartFile;


public class FileUtil {
	
	public static Map<String, byte[]> fileMap = new HashMap<>();
	
	protected static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9','a', 'b', 'c', 'd', 'e', 'f' };
	protected static MessageDigest messagedigest = null;
	
	static{
	   try{
	    messagedigest = MessageDigest.getInstance("MD5");
	   }catch(NoSuchAlgorithmException nsaex){
	    nsaex.printStackTrace();
	   }
	}
	
	
	
	/**
	 * 计算文件的crc32校验码
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static String getFileCRCCode(File file) throws Exception {

		CRC32 crc32 = new CRC32();
		FileInputStream fileinputstream = null;
		CheckedInputStream checkedinputstream = null;
		String fileCRC32Code = null;
		try {
			fileinputstream = new FileInputStream(file);
			checkedinputstream = new CheckedInputStream(fileinputstream, crc32);
			while (checkedinputstream.read() != -1) {
			}
			fileCRC32Code = Long.toHexString(crc32.getValue()).toUpperCase();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fileinputstream != null) {
				try {
					fileinputstream.close();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
			}
			if (checkedinputstream != null) {
				try {
					checkedinputstream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return fileCRC32Code;
	}
	
	
	/**
	 * 获取文件的MD5校验码
	 * @param file
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public static String getFileMD5Code(File file) throws IOException {
		FileInputStream in = new FileInputStream(file);
		FileChannel ch = in.getChannel();
		MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
		messagedigest.update(byteBuffer);
		return bufferToHex(messagedigest.digest());
	}

	private static String bufferToHex(byte bytes[]) {
		return bufferToHex(bytes, 0, bytes.length);
	}

	private static String bufferToHex(byte bytes[], int m, int n) {
		StringBuffer stringbuffer = new StringBuffer(2 * n);
		int k = m + n;
		for (int l = m; l < k; l++) {
			appendHexPair(bytes[l], stringbuffer);
		}
		return stringbuffer.toString();
	}

	private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
		char c0 = hexDigits[(bt & 0xf0) >> 4];
		char c1 = hexDigits[bt & 0xf];
		stringbuffer.append(c0);
		stringbuffer.append(c1);
	}
	
	/**
	 * 获取文件的SHA1校验码
	 * @param file
	 * @return
	 * @throws OutOfMemoryError
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	@SuppressWarnings("resource")
	public static String getFileSHA1Code(File file) throws OutOfMemoryError, IOException, NoSuchAlgorithmException {
		messagedigest = MessageDigest.getInstance("SHA-1");
		FileInputStream in = new FileInputStream(file);
		FileChannel ch = in.getChannel();
		MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
		messagedigest.update(byteBuffer);
		return bufferToHex(messagedigest.digest());
	}
	
	/**
	 * 读取文件指定位置的指定字节数。
	 * @param fileMark
	 * @param pos 开始读取的位置
	 * @param len 读取的字节长度
	 * @return
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static byte[] getFileBytes(String fileMark, int pos, int len) throws IOException, InterruptedException {
		/*FileInputStream stream = null;
		if (len + pos >= file.length()) {
			len = (int) (file.length() - pos);
		}
		byte[] fileBytes = new byte[len];
		try {
			stream = new java.io.FileInputStream(file);
			// 跳过之前的字节数
			stream.skip(pos);
			stream.read(fileBytes);
			stream.close();
		} catch (IOException e) {
			logger.error("get file bytes error!!!  " + e.getMessage());
			throw e;
		} finally {
			if (stream != null) {
				stream.close();
			}
		}*/
		
		
		/*if (len + pos >= file.length()) {
			len = (int) (file.length() - pos);
		}
		byte[] fileBytes = new byte[len];
		RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
		FileChannel fileChannel = randomAccessFile.getChannel();
		FileLock fileLock = null;
		while (true) {
			try {
				fileLock = fileChannel.tryLock(0, Long.MAX_VALUE, true);
				if (fileLock != null) {
					break;
				}
				Thread.sleep(500);
			} catch (Exception e) {
				logger.debug("**************************   " + "can not get the file lock now" + e.getMessage());
				throw e;
			}
		}
		randomAccessFile.skipBytes(pos);
		randomAccessFile.read(fileBytes);
		fileLock.release();
		fileChannel.close();
		randomAccessFile.close();
		randomAccessFile = null;
		return fileBytes;*/
		
		
		/*if (len + pos >= file.length()) {
			len = (int) (file.length() - pos);
		}
		byte[] fileBytes = new byte[len];
		RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
		FileChannel fileChannel = randomAccessFile.getChannel();
		FileLock fileLock = null;
		while (true) {
			try {
				fileLock = fileChannel.tryLock(0, Long.MAX_VALUE, true);
				if (fileLock != null) {
					break;
				}
				Thread.sleep(500);
			} catch (Exception e) {
				logger.debug("**************************   " + "can not get the file lock now" + e.getMessage());
				throw e;
			}
		}
		randomAccessFile.skipBytes(pos);
		randomAccessFile.read(fileBytes);
		//fileLock.release();
		//fileChannel.close();
		randomAccessFile.close();
		randomAccessFile = null;
		return fileBytes;*/
		
		byte[] buffer = fileMap.get(fileMark);
		long fileLength = buffer.length;
		if (len + pos >= fileLength) {
			len = (int) (fileLength - pos);
		}
		byte[] fileBytes = new byte[len];
		for (int index = 0; index < len; index++) {
			fileBytes[index] = buffer[index + pos];
		}
		/*
		if (len + pos >= file.length()) {
			len = (int) (file.length() - pos);
		}
		ByteBuffer byteBuffer = ByteBuffer.allocate(len);
		byte[] fileBytes = new byte[len];
		Path path = Paths.get(file.getAbsolutePath());
		AsynchronousFileChannel asyFileChannel = AsynchronousFileChannel.open(path, StandardOpenOption.READ);
		Future<Integer> operation = asyFileChannel.read(byteBuffer, pos);
		
		while(!operation.isDone());

		byteBuffer.flip();
		byteBuffer.get(fileBytes);
		byteBuffer.clear();*/
		
		/*if (len + pos >= file.length()) {
			len = (int) (file.length() - pos);
		}
		ByteBuffer byteBuffer = ByteBuffer.allocate(len);
		byte[] fileBytes = new byte[len];

		if (!fileMap.containsKey(file.getAbsolutePath())) {
			RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
			fileMap.put(file.getAbsolutePath(), randomAccessFile);
		}
		
		try {
			RandomAccessFile randomAccessFile = fileMap.get(file.getAbsolutePath());
			FileChannel fileChannel = randomAccessFile.getChannel();
			fileChannel.read(byteBuffer,pos);
			byteBuffer.flip();
			byteBuffer.get(fileBytes);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw e;
		}*/
		
		return fileBytes;
	}
	
	
	public static void initDownLoadFile(String fileMark, MultipartFile file) throws Exception {

		try {
			byte[] fileBytes = new byte[(int) file.getSize()];
			InputStream inputStream = file.getInputStream();
			inputStream.read(fileBytes);
			fileMap.put(fileMark, fileBytes);
		} catch (Exception e) {
			if (fileMap.containsKey(fileMark)) {
				fileMap.remove(fileMark);
			}
			throw e;
		}
	}
	
	public static void main(String[] args) {
		System.out.println(Integer.MAX_VALUE);
	}
}
