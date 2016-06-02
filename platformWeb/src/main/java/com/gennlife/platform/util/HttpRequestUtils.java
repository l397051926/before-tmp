package com.gennlife.platform.util;

import java.io.IOException;
import java.net.URLDecoder;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequestUtils {
	private static Logger logger = LoggerFactory.getLogger(HttpRequestUtils.class);

	/**
	 * post
	 * @param url
	 * @param jsonParam
	 * @return
	 */
	public static String httpPost(String url, String jsonParam) {
		HttpClient httpClient = HttpClients.createDefault();

		HttpPost method = new HttpPost(url);

		try {
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(3000).setConnectTimeout(3000).build();
			method.setConfig(requestConfig);
			if (null != jsonParam) {
				StringEntity entity = new StringEntity(jsonParam,"utf-8");
				entity.setContentEncoding("UTF-8");
				entity.setContentType("application/json");

				method.setEntity(entity);
			}
			HttpResponse result = httpClient.execute(method);
			url = URLDecoder.decode(url, "UTF-8");
			if (result.getStatusLine().getStatusCode() == 200) {
				String str = "";
				try {
					str = EntityUtils.toString(result.getEntity());
					return str;
				} catch (Exception e) {
					logger.error("" + url, e);
				}
			}
		} catch (IOException e) {
			logger.error("" + url, e);
		}
		return null;
	}


	public static String httpGet(String url) {
		HttpClient httpClient = HttpClients.createDefault();
		HttpGet method = new HttpGet(url);
		try {
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(3000).setConnectTimeout(3000).build();
			method.setConfig(requestConfig);
			HttpResponse result = httpClient.execute(method);
			if (result.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String str = null;
				try {
					str = EntityUtils.toString(result.getEntity(),"utf-8");
					return str;
				} catch (Exception e) {
					logger.error("" + url, e);
				}
			}
		} catch (IOException e) {
			logger.error("" + url, e);
		}
		return null;
	}

	public static String httpPost(String url) {
		HttpClient httpClient = HttpClients.createDefault();
		HttpPost method = new HttpPost(url);
		try {
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(3000).setConnectTimeout(3000).build();
			method.setConfig(requestConfig);
			HttpResponse result = httpClient.execute(method);
			url = URLDecoder.decode(url, "UTF-8");
			if (result.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String str = null;
				try {
					str = EntityUtils.toString(result.getEntity());
					return str;
				} catch (Exception e) {
					logger.error("" + url, e);
				}
			}
		} catch (IOException e) {
			logger.error("" + url, e);
		}
		return null;
	}

	public static void main(String[] args){
		String url = "http://localhost:8081/file/Rec";
		String ch = "中国";
		httpPost(url,ch);
	}



}
