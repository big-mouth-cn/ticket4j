package org.bigmouth.ticket4j.impl;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.bigmouth.framework.util.PathUtils;
import org.bigmouth.ticket4j.PassCode;
import org.bigmouth.ticket4j.Ticket4jDefaults;
import org.bigmouth.ticket4j.entity.Response;
import org.bigmouth.ticket4j.entity.Token;
import org.bigmouth.ticket4j.entity.response.CheckPassCodeResponse;
import org.bigmouth.ticket4j.http.Ticket4jHttpClient;
import org.bigmouth.ticket4j.http.Ticket4jHttpResponse;
import org.bigmouth.ticket4j.utils.HttpClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultPassCode extends AccessSupport implements PassCode {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultPassCode.class);

    public static final String TYPE_LOGIN = "sjrand";
    public static final String TYPE_ORDER = "randp";

    private String imgDir = Ticket4jDefaults.PATH_TMP;

    private String uriGetLoginPassCode = Ticket4jDefaults.URI_GET_LOGIN_PASSCODE;
    private String uriGetOrderPassCode = Ticket4jDefaults.URI_GET_ORDER_PASSCODE;
    private String uriCheckPassCode = Ticket4jDefaults.URI_CHECK_PASSCODE;
    
    public enum PassCodeType {
        LOGIN, ORDER
    }

    public DefaultPassCode(Ticket4jHttpClient ticket4jHttpClient) {
        super(ticket4jHttpClient);
    }

    @Override
    public File getOrderPassCode(Ticket4jHttpResponse response) {
        return getPassCode(response, PassCodeType.ORDER);
    }

    @Override
    public File getLoginPassCode(Ticket4jHttpResponse response) {
        return getPassCode(response, PassCodeType.LOGIN);
    }

    private File getPassCode(Ticket4jHttpResponse response, PassCodeType codeType) {
        HttpClient httpClient = ticket4jHttpClient.buildHttpClient();
        String uri = codeType == PassCodeType.LOGIN ? uriGetLoginPassCode : uriGetOrderPassCode;
        HttpGet httpGet = ticket4jHttpClient.buildGetMethod(uri, response, new Header[] { 
                new BasicHeader("Accept", "image/webp,*/*;q=0.8") 
        });
        try {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("正在加载验证码...");
            }
            HttpResponse httpResponse = httpClient.execute(httpGet);
            String path = new StringBuilder(128).append(PathUtils.appendEndFileSeparator(imgDir))
                    .append(System.currentTimeMillis()).append(Ticket4jDefaults.IMAGE_SUFFIX).toString();
            File image = HttpClientUtils.getResponseBodyAsFile(httpResponse, path);
            if (null == image) {
                throw new IOException("验证码下载失败!");
            }
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("验证码下载完成，文件地址：{}", image.getPath());
            }
            return image;
        }
        catch (Exception e) {
            LOGGER.error("getPassCode: ", e);
        }
        finally {
            httpClient.getConnectionManager().shutdown();
        }
        return null;
    }

    @Override
    public Response checkLogin(Ticket4jHttpResponse response, String passCode) {
        return check(response, TYPE_LOGIN, passCode, null);
    }

    @Override
    public Response checkOrder(Ticket4jHttpResponse response, String passCode, Token token) {
        return check(response, TYPE_ORDER, passCode, token);
    }

    @Override
    public Response check(Ticket4jHttpResponse response, String type, String passCode, Token token) {
        HttpClient httpClient = ticket4jHttpClient.buildHttpClient();
        HttpPost post = ticket4jHttpClient.buildPostMethod(uriCheckPassCode, response);
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        pairs.add(new BasicNameValuePair("randCode", passCode));
        pairs.add(new BasicNameValuePair("rand", type));
        if (null != token) {
            pairs.add(new BasicNameValuePair("_json_att", ""));
            pairs.add(new BasicNameValuePair("REPEAT_SUBMIT_TOKEN", token.getToken()));
        }
        try {
            post.setEntity(new UrlEncodedFormEntity(pairs, Ticket4jDefaults.DEFAULT_CHARSET));
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("正在认证验证码是否正确...");
            }
            HttpResponse httpResponse = httpClient.execute(post);
            String body = HttpClientUtils.getResponseBody(httpResponse);
            CheckPassCodeResponse result = fromJson(body, CheckPassCodeResponse.class);
            
            if (!result.isContinue()) {
                if (LOGGER.isWarnEnabled()) {
                    LOGGER.warn("验证码不正确!");
                }
                result.printMessage();
            }
            return result;
        }
        catch (UnsupportedEncodingException e) {
            LOGGER.error("check: ", e);
        }
        catch (ClientProtocolException e) {
            LOGGER.error("check: ", e);
        }
        catch (IOException e) {
            LOGGER.error("check: ", e);
        }
        return null;
    }

    public void setImgDir(String imgDir) {
        if (StringUtils.isNotBlank(imgDir)) {
            File dir = new File(imgDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            this.imgDir = imgDir;
        }
    }

    public void setUriCheckPassCode(String uriCheckPassCode) {
        this.uriCheckPassCode = uriCheckPassCode;
    }

    public void setUriGetLoginPassCode(String uriGetLoginPassCode) {
        this.uriGetLoginPassCode = uriGetLoginPassCode;
    }
    
    public void setUriGetOrderPassCode(String uriGetOrderPassCode) {
        this.uriGetOrderPassCode = uriGetOrderPassCode;
    }
}
