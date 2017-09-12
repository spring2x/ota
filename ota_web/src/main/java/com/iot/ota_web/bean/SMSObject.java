package com.iot.ota_web.bean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class SMSObject {

	private static final String SMS_URL = "http://api.sms.heclouds.com/tempsmsSend";

	// 参数集合
	private Map<String, String> _param = null;

	public SMSObject() {
		this._param = new HashMap<String, String>();
	}

	// 获取参数字节数组
	private byte[] getSMSParam() {
		StringBuilder result = new StringBuilder();

		for (Entry<String, String> entry : _param.entrySet()) {
			result.append(String.format("&%s=%s", entry.getKey(), entry.getValue()));
		}

		if (result.length() > 0) {
			result.deleteCharAt(0);
		}

		return result.toString().getBytes();
	}

	private HttpURLConnection getSMSConnection() throws IOException {
		HttpURLConnection result = (HttpURLConnection) new URL(SMS_URL).openConnection();
		result.setRequestMethod("POST");
		result.setDoOutput(true);
		return result;
	}

	// 将短信参数写入连接对象
	private void writeSMSParam(HttpURLConnection connection) throws IOException {
		connection.getOutputStream().write(getSMSParam());
		connection.getOutputStream().close();
	}

	// 获取短信发送结果
	private SMSSendResponse getSendResponse(HttpURLConnection connection) throws IOException {
		SMSSendResponse result = new SMSSendResponse();

		result.setResponseCode(connection.getResponseCode());

		BufferedReader br = new BufferedReader(new InputStreamReader(
				result.getResponseCode() == 200 ? connection.getInputStream() : connection.getErrorStream(), "utf-8"));

		StringBuilder sb = new StringBuilder();
		String temp = null;
		while ((temp = br.readLine()) != null) {
			sb.append(temp);
		}

		result.setResponseStr(sb.toString());
		System.out.println("验证码发送结果：" + result.getResponseStr());

		return result;
	}

	/**
	 * 发送短信
	 * 
	 * @return 发送结果代码
	 */
	public SMSSendResponse sendSMS() throws IOException {
		HttpURLConnection connection = getSMSConnection();
		writeSMSParam(connection);
		return getSendResponse(connection);
	}

	public void addParam(String key, String value) {
		this._param.put(key, value);
	}

	public class SMSSendResponse {
		private int responseCode = -1;
		private String responseStr = "";

		public int getResponseCode() {
			return responseCode;
		}

		public void setResponseCode(int responseCode) {
			this.responseCode = responseCode;
		}

		public String getResponseStr() {
			return responseStr;
		}

		public void setResponseStr(String responseStr) {
			this.responseStr = responseStr;
		}
	}

}
