package com.iot.ota_web.controller;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.CharsetUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.iot.ota_web.bean.TerminalProperty;
import com.iot.ota_web.util.FileUtil;

/**
 * 升级包文件下发到具体的升级服务器
 * @author liqiang
 *
 */
@RestController
@RequestMapping("/fileIssued")
public class PackageFileIssuedController {
	
	private Logger logger = LogManager.getLogger(PackageFileIssuedController.class);
	
	@Autowired
	TerminalProperty terminalProperty;

	@RequestMapping(value="", produces="text/html;charset=UTF-8", method={RequestMethod.POST})
	public String dealIssuedRequest(@RequestBody JSONObject params, HttpServletRequest request, HttpServletResponse response){
		JSONObject result = new JSONObject();
		result.put("code", "0000");
		String terminalMark = params.getString("terminal");
		String packageMark = params.getString("package");
		String versionMark = params.getString("version");
		String responseUrl = params.getString("responseUrl");
		
		logger.info(responseUrl + "  request to download file" + terminalMark + File.separator + packageMark + File.separator + versionMark);
		MultipartEntityBuilder reqEntity = null;
		try {
			reqEntity = MultipartEntityBuilder.create()
	                .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
	                .setCharset(CharsetUtils.get("UTF-8"));
		} catch (Exception e) {
			logger.error("PackageFileIssuedController error log: " + e);
			result.put("code", "0001");
			result.put("message", "server error!!!");
			return result.toJSONString();
		}
		
		String filePath = new StringBuilder(terminalProperty.getUpgradePackagePath()).append(File.separator).append(terminalMark)
				.append(File.separator).append(packageMark).append(File.separator).append(versionMark).toString();
		File fileFolder = new File(filePath);
		if (!fileFolder.exists() || fileFolder.listFiles().length == 0) {
			result.put("code", "0001");
			result.put("message", "down load file is not exist!!!");
			return result.toJSONString();
		}
		File downLoadFile = fileFolder.listFiles()[0];
		try {
			String crcCode = FileUtil.getFileCRCCode(downLoadFile);
			String md5Code = FileUtil.getFileMD5Code(downLoadFile);
			String sha1Code = FileUtil.getFileSHA1Code(downLoadFile);
			JSONObject valideCodeJson = new JSONObject();
			valideCodeJson.put("crcCode", crcCode);
			valideCodeJson.put("md5Code", md5Code);
			valideCodeJson.put("sha1Code", sha1Code);
			reqEntity.addTextBody("valideCode", valideCodeJson.toJSONString());
		} catch (Exception e1) {
			result.put("code", "0001");
			result.put("message", "get file valide code error!!!");
			return result.toJSONString();
		}
		reqEntity.addPart(downLoadFile.getName(), new FileBody(downLoadFile));
		reqEntity.addTextBody("terminal", terminalMark);
		reqEntity.addTextBody("package", packageMark);
		reqEntity.addTextBody("version", versionMark);
		
		HttpPost httpPost = new HttpPost(responseUrl);
		httpPost.setEntity(reqEntity.build());
		CloseableHttpClient client = HttpClients.createDefault();
		
		try {
			client.execute(httpPost);
		} catch (ClientProtocolException e) {
			logger.error(e);
			result.put("code", "0001");
			result.put("message", "server error!!!");
			return result.toJSONString();
		} catch (IOException e) {
			logger.error(e);
			result.put("code", "0001");
			result.put("message", "server error!!!");
			return result.toJSONString();
		}finally {
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result.toJSONString();
	}
	
}
