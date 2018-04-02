package com.iot.ota_web.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;
import com.iot.ota_web.mapper.PackageFileMapper;

public class FileUtil {
	
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
	 * @return 文件保存路径
	 * @throws IOException 
	 */
	public static String savePackage(MultipartFile file, String rootPath, String deviceTypeMark, String packageMark, String packageVersionMark) throws IOException{
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
			throw e;
		}
		
		return versionDirectory.getPath() + File.separator + file.getOriginalFilename();
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
			throw e;
		} catch (IOException e) {
			throw e;
		} finally {
			if (fileinputstream != null) {
				try {
					fileinputstream.close();
				} catch (IOException e2) {
					throw e2;
				}
			}
			if (checkedinputstream != null) {
				try {
					checkedinputstream.close();
				} catch (IOException e) {
					throw e;
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
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(file);
			return DigestUtils.md5Hex(inputStream);
		} catch (IOException e) {
			throw e;
		}finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}
	}
	
	/**
	 * 获取文件的SHA1校验码
	 * @param file
	 * @return
	 * @throws OutOfMemoryError
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	public static String getFileSHA1Code(File file) throws IOException{
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(file);
			return DigestUtils.sha1Hex(inputStream);
		} catch (IOException e) {
			throw e;
		}finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}
		
	}
	
	/**
	 * 获取文件的字节数据
	 * @param file 文件不能超过2G
	 * @return
	 * @throws Exception 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static byte[] getFileBytes(File file) throws Exception{
		//文件大小不能超过2G
		byte[] fileBytes = new byte[(int) file.length()];
		
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(file);
			fileInputStream.read(fileBytes);
		} catch (Exception e) {
			throw e;
		}finally {
			if (fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
					throw e;
				}
			}
		}
		return fileBytes;
	}
}
