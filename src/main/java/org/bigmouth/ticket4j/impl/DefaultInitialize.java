package org.bigmouth.ticket4j.impl;

import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.bigmouth.ticket4j.Initialize;
import org.bigmouth.ticket4j.Ticket4jDefaults;
import org.bigmouth.ticket4j.http.Ticket4jHeader;
import org.bigmouth.ticket4j.http.Ticket4jHttpClient;
import org.bigmouth.ticket4j.http.Ticket4jHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;


public class DefaultInitialize extends AccessSupport implements Initialize {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultInitialize.class);
    private String uriInit = Ticket4jDefaults.URI_INIT;
    
    public DefaultInitialize(Ticket4jHttpClient ticket4jHttpClient) {
        super(ticket4jHttpClient);
    }

    @Override
    public Ticket4jHttpResponse init() {
        HttpClient httpClient = ticket4jHttpClient.buildHttpClient();
        HttpGet get = ticket4jHttpClient.buildGetMethod(uriInit);
        Ticket4jHttpResponse response = new Ticket4jHttpResponse();
        try {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("正在初始化...");
            }
            HttpResponse httpResponse = httpClient.execute(get);
            
            Header[] allHeaders = httpResponse.getAllHeaders();
            List<Header> headers = Lists.newArrayList();
            for (Header header : allHeaders) {
                headers.add(new Ticket4jHeader(header));
            }
            response.setHeaders(headers.toArray(new Ticket4jHeader[0]));
            
            return response;
        }
        catch (Exception e) {
            LOGGER.error("初始化失败,错误原因：", e.getMessage());
        }
        finally {
            httpClient.getConnectionManager().shutdown();
        }
        return null;
    }

    public void setUriInit(String uriInit) {
        this.uriInit = uriInit;
    }
}
