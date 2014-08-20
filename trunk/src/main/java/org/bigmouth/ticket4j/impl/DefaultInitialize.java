package org.bigmouth.ticket4j.impl;

import java.io.IOException;
import java.net.InetAddress;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.bigmouth.ticket4j.Initialize;
import org.bigmouth.ticket4j.Ticket4jDefaults;
import org.bigmouth.ticket4j.http.Ticket4jHttpClient;
import org.bigmouth.ticket4j.http.Ticket4jHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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
            StringBuilder addr = new StringBuilder(64);
            InetAddress[] inetAddresses = InetAddress.getAllByName(ticket4jHttpClient.getHost());
            addr.append("当前DNS服务器地址：");
            for (InetAddress inetAddress : inetAddresses) {
                addr.append(inetAddress).append("|");
            }
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(addr.toString());
            }
            
            HttpResponse httpResponse = httpClient.execute(get);
            ticket4jHttpClient.printHttpResponseHeader(httpResponse);
            response.setHeaders(httpResponse.getAllHeaders());
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("初始化完成!");
            }
        }
        catch (ClientProtocolException e) {
            LOGGER.error("init: ", e);
        }
        catch (IOException e) {
            LOGGER.error("init: ", e);
        }
        finally {
            httpClient.getConnectionManager().shutdown();
        }
        return response;
    }
}
