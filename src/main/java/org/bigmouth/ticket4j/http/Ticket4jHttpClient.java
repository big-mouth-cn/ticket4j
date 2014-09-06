package org.bigmouth.ticket4j.http;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
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
import org.apache.http.conn.ClientConnectionOperator;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.BasicClientConnectionManager;
import org.apache.http.impl.conn.DefaultClientConnectionOperator;
import org.apache.http.params.CoreConnectionPNames;
import org.bigmouth.ticket4j.Ticket4jDefaults;
import org.bigmouth.ticket4j.utils.InetAddressUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;


public final class Ticket4jHttpClient {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Ticket4jHttpClient.class);
    private static final String SCHEMA = Ticket4jDefaults.SCHEMA;
    
    private String host = Ticket4jDefaults.HOST;
    private int port = Ticket4jDefaults.PORT;
    private int timeout = Ticket4jDefaults.TIME_OUT;
    private DNSDistributeType dnsDistributeType = DNSDistributeType.RANDOM;
    
    public HttpClient buildHttpClient() {
        return buildHttpClient(true);
    }

    public HttpClient buildHttpClient(final boolean changeInetAddress) {
        try {
            SSLContext ctx = getInstance();
            ClientConnectionManager ccm = new BasicClientConnectionManager() {

                @Override
                protected ClientConnectionOperator createConnectionOperator(SchemeRegistry schreg) {
                    if (changeInetAddress) {
                        return new DefaultClientConnectionOperator(schreg, new DnsResolver() {
                            
                            @Override
                            public InetAddress[] resolve(String host) throws UnknownHostException {
                                Ticket4jDns ticket4jDns = Ticket4jDNSChecker.getTicket4jDns(dnsDistributeType);
                                if (null == ticket4jDns) {
                                    return InetAddress.getAllByName(host);
                                }
                                InetAddress[] inetAddresses = ticket4jDns.getInetAddresses();
                                if (ArrayUtils.isEmpty(inetAddresses))
                                    return InetAddress.getAllByName(host);
                                if (LOGGER.isDebugEnabled()) {
                                    LOGGER.debug("CHANGE DNS ----> {}", ArrayUtils.toString(inetAddresses));
                                }
                                return inetAddresses;
                            }
                        });
                    }
                    return super.createConnectionOperator(schreg);
                }
            };
            return getHttpClient(ctx, ccm);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public HttpClient buildHttpClient(final String dnsIp) {
        try {
            SSLContext ctx = getInstance();
            ClientConnectionManager ccm = new BasicClientConnectionManager() {

                @Override
                protected ClientConnectionOperator createConnectionOperator(SchemeRegistry schreg) {
                    return new DefaultClientConnectionOperator(schreg, new DnsResolver() {

                        @Override
                        public InetAddress[] resolve(String host) throws UnknownHostException {
                            return InetAddressUtils.getByAddress(dnsIp);
                        }
                    });
                }
            };
            return getHttpClient(ctx, ccm);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private HttpClient getHttpClient(SSLContext ctx, ClientConnectionManager ccm) {
        SchemeRegistry sr = ccm.getSchemeRegistry();
        sr.register(new Scheme(SCHEMA, getPort(), new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)));
        HttpClient httpClient = new DefaultHttpClient(ccm);
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, getTimeout());
        return httpClient;
    }
    
    private SSLContext getInstance() throws NoSuchAlgorithmException, KeyManagementException {
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
        return ctx;
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
    
    public static void printHttpRequestHeader(HttpRequestBase httpRequestBase) {
        if (null == httpRequestBase)
            return;
        Header[] headers = httpRequestBase.getAllHeaders();
        if (LOGGER.isInfoEnabled())
            LOGGER.info("===============Request Headers===============");
        printHeaders(headers);
    }
    
    public static void printHttpResponseHeader(HttpResponse httpResponse) {
        if (null == httpResponse)
            return;
        Header[] headers = httpResponse.getAllHeaders();
        if (LOGGER.isInfoEnabled())
            LOGGER.info("===============Response Headers===============");
        printHeaders(headers);
    }
    
    public static void printHeaders(Header[] headers) {
        if (ArrayUtils.isEmpty(headers))
            return;
        for (Header header : headers) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(header.getName() + " = " + header.getValue());
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

    public void setDnsDistributeType(DNSDistributeType dnsDistributeType) {
        this.dnsDistributeType = dnsDistributeType;
    }
}
