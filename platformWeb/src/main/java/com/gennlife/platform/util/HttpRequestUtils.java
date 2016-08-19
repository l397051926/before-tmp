package com.gennlife.platform.util;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;

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
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(6000).setConnectTimeout(3000).build();
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


	/**
	 * post
	 * @param url
	 * @param jsonParam
	 * @return
	 */
	public static String httpPostForRRun(String url, String jsonParam) {
		HttpClient httpClient = HttpClients.createDefault();

		HttpPost method = new HttpPost(url);

		try {
			//RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(6000).setConnectTimeout(3000).build();
			//method.setConfig(requestConfig);
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
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(3000).build();
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

	public static String httpGetForCS(String url) {
		HttpClient httpClient = HttpClients.createDefault();
		HttpGet method = new HttpGet(url);
		method.addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*");
		method.addHeader("Connection","keep-alive");
		method.addHeader("Content-Coding","UTF-8");
		method.addHeader("User-Agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36");
		try {
			/*
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
			*/
			Process process = Runtime.getRuntime().exec("curl "+url);
			BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
			StringBuffer sb = new StringBuffer();
			String line = "";
			while ((line = input.readLine()) != null) {
				sb.append(line);
			}
			return sb.toString();
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
