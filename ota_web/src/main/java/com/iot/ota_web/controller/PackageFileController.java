package com.iot.ota_web.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.iot.ota_web.bean.TerminalProperty;
import com.iot.ota_web.service.PackageFileService;
import com.iot.ota_web.util.ExceptionUtil;
import com.iot.ota_web.util.RequestUtil;

/**
 * 升级包文件管理控制器
 * @author tangliang
 *
 */
@Controller
@RequestMapping("/ong/packageFile")
public class PackageFileController extends BasicController{
	
	Logger logger = LogManager.getLogger(PackageFileController.class.getName());
	
	@Autowired
	public PackageFileService packageFileService;
	
	@Autowired
	TerminalProperty terminalProperty;
	
	@RequestMapping(value="/upload", method={RequestMethod.POST})
	public @ResponseBody String deal(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
		
		JSONObject result = generateResult();
		if (!file.isEmpty()) {
			Map<String, Object> params = new HashMap<>();
			params.put("userId", request.getAttribute("userId").toString());
			try {
				params.putAll(RequestUtil.getParams(request));
				packageFileService.saveFile(file, params, result);
			} catch (Exception e) {
				if (e instanceof IOException) {
					result.put("code", "0001");
					result.put("message", e.getMessage());
				}else {
					result.put("code", "0001");
					result.put("message", "err");
				}
			}
		}
		return result.toJSONString();
	}
	
	@RequestMapping(value="/packageFilesInfo", method={RequestMethod.POST})
	public @ResponseBody String getPackageFilesInfo(HttpServletRequest request) {
		JSONObject result = generateResult();
		Map<String, Object> params = new HashMap<>();
		params.put("userId", request.getAttribute("userId").toString());
		try {
			params.putAll(RequestUtil.getParams(request));
			packageFileService.getPackageFileInfo(params, result);
		} catch (Exception e) {
			result.put("code", "0001");
			result.put("message", "服务器错误");
		}
		return result.toJSONString();
	}
	
	@RequestMapping(value="/downloadFile")
	public @ResponseBody String downloadPackageFile(HttpServletRequest request, HttpServletResponse response){
		JSONObject result = generateResult();
		Map<String, Object> params = new HashMap<>();
		try {
			params.putAll(RequestUtil.getParams(request));
		} catch (Exception e) {
			ExceptionUtil.printExceptionToLog(logger, e);
			result.put("code", "0001");
			result.put("message", "服务器错误");
			return result.toJSONString();
		}
		
		if (params.isEmpty() || !params.containsKey("terminal_id") || !params.containsKey("package_id") || !params.containsKey("version_id") || !params.containsKey("fileName")) {
			result.put("code", "0001");
			result.put("message", "请求参数不完整");
			return result.toJSONString();
		}
		
		String realPath = terminalProperty.getUpgradePackagePath();
		StringBuilder filePath = new StringBuilder(realPath).append(File.separator).append(params.get("terminal_id"))
				.append(File.separator).append(params.get("package_id")).append(File.separator).append(params.get("version_id")).append(File.separator).append(params.get("fileName"));
		File file = new File(filePath.toString());
		if (file.exists()) {
			response.setContentType("application/force-download");// 设置强制下载不打开
			response.addHeader("Content-Disposition","attachment;fileName=" + params.get("fileName"));// 设置文件名
			byte[] buffer = new byte[1024];
			FileInputStream fis = null;
			BufferedInputStream bis = null;
			try {
				fis = new FileInputStream(file);
				bis = new BufferedInputStream(fis);
				OutputStream os = response.getOutputStream();
				int i = bis.read(buffer);
				while (i != -1) {
					os.write(buffer, 0, i);
					i = bis.read(buffer);
				}
			} catch (Exception e) {
				ExceptionUtil.printExceptionToLog(logger, e);
				result.put("code", "0001");
				result.put("message", "服务器错误");
				return result.toJSONString();
			} finally {
				if (bis != null) {
					try {
						bis.close();
					} catch (IOException e) {
						ExceptionUtil.printExceptionToLog(logger, e);
						result.put("code", "0001");
						result.put("message", "服务器错误");
						return result.toJSONString();
					}
				}
				if (fis != null) {
					try {
						fis.close();
					} catch (IOException e) {
						ExceptionUtil.printExceptionToLog(logger, e);
						result.put("code", "0001");
						result.put("message", "服务器错误");
						return result.toJSONString();
					}
				}
			}
		}else {
			result.put("code", "0001");
			result.put("message", "文件不存在");
			return result.toJSONString();
		}
		return result.toJSONString();
	}
}
