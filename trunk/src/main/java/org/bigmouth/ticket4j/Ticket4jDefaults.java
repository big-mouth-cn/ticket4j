package org.bigmouth.ticket4j;


public final class Ticket4jDefaults {

    public static final String UTF_8 = "UTF-8";
    public static final String DEFAULT_CHARSET = UTF_8;
    
    public static final String SCHEMA = "https";
    public static final String HOST = "kyfw.12306.cn";
    public static final int PORT = 443;
    public static final int TIME_OUT = 30000;
    
    public static final int BUFFER = 1024;
    
    public static final String PURPOSE_CODE_ADULT = "ADULT";

    public static final String PATH_TMP = System.getProperty("java.io.tmpdir");
    public static final String IMAGE_SUFFIX = ".png";
    
    public static final String URI_INIT = "/otn/login/init.do";
    public static final String URI_GET_PASSCODE = "/otn/passcodeNew/getPassCodeNew.do?module=login&rand=sjrand";
    public static final String URI_CHECK_PASSCODE = "/otn/passcodeNew/checkRandCodeAnsyn";
    public static final String URI_LOGIN = "/otn/login/loginAysnSuggest";
    public static final String URI_QUERY_TICKETS = "/otn/leftTicket/query.do";
    
    private Ticket4jDefaults() {
    }
}
