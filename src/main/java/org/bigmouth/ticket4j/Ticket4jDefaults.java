package org.bigmouth.ticket4j;

import org.bigmouth.framework.util.PathUtils;


public final class Ticket4jDefaults {

    public static final String UTF_8 = "UTF-8";
    public static final String DEFAULT_CHARSET = UTF_8;
    
    public static final String SCHEMA = "https";
    public static final String HOST = "kyfw.12306.cn";
    public static final int PORT = 443;
    public static final int TIME_OUT = 30000;
    
    public static final int DEFAULT_LIMIT_CONSUME_TIME_IN_MILLIS = 1000;
    
    public static final int BUFFER = 1024;
    
    public static final String TOUR_FLAG_DC = "dc";
    public static final String PURPOSE_CODE_ADULT = "ADULT";
    public static final String PURPOSE_CODE_00 = "00";

    public static final String PATH_TMP = System.getProperty("java.io.tmpdir");
    public static final String PATH_COOKIE_DIRECTORY = PathUtils.appendEndFileSeparator(Ticket4jDefaults.PATH_TMP) + "bigmouth-ticket4j/cookies";
    public static final String PATH_ORDER_DIRECTORY = PathUtils.appendEndFileSeparator(PATH_TMP) + "bigmouth-ticket4j/orders";
    public static final String IMAGE_SUFFIX = ".png";
    
    public static final String URI = "/otn";
    public static final String URI_INIT = "/otn/login/init.do";
    public static final String URI_GET_LOGIN_PASSCODE = "/otn/passcodeNew/getPassCodeNew.do?module=login&rand=sjrand";
    public static final String URI_GET_ORDER_PASSCODE = "/otn/passcodeNew/getPassCodeNew.do?module=passenger&rand=randp";
    public static final String URI_CHECK_PASSCODE = "/otn/passcodeNew/checkRandCodeAnsyn";
    public static final String URI_LOGIN = "/otn/login/loginAysnSuggest";
    public static final String URI_CHECK_USER = "/otn/login/checkUser";
    public static final String URI_QUERY_TICKETS = "/otn/leftTicket/query";
    public static final String URI_PASSENGERS_QUERY = "/otn/passengers/query";
    public static final String URI_SUBMIT_ORDER = "/otn/leftTicket/submitOrderRequest";
    public static final String URI_INIT_DC = "/otn/confirmPassenger/initDc";
    public static final String URI_CHECK_ORDER_INFO = "/otn/confirmPassenger/checkOrderInfo";
    public static final String URI_GET_QUEUE_COUNT = "/otn/confirmPassenger/getQueueCount";
    public static final String URI_CONFIRM_SINGLE_FOR_QUEUE = "/otn/confirmPassenger/confirmSingleForQueue";
    public static final String URI_QUERY_ORDER_WAIT_TIME = "/otn/confirmPassenger/queryOrderWaitTime";
    public static final String URI_QUERY_NO_COMPLETE = "/otn/queryOrder/queryMyOrderNoComplete";
    
    public static final String URL_REPORT = "http://www.big-mouth.cn/ticket4j/report.shtml";
    public static final String URL_DNS_RESOURCE = "http://www.big-mouth.cn/DNS.shtml";
    
    private Ticket4jDefaults() {
    }
}
