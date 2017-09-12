package com.iot.ota_web.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;
import com.iot.ota_web.mapper.PackageFileMapper;

public class FileUtil {
	
	private static Logger logger = LogManager.getLogger(FileUtil.class.getName());
	
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
	 * 根据设备id,包名称，包版本创建文件夹,保存升级包。
	 * @param file
	 * @param rootPath
	 * @param deviceTypeMark
	 * @param packageMark
	 * @param packageVersionMark
	 * @throws IOException 
	 */
	public static void savePackage(MultipartFile file, String rootPath, String deviceTypeMark, String packageMark, String packageVersionMark) throws IOException{
		File rootDirectory = new File(rootPath);
		if (!rootDirectory.exists()) {
			rootDirectory.mkdirs();
		}
		
		File deviceDirectory = new File(rootPath + File.separator + deviceTypeMark);
		if (!deviceDirectory.exists()) {
			deviceDirectory.mkdirs();
		}
		
		File packageDirectory = new File(rootPath + File.separator + deviceTypeMark + File.separator + packageMark);
		if (!packageDirectory.exists()) {
			packageDirectory.mkdirs();
		}
		
		File versionDirectory = new File(rootPath + File.separator + deviceTypeMark + File.separator + packageMark + File.separator + packageVersionMark);
		if (!versionDirectory.exists()) {
			versionDirectory.mkdirs();
		}
		
		if (versionDirectory.listFiles() != null && versionDirectory.listFiles().length > 0) {
			throw new IOException(versionDirectory.getPath() + "  已经存在文件");
		}
			
		try {
			FileUtils.copyInputStreamToFile(file.getInputStream(),
					new File(versionDirectory.getPath() + File.separator + file.getOriginalFilename()));
		} catch (IOException e) {
			logger.error("write file to disk error!!!   " + e.getMessage());
			throw e;
		}
	}
	
	/**
	 * 删除文件
	 * @param params
	 * @param packageFileMapper
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	public static void deleteFolder(PackageFileMapper packageFileMapper, String terminalPackagePath) throws IOException, InterruptedException{
		//删除终端的所有升级文件
		File terminalPackageFile = new File(terminalPackagePath);
		
		if (!terminalPackageFile.exists()) {
			throw new IOException(terminalPackagePath + " is not exist!!!");
		}
		
		if (!terminalPackageFile.isDirectory()) {
			throw new IOException(terminalPackagePath + " is not a directory!!!");
		}
		
		File[] files = terminalPackageFile.listFiles();
		for(File file : files){
			if (file.isFile()) {
				boolean deleteResult = file.delete();
				if (!deleteResult) {
					throw new IOException("delete terminal package file false!!!   " + "check the file " + file.getName() + " is open or not");
				}
			}else if (file.isDirectory()) {
				deleteFolder(packageFileMapper, file.getPath());
			}
		}
		//睡1s，防止文件夹打开时，无法删除的情况
		Thread.sleep(1000);
		boolean deleteResult = terminalPackageFile.delete();
		if (!deleteResult) {
			throw new IOException("delete terminal package file false!!!   " + " can not delete " + terminalPackageFile.getPath());
		}
	}
	
	/**
	 * 创建文件夹
	 * @param relativePath
	 * @throws Exception
	 */
	public static void createFolder(String relativePath, String rootPath) throws Exception{
		//删除终端的所有升级文件
		String path = rootPath + File.separator + relativePath;
		File file = new File(path);
		if (!file.exists()) {
			file.mkdir();
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
			logger.error(e);
			throw e;
		} catch (IOException e) {
			logger.error(e);
			throw e;
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
	public static String getFileMD5Code(File file) throws IOException {
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			FileChannel ch = in.getChannel();
			MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
			messagedigest.update(byteBuffer);
			return bufferToHex(messagedigest.digest());
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}finally {
			in.close();
		}
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
	public static String getFileSHA1Code(File file) throws OutOfMemoryError, IOException, NoSuchAlgorithmException {
		messagedigest = MessageDigest.getInstance("SHA-1");
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			FileChannel ch = in.getChannel();
			MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
			messagedigest.update(byteBuffer);
			return bufferToHex(messagedigest.digest());
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}finally {
			in.close();
		}
		
	}
}
