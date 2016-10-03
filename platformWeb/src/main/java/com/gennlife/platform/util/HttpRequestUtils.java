package com.gennlife.platform.util;

import com.squareup.okhttp.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;

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
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(3000).build();
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
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder()
				.url(url)
				.get()
				.addHeader("cache-control", "no-cache")
				.addHeader("postman-token", "95435e0a-f31f-fcfd-74f9-c9ffc4b38acb")
				.build();
		try {
			Response response = client.newCall(request).execute();
			return response.body().string();
		} catch (IOException e) {
			logger.error("" + url, e);
			return ParamUtils.errorParam("出现异常");
		}
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

	public static String httpPost(String url,File file,String fileName) {
		// 实例化http客户端
		HttpClient httpClient = new DefaultHttpClient();
		// 实例化post提交方式
		HttpPost post = new HttpPost(url);
		try {
			// 实例化参数对象
			MultipartEntity params = new MultipartEntity();
			// 图片文本参数
			params.addPart("textParams", new StringBody("test", Charset.forName("UTF-8")));
			// 设置上传文件
			// 文件参数内容
			FileBody fileBody = new FileBody(file);
			// 添加文件参数
			params.addPart("CSV", fileBody);
			params.addPart("photoName", new StringBody(fileName));
			post.setEntity(params);
			// 执行post请求并得到返回对象 [ 到这一步我们的请求就开始了 ]
			HttpResponse resp = httpClient.execute(post);
			// 解析返回请求结果
			HttpEntity entity = resp.getEntity();
			InputStream is = entity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			StringBuffer buffer = new StringBuffer();
			String temp;
			while ((temp = reader.readLine()) != null) {
				buffer.append(temp);
			}
			return buffer.toString();
		} catch (UnsupportedEncodingException e) {
			logger.error("",e);
		} catch (ClientProtocolException e) {
			logger.error("",e);
		} catch (IOException e) {
			logger.error("",e);
		} catch (IllegalStateException e) {
			logger.error("",e);
		}
		return null;
	}

}
