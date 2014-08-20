package org.bigmouth.ticket4j.http;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.params.CoreConnectionPNames;
import org.bigmouth.ticket4j.Ticket4jDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;


public final class Ticket4jHttpClient {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Ticket4jHttpClient.class);
    private static final String SCHEMA = Ticket4jDefaults.SCHEMA;
    
    private String host = Ticket4jDefaults.HOST;
    private int port = Ticket4jDefaults.PORT;
    private int timeout = Ticket4jDefaults.TIME_OUT; 

    public HttpClient buildHttpClient() {
        try {
            SSLContext ctx = SSLContext.getInstance("SSL");  
            ctx.init(null, new TrustManager[] { new X509TrustManager() {
                
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                
                public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                }
                
                public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                }
            }}, null);
            ClientConnectionManager ccm = new SingleClientConnManager();
            SchemeRegistry sr = ccm.getSchemeRegistry();  
            sr.register(new Scheme(SCHEMA, port, new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)));
            HttpClient httpClient = new DefaultHttpClient(ccm);
            httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, timeout);
            return httpClient;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public HttpGet buildGetMethod(String uri) {
        return buildGetMethod(uri, null, null);
    }
    
    public HttpGet buildGetMethod(String uri, Ticket4jHttpResponse response) {
        return buildGetMethod(uri, response, null);
    }
    
    public HttpGet buildGetMethod(String uri, Header[] headers) {
        return buildGetMethod(uri, null, headers);
    }
    
    public HttpGet buildGetMethod(String uri, Ticket4jHttpResponse response, Header[] headers) {
        HttpGet httpGet = new HttpGet(getUrl(uri));
        setRequestBaseHeader(httpGet);
        if (null != response) {
            setRequestCookie(httpGet, getCookieHeaders(response));
        }
        setRequestHeader(httpGet, headers);
        printHttpRequestHeader(httpGet);
        return httpGet;
    }
    
    
    public HttpPost buildPostMethod(String uri) {
        return buildPostMethod(uri, null, null);
    }
    
    public HttpPost buildPostMethod(String uri, Ticket4jHttpResponse response) {
        return buildPostMethod(uri, response, null);
    }
    
    public HttpPost buildPostMethod(String uri, Header[] headers) {
        return buildPostMethod(uri, null, headers);
    }
    
    public HttpPost buildPostMethod(String uri, Ticket4jHttpResponse response, Header[] headers) {
        HttpPost httpPost = new HttpPost(getUrl(uri));
        setRequestBaseHeader(httpPost);
        if (null != response) {
            setRequestCookie(httpPost, getCookieHeaders(response));
        }
        setRequestHeader(httpPost, headers);
        printHttpRequestHeader(httpPost);
        return httpPost;
    }

    private Header[] getCookieHeaders(Ticket4jHttpResponse response) {
        List<Header> cookies = Lists.newArrayList();
        Header[] responseHeaders = response.getHeaders();
        for (Header header : responseHeaders) {
            if (StringUtils.equals(header.getName(), "Set-Cookie")) {
                cookies.add(header);
            }
        }
        return cookies.toArray(new Header[0]);
    }
    
    private String getUrl(String uri) {
        return SCHEMA + "://" + host + uri;
    }
    
    private void setRequestBaseHeader(HttpRequestBase httpRequest) {
        httpRequest.setHeader("Connection", "keep-alive");
        httpRequest.setHeader("User-Agent", "Mozilla/5.0 (MSIE 9.0; Windows NT 6.1; Trident/5.0;)");
        httpRequest.setHeader("Accept-Encoding", "gzip,deflate,sdch");
        httpRequest.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
        httpRequest.setHeader("Host", host);
        httpRequest.setHeader("Origin", SCHEMA + "://" + host);
        httpRequest.setHeader("Cache-Control", "max-age=0");
    }
    
    private void setRequestHeader(HttpRequestBase httpRequest, Header[] headers) {
        if (ArrayUtils.isNotEmpty(headers)) {
            for (Header header : headers) {
                httpRequest.addHeader(header);
            }
        }
    }
    
    private void setRequestCookie(HttpRequestBase httpRequest, Header[] headers) {
        StringBuilder sb = new StringBuilder(128);
        if (ArrayUtils.isNotEmpty(headers)) {
            for (Header header : headers) {
                if (StringUtils.equals(header.getName(), "Set-Cookie")) {
                    String value = header.getValue();
                    if (StringUtils.isNotBlank(value)) {
                        // Because of the need to remove the effective range: path=/
                        String[] values = StringUtils.split(value, ";");
                        sb.append(values[0]).append(";");
                    }
                }
            }
        }
        httpRequest.addHeader("Cookie", sb.toString());
    }
    
    private void printHttpRequestHeader(HttpRequestBase httpRequestBase) {
        if (null == httpRequestBase)
            return;
        Header[] headers = httpRequestBase.getAllHeaders();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("===============Request Headers===============");
        printHeaders(headers);
    }
    
    public void printHttpResponseHeader(HttpResponse httpResponse) {
        if (null == httpResponse)
            return;
        Header[] headers = httpResponse.getAllHeaders();
        if (LOGGER.isDebugEnabled())
            LOGGER.debug("===============Response Headers===============");
        printHeaders(headers);
    }
    
    private void printHeaders(Header[] headers) {
        if (ArrayUtils.isEmpty(headers))
            return;
        for (Header header : headers) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(header.getName() + " = " + header.getValue());
            }
        }
    }
    
    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public int getTimeout() {
        return timeout;
    }
}
