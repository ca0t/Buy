package com.ms.common;

import cn.hutool.json.JSONObject;
import com.ms.model.HttpRes;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpClientHelp {

    CloseableHttpClient client;
    HttpClientContext context;


    private RequestConfig requestConfig;

    public HttpClientHelp() {
        //采用绕过验证的方式处理https请求
        SSLContext sslcontext = null;
        try {
            sslcontext = createIgnoreVerifySSL();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        //设置协议http和https对应的处理socket链接工厂的对象
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", new SSLConnectionSocketFactory(sslcontext))
                .build();
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);

        Registry<CookieSpecProvider> cookieHelp = RegistryBuilder.<CookieSpecProvider>create()
                .register("CookieHelp", context -> new CookieHelp()).build();
        HttpClients.custom().setConnectionManager(connManager);

        //创建自定义的httpclient对象
        client = HttpClients.custom().setConnectionManager(connManager).build();

        context = HttpClientContext.create();
        context.setCookieSpecRegistry(cookieHelp);
    }

    public HttpRes get(String url) {
        try {
            HttpRes ba = new HttpRes();
            HttpGet httpGet = new HttpGet(url);
            httpGet.setConfig(RequestConfig.custom().setCookieSpec("CookieHelp").build());
            httpGet.setHeader(new BasicHeader("Accept", "*/*"));
            httpGet.setHeader(new BasicHeader("Accept-Encoding", "gzip, deflate, br"));
            httpGet.setHeader(new BasicHeader("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6"));
            httpGet.setHeader(new BasicHeader("User-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.69 Safari/537.36 Edg/95.0.1020.53"));

            CloseableHttpResponse response = client.execute(httpGet, context);
            HttpEntity entity = response.getEntity();
            ba.setHeaders(response.getAllHeaders());
            ba.setCookieList(context.getCookieStore().getCookies());
            ba.setContent(EntityUtils.toString(entity, "UTF-8"));
            return ba;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public HttpRes post(String url, String postContent, Map<String, String> param) {
        try {
            HttpRes ba = new HttpRes();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(RequestConfig.custom().setCookieSpec("CookieHelp").build());
            httpPost.setHeader(new BasicHeader("Accept-Encoding", "gzip, deflate, br"));
            httpPost.setHeader(new BasicHeader("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6"));
            httpPost.setHeader(new BasicHeader("User-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.69 Safari/537.36 Edg/95.0.1020.53"));
            httpPost.setHeader(new BasicHeader("X-Requested-With", "XMLHttpRequest"));

            if ((postContent == null || postContent.length() <= 0) && (param != null || param.size() > 0)) {
                httpPost.setHeader(new BasicHeader("Accept", "application/json, text/javascript, */*; q=0.01"));
                httpPost.setHeader(new BasicHeader("X-NewRelic-ID", "VgEOVF5RDxAGUlNSAwcPXw=="));
                httpPost.setHeader(new BasicHeader("Cookie","section_data_ids=%7B%22customer%22%3A1637304027%2C%22cart%22%3A1637304328%2C%22directory-data%22%3A1637304027%2C%22ammessages%22%3A1637304328%2C%22coupon-data%22%3A1637304027%2C%22addresses-data%22%3A1637304312%2C%22last-ordered-items%22%3A1637304328%2C%22instant-purchase%22%3A1637304328%7D"));
                //httpPost.setHeader("Content-Type", "multipart/form-data");

                MultipartEntityBuilder EntityBuilder = MultipartEntityBuilder.create();
                ContentType contentType = ContentType.create("text/plain", Charset.forName("UTF-8"));
                EntityBuilder.addTextBody("product", param.get("product"),contentType);
                EntityBuilder.addTextBody("product_sku", param.get("product_sku"),contentType);
                EntityBuilder.addTextBody("selected_configurable_option", "1150",contentType);
                EntityBuilder.addTextBody("related_product", "",contentType);
                EntityBuilder.addTextBody("item", param.get("item"),contentType);
                EntityBuilder.addTextBody("form_key", param.get("form_key"),contentType);
                EntityBuilder.addTextBody("super_attribute[186]", "5692",contentType);
/*                EntityBuilder.addTextBody("product_sku", "123213");
                EntityBuilder.addTextBody("product", "423435134");
                EntityBuilder.addTextBody("PanelNo", "f0sa9df90sdf");*/
                httpPost.setEntity(EntityBuilder.build());
            } else if ((param == null || param.size() <= 0) && (postContent != null && postContent.length() > 0)) {
                httpPost.setHeader(new BasicHeader("Accept", "*/*"));
                httpPost.setHeader(new BasicHeader("Content-Type", "application/json;charset=UTF-8"));

                StringEntity se = new StringEntity(postContent);
                httpPost.setEntity(se);
            }

            CloseableHttpResponse response = client.execute(httpPost, context);
            HttpEntity entity = response.getEntity();
            String html = EntityUtils.toString(entity, "UTF-8");
            ba.setHeaders(response.getAllHeaders());
            ba.setCookieList(context.getCookieStore().getCookies());
            ba.setContent(html);
            return ba;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sc = SSLContext.getInstance("TLS");

        // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) throws CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        sc.init(null, new TrustManager[]{trustManager}, null);
        return sc;
    }
}
