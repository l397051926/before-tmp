package com.gennlife.platform.util;

import com.gennlife.platform.exception.ReadTimeOutException;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.SocketTimeoutException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Map;

public class HttpRequestUtils {
    private static Logger logger = LoggerFactory.getLogger(HttpRequestUtils.class);

    /**
     * post
     *
     * @param url
     * @param jsonParam
     * @return
     */
    public static String httpPost(String url, String jsonParam) {
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(50000).setConnectTimeout(50000).build();
        return httpPostExecute(url, jsonParam, requestConfig);
    }
    public static String httpPostToRws(String url, String jsonParam) {
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(45000).setConnectTimeout(45000).build();
        return httpPostExecute(url, jsonParam, requestConfig);
    }

    public static String httpPost(String url, String jsonParam, int socketTimeout, int connectTimeout) {
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout).build();
        return httpPostExecute(url, jsonParam, requestConfig);
    }

    public static String httpPostPubMed(String url, String jsonParam) {
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(600000).setConnectTimeout(600000).build();
        return httpPostExecute(url, jsonParam, requestConfig);
    }

    /**
     * post
     *
     * @param url
     * @param jsonParam
     * @return
     */
    public static String httpPostForSampleImport(String url, String jsonParam, int timeOut) {
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeOut).setConnectTimeout(1010*60*1000000).build();
        return httpPostExecute(url, jsonParam, requestConfig);
    }

    public static String httpPostExecute(String url, String jsonParam, RequestConfig requestConfig) {
        HttpClient httpClient = HttpClients.createDefault();

        HttpPost method = new HttpPost(url);

        try {
            if (requestConfig != null) method.setConfig(requestConfig);
            if (null != jsonParam) {
                StringEntity entity = new StringEntity(jsonParam, "utf-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/json");
                method.setHeader("Accept-Encoding", "gzip,deflate");
                method.setEntity(entity);
            }
            HttpResponse result = httpClient.execute(method);
            url = URLDecoder.decode(url, "UTF-8");
            if (result.getStatusLine().getStatusCode() == 200) {
                String str = "";
                Header contentHeader = result.getFirstHeader("Content-Encoding");
                try {
                    if (contentHeader != null
                            && contentHeader.getValue().toLowerCase().indexOf("gzip") > -1) {
                        str = EntityUtils.toString(new GzipDecompressingEntity(result.getEntity()));
                        return str;
                    }
                    str = EntityUtils.toString(result.getEntity());
                    return str;
                } catch (Exception e) {
                    logger.error("" + url + " param " + jsonParam, e);
                }
            } else if (result.getStatusLine().getStatusCode() == 404) {
                logger.error(url + " 不存在");
            } else if(result.getStatusLine().getStatusCode() == 504){
                logger.error("超时了 504 url "+ url +"超时时间 getConnectTimeout" + requestConfig.getConnectTimeout()+" socketTimeout:  "+requestConfig.getSocketTimeout() + "result : " + EntityUtils.toString(result.getEntity()) );
                throw new RuntimeException("系统繁忙 请稍后再试");
            }else {
                logger.error("error code " + result.getStatusLine().getStatusCode() + " url " + url + " param " + jsonParam);
            }
        }catch (SocketTimeoutException e){
            logger.error("" + url, e);
            throw new ReadTimeOutException("系统繁忙 请稍后再试");
        } catch (IOException e) {
            logger.error("" + url, e);
        }
        return null;
    }

    /**
     * post
     *
     * @param url
     * @param jsonParam
     * @return
     */
    public static String httpPostForRRun(String url, String jsonParam) {
        return httpPostExecute(url, jsonParam, null);
    }


 /*   public static String httpGet(String url) {
        return httpGet(url, 60000, false);
    }*/

    public static String httpGet(String url) {
        return httpGet(url, 10*60*1000);
    }

    public static String httpGet(String url, int time) {
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet method = new HttpGet(url);
        try {
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(time).setConnectTimeout(time).build();
            method.setConfig(requestConfig);
            method.setHeader("Accept-Encoding", "gzip,deflate");
            HttpResponse result = httpClient.execute(method);
            String str = null;
            if (result.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                Header contentHeader = result.getFirstHeader("Content-Encoding");
                try {
                    if (contentHeader != null
                            && contentHeader.getValue().toLowerCase().indexOf("gzip") > -1) {
                        str = EntityUtils.toString(new GzipDecompressingEntity(result.getEntity()));
                        return str;
                    }
                    str = EntityUtils.toString(result.getEntity(), "utf-8");
                    return str;
                } catch (Exception e) {
                    logger.error("" + url, e);
                }
            } else {
                logger.error("error code " + result.getStatusLine().getStatusCode() + " url" + url);
            }
        } catch (SocketTimeoutException e) {
            logger.error(url + " 超时");
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

    public static void main(String[] args) {
        String url = "http://localhost:8080/PatientDetail/UploadCsvFile";
        File file = new File("/home-preserved/tomcat_demo2_web/crf/2016_10_05_17_48_22-联调mock数据.csv");
        String fileName = "2016_10_05_17_48_22-联调mock数据.csv";
        System.out.println(httpPost(url, file, fileName));
    }

    public static String httpPost(String url, File file, String fileName) {
        // 实例化http客户端
        HttpClient httpClient = new DefaultHttpClient();
        HttpParams ps = httpClient.getParams();
        ps.setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, Charset.forName("GB2312"));
        // 实例化post提交方式
        HttpPost post = new HttpPost(url);
        try {

            // 实例化参数对象
            MultipartEntity params = new MultipartEntity();
            // 设置上传文件
            // 文件参数内容
            FileBody fileBody = new FileBody(file);
            // 添加文件参数
            params.addPart("CSV", fileBody);
            params.addPart("photoName", new StringBody(fileName));
            post.setEntity(params);
            // 执行post请求并得到返回对象 [ 到这一步我们的请求就开始了 ]
            HttpResponse resp = httpClient.execute(post);
            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String str = null;
                try {
                    str = EntityUtils.toString(resp.getEntity());
                    return str;
                } catch (Exception e) {
                    logger.error("" + url, e);
                }
            }
        } catch (UnsupportedEncodingException e) {
            logger.error("", e);
        } catch (ClientProtocolException e) {
            logger.error("", e);
        } catch (IOException e) {
            logger.error("", e);
        } catch (IllegalStateException e) {
            logger.error("", e);
        }
        return null;
    }

    public static String httpPostImg(String url, File file, String contentType) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpParams ps = httpClient.getParams();
        // ps.setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, Charset.forName("GB2312"));
        // 实例化post提交方式
        HttpPost post = new HttpPost(url);
        try {
            // 实例化参数对象
            MultipartEntity params = new MultipartEntity();
            // 设置上传文件
            // 文件参数内容
            FileBody fileBody = new FileBody(file, contentType);
            // 添加文件参数
            params.addPart("Image", fileBody);
            post.setEntity(params);
            // 执行post请求并得到返回对象 [ 到这一步我们的请求就开始了 ]
            HttpResponse resp = httpClient.execute(post);
            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String str = null;
                try {
                    str = EntityUtils.toString(resp.getEntity());
                    return str;
                } catch (Exception e) {
                    logger.error("" + url, e);
                }
            }
        } catch (UnsupportedEncodingException e) {
            logger.error("", e);
        } catch (ClientProtocolException e) {
            logger.error("", e);
        } catch (IOException e) {
            logger.error("", e);
        } catch (IllegalStateException e) {
            logger.error("", e);
        }
        return null;
    }

    public static String httpPostNew(String url, File file, String fileName) {
        // 实例化http客户端
        HttpClient httpClient = new DefaultHttpClient();
        HttpParams ps = httpClient.getParams();
        ps.setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, Charset.forName("GB2312"));
        // 实例化post提交方式
        HttpPost post = new HttpPost(url);
        try {

            // 实例化参数对象
            MultipartEntity params = new MultipartEntity();
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
            logger.error("", e);
        } catch (ClientProtocolException e) {
            logger.error("", e);
        } catch (IOException e) {
            logger.error("", e);
        } catch (IllegalStateException e) {
            logger.error("", e);
        }
        return null;
    }

    public static String httpDelte(String url) {
        HttpClient httpClient = HttpClients.createDefault();
        HttpDelete method = new HttpDelete(url);
        try {
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(10000).build();
            method.setConfig(requestConfig);
            HttpResponse result = httpClient.execute(method);
            if (result.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String str = null;
                try {
                    str = EntityUtils.toString(result.getEntity(), "utf-8");
                    return str;
                } catch (Exception e) {
                    logger.error("" + url, e);
                }
            } else {
                logger.error("error code " + result.getStatusLine().getStatusCode() + " url" + url);
            }
        } catch (IOException e) {
            logger.error("" + url, e);
        }
        return null;
    }

    //http get 透传流
    public static void httpGetStream(String url, HttpServletResponse response) {
        HttpClient httpclient = new DefaultHttpClient();
        try {
            HttpGet httpGet = new HttpGet(url);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(30000).build();
            httpGet.setConfig(requestConfig);
            HttpResponse httpResponse = httpclient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String headerName;
                for (Header header : httpResponse.getAllHeaders()) {
                    headerName = header.getName();
                    switch (headerName) {
                        //这里根据需要自己添加需要输出的响应头
                        case "Content-Disposition":
                        case "Content-Type":
                            response.setHeader(headerName, header.getValue());
                            break;
                    }
                }
                HttpEntity httpEntity = httpResponse.getEntity();
                httpEntity.writeTo(response.getOutputStream());
            }
            EntityUtils.consume(httpResponse.getEntity());
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            if (httpclient != null) {
                httpclient.getConnectionManager().shutdown();
            }
        }
    }
    public static String doGet(String url, Map<String, String> params, String encode) throws Exception {
        HttpClient httpClient = HttpClients.createDefault();
        logger.info("执行GET请求，URL = {}", url);
        if(null != params){
            URIBuilder builder = new URIBuilder(url);
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.setParameter(entry.getKey(), entry.getValue());
            }
            url = builder.build().toString();
        }
        // 创建http GET请求
        HttpGet httpGet = new HttpGet(url);
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(30000).build();
        httpGet.setConfig(requestConfig);
        CloseableHttpResponse response = null;
        try {
            // 执行请求
             response = (CloseableHttpResponse) httpClient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                if(encode == null){
                    encode = "UTF-8";
                }
                return EntityUtils.toString(response.getEntity(), encode);
            }
        } finally {
            if (response != null) {
                response.close();
            }
            // 此处不能关闭httpClient，如果关闭httpClient，连接池也会销毁
        }
        return null;
    }


}
