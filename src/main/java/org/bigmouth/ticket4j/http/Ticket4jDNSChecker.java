package org.bigmouth.ticket4j.http;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.bigmouth.ticket4j.Ticket4jDefaults;
import org.bigmouth.ticket4j.utils.BaseLifeCycleSupport;
import org.bigmouth.ticket4j.utils.HttpClientUtils;
import org.bigmouth.ticket4j.utils.InetAddressUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

/**
 * 检测DNS速度
 * 
 * @author Allen.Hu / 2014-9-3
 */
public class Ticket4jDNSChecker extends BaseLifeCycleSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(Ticket4jDNSChecker.class);
    private static List<Ticket4jDns> MAPPINGS = Lists.newArrayList();

    private final Ticket4jHttpClient ticket4jHttpClient;
    private String[] addresses;
    private String uri = Ticket4jDefaults.URI;
    private String urlDnsResource = Ticket4jDefaults.URL_DNS_RESOURCE;
    private int limitConsumeTimeInMillis = Ticket4jDefaults.DEFAULT_LIMIT_CONSUME_TIME_IN_MILLIS;
    private long sleepTime = 300000;

    public Ticket4jDNSChecker(Ticket4jHttpClient ticket4jHttpClient) {
        Preconditions.checkNotNull(ticket4jHttpClient, "ticket4jHttpClient");
        this.ticket4jHttpClient = ticket4jHttpClient;
    }
    
    public static Ticket4jDns getTicket4jDns(DNSDistributeType dnsDistributeType) {
        synchronized (MAPPINGS) {
            if (CollectionUtils.isNotEmpty(MAPPINGS)) {
                switch (dnsDistributeType) {
                    case FAST:
                        return MAPPINGS.get(0);
                    case RANDOM:
                    default:
                        return MAPPINGS.get(RandomUtils.nextInt(MAPPINGS.size()));
                }
            }
            return null;
        }
    }

    @Override
    protected void doInit() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("正在获取远程DNS列表...");
        }
        initAddresses();
        
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("开始对DNS列表进行速度测试...");
            LOGGER.debug("{}", ArrayUtils.toString(addresses));
        }
        
        Executors.newFixedThreadPool(1, new ThreadFactory() {
            
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r);
            }
        }).submit(new Runnable() {
            
            @Override
            public void run() {
                if (ArrayUtils.isNotEmpty(addresses)) {
                    while (true) {
                        testSpeed();
                        if (CollectionUtils.isNotEmpty(MAPPINGS)) {
                            if (LOGGER.isDebugEnabled()) {
                                StringBuilder result = new StringBuilder(128);
                                result.append("当前可用的DNS服务数：").append(MAPPINGS.size()).append(" 个，响应速度：最快 ")
                                        .append(MAPPINGS.get(0).getConsumeTimeInMillis()).append(" ms、最慢 ")
                                        .append(MAPPINGS.get(MAPPINGS.size() - 1).getConsumeTimeInMillis())
                                        .append(" ms");
                                LOGGER.debug(result.toString());
                            }
                        }
                        try {
                            Thread.sleep(sleepTime);
                        }
                        catch (InterruptedException e) {
                        }
                    }
                }
            }

            private void testSpeed() {
                for (String ip : addresses) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("正在测试 {} 的速度...", ip);
                    }
                    Ticket4jDns dns = new Ticket4jDns();
                    HttpClient httpClient = ticket4jHttpClient.buildHttpClient(ip);
                    HttpGet httpGet = ticket4jHttpClient.buildGetMethod(uri);
                    long start = System.currentTimeMillis();
                    try {
                        HttpResponse httpResponse = httpClient.execute(httpGet);
                        long time = System.currentTimeMillis() - start;
                        int statusCode = httpResponse.getStatusLine().getStatusCode();
                        if (statusCode == 200 || statusCode == 302) {
                            // Success
                            dns.setIp(ip);
                            dns.setInetAddresses(InetAddressUtils.getByAddress(ip));
                            dns.setConsumeTimeInMillis(time);
                            dns.setTimeout(false);
                            if (LOGGER.isDebugEnabled()) {
                                LOGGER.debug("{} 响应成功，消耗时间 {} ms...", ip, time);
                            }
                            if (time <= limitConsumeTimeInMillis) {
                                if (!MAPPINGS.contains(dns)) {
                                    MAPPINGS.add(dns);
                                    Collections.sort(MAPPINGS, new Ticket4jDns());
                                }
                            }
                        }
                        else {
                            if (LOGGER.isDebugEnabled()) {
                                LOGGER.debug("{} 响应失败，错误代码 {}...", ip, statusCode);
                            }
                        }
                    }
                    catch (ClientProtocolException e) {
                        // Ignore
                    }
                    catch (IOException e) {
                        // Ignore
                    }
                }
            }
        });
    }
    
    private void initAddresses() {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet get = new HttpGet(urlDnsResource);
        try {
            HttpResponse httpResponse = httpClient.execute(get);
            String body = HttpClientUtils.getResponseBody(httpResponse);
            if (StringUtils.isNotBlank(body)) {
                addresses = StringUtils.split(body, ",");
            }
        }
        catch (Exception e) {
            LOGGER.error("获取远程DNS列表失败!错误原因：{}", e.getMessage());
        }
    }

    @Override
    protected void doDestroy() {
        MAPPINGS.clear();
    }
    
    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setLimitConsumeTimeInMillis(int limitConsumeTimeInMillis) {
        this.limitConsumeTimeInMillis = limitConsumeTimeInMillis;
    }

    public void setSleepTime(long sleepTime) {
        this.sleepTime = sleepTime;
    }

    public void setUrlDnsResource(String urlDnsResource) {
        this.urlDnsResource = urlDnsResource;
    }
}
