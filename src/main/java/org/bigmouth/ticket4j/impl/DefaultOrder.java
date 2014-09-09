package org.bigmouth.ticket4j.impl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.bigmouth.framework.util.DateUtils;
import org.bigmouth.ticket4j.Order;
import org.bigmouth.ticket4j.Ticket4jDefaults;
import org.bigmouth.ticket4j.entity.Response;
import org.bigmouth.ticket4j.entity.Seat;
import org.bigmouth.ticket4j.entity.Token;
import org.bigmouth.ticket4j.entity.request.CheckOrderInfoRequest;
import org.bigmouth.ticket4j.entity.request.ConfirmSingleForQueueRequest;
import org.bigmouth.ticket4j.entity.request.QueueCountRequest;
import org.bigmouth.ticket4j.entity.request.SubmitOrderRequest;
import org.bigmouth.ticket4j.entity.response.CheckOrderInfoResponse;
import org.bigmouth.ticket4j.entity.response.ConfirmSingleForQueueResponse;
import org.bigmouth.ticket4j.entity.response.NoCompleteOrderResponse;
import org.bigmouth.ticket4j.entity.response.OrderWaitTimeResponse;
import org.bigmouth.ticket4j.entity.response.QueueCountResponse;
import org.bigmouth.ticket4j.entity.response.SubmitOrderResponse;
import org.bigmouth.ticket4j.entity.train.Train;
import org.bigmouth.ticket4j.entity.train.TrainDetails;
import org.bigmouth.ticket4j.http.Ticket4jHttpClient;
import org.bigmouth.ticket4j.http.Ticket4jHttpResponse;
import org.bigmouth.ticket4j.utils.HttpClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.google.common.base.Preconditions;


public class DefaultOrder extends AccessSupport implements Order {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultOrder.class);

    private String uriSubmitOrder = Ticket4jDefaults.URI_SUBMIT_ORDER;
    private String uriInitDc = Ticket4jDefaults.URI_INIT_DC;
    private String uriCheckOrderInfo = Ticket4jDefaults.URI_CHECK_ORDER_INFO;
    private String uriGetQueueCount = Ticket4jDefaults.URI_GET_QUEUE_COUNT;
    private String uriConfirmSingleForQueue = Ticket4jDefaults.URI_CONFIRM_SINGLE_FOR_QUEUE;
    private String uriQueryNoComplete = Ticket4jDefaults.URI_QUERY_NO_COMPLETE;
    private String uriQueryOrderWaitTime = Ticket4jDefaults.URI_QUERY_ORDER_WAIT_TIME;
    
    public DefaultOrder(Ticket4jHttpClient ticket4jHttpClient) {
        super(ticket4jHttpClient);
    }

    @Override
    public Response submit(Ticket4jHttpResponse ticket4jHttpResponse, SubmitOrderRequest orderRequest) {
        HttpClient httpClient = ticket4jHttpClient.buildHttpClient();
        HttpPost post = ticket4jHttpClient.buildPostMethod(uriSubmitOrder, ticket4jHttpResponse);
        try {
            Preconditions.checkNotNull(orderRequest, "提交订单参数有误，请检查!");
            Train train = orderRequest.getTrain();
            Preconditions.checkNotNull(train, "提交订单参数中的车次信息有误，请检查!");
            TrainDetails details = train.getQueryLeftNewDTO();
            List<Seat> seats = details.getCanBuySeats();
            if (CollectionUtils.isEmpty(seats)) {
                throw new IllegalArgumentException("没有可预订的席别!");
            }
            String trainName = details.getStation_train_code();
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("正在预订 {} 的车票...", trainName);
            }
            
            addPair(post, new NameValuePair[] {
                    new BasicNameValuePair("secretStr", train.getSecretStr()),
                    new BasicNameValuePair("train_date", orderRequest.getTrainDate()),
                    new BasicNameValuePair("back_train_date", orderRequest.getTrainDate()),
                    new BasicNameValuePair("tour_flag", orderRequest.getTourFlag()),
                    new BasicNameValuePair("purpose_codes", orderRequest.getPurposeCodes()),
                    new BasicNameValuePair("query_from_station_name", details.getFrom_station_name()),
                    new BasicNameValuePair("query_to_station_name", details.getTo_station_name())
            });
            
            HttpResponse httpResponse = httpClient.execute(post);
            String body = HttpClientUtils.getResponseBody(httpResponse);
            Response response = fromJson(body, SubmitOrderResponse.class);
            return response;
        }
        catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        finally {
            httpClient.getConnectionManager().shutdown();
        }
        return null;
    }
    
    @Override
    public Token getToken(Ticket4jHttpResponse ticket4jHttpResponse) {
        HttpClient httpClient = ticket4jHttpClient.buildHttpClient();
        HttpGet get = ticket4jHttpClient.buildGetMethod(uriInitDc, ticket4jHttpResponse);
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("正在获取 Token 及令牌...");
            }
            HttpResponse httpResponse = httpClient.execute(get);
            Token token = HttpClientUtils.getResponseBodyAsToken(httpResponse);
            return token;
        }
        catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return null;
    }

    @Override
    public CheckOrderInfoResponse checkOrderInfo(Ticket4jHttpResponse ticket4jHttpResponse,
            CheckOrderInfoRequest infoRequest) {
        HttpClient httpClient = ticket4jHttpClient.buildHttpClient();
        HttpPost post = ticket4jHttpClient.buildPostMethod(uriCheckOrderInfo, ticket4jHttpResponse, new Header[] {
                new BasicHeader("Accept", "text/json, application/json, */*; q=0.01"),
                new BasicHeader("X-Requested-With", "XMLHttpRequest")
        });
        try {
            Preconditions.checkNotNull(infoRequest, "订单信息不完整，请检查!");
            addPair(post, new NameValuePair[] {
                    new BasicNameValuePair("cancel_flag", String.valueOf(infoRequest.getCancelFlag())),
                    new BasicNameValuePair("bed_level_order_num", infoRequest.getBedLevelOrderNum()),
                    new BasicNameValuePair("passengerTicketStr", infoRequest.getPassengerTicketStr()),
                    new BasicNameValuePair("oldPassengerStr", infoRequest.getOldPassengerStr()),
                    new BasicNameValuePair("tour_flag", infoRequest.getTourFlag()),
                    new BasicNameValuePair("randCode", infoRequest.getRandCode()),
                    new BasicNameValuePair("_json_att", ""),
                    new BasicNameValuePair("REPEAT_SUBMIT_TOKEN", infoRequest.getRepeatSubmitToken())
            });
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("正在验证订单完整性...");
            }
            HttpResponse httpResponse = httpClient.execute(post);
            String body = HttpClientUtils.getResponseBody(httpResponse);
            CheckOrderInfoResponse response = fromJson(body, CheckOrderInfoResponse.class);
            return response;
        }
        catch (Exception e) {
            LOGGER.error("检查订单信息失败,错误原因：{}", e.getMessage());
        }
        return null;
    }

    @Override
    public QueueCountResponse getQueueCount(Ticket4jHttpResponse ticket4jHttpResponse, QueueCountRequest forQueueRequest) {
        HttpClient httpClient = ticket4jHttpClient.buildHttpClient();
        HttpPost post = ticket4jHttpClient.buildPostMethod(uriGetQueueCount, ticket4jHttpResponse);
        try {
            TrainDetails trainDetails = forQueueRequest.getTrainDetails();
            addPair(post, new NameValuePair[] {
                    new BasicNameValuePair("train_date", DateUtils.convertString2Date(forQueueRequest.getTrainDate(), "yyyy-MM-dd").toString()),
                    new BasicNameValuePair("train_no", trainDetails.getTrain_no()),
                    new BasicNameValuePair("stationTrainCode", trainDetails.getStation_train_code()),
                    new BasicNameValuePair("seatType", trainDetails.getSeat_types()),
                    new BasicNameValuePair("fromStationTelecode", trainDetails.getFrom_station_telecode()),
                    new BasicNameValuePair("toStationTelecode", trainDetails.getTo_station_telecode()),
                    new BasicNameValuePair("leftTicket", trainDetails.getYp_info()),
                    new BasicNameValuePair("purpose_codes", "00"),
                    new BasicNameValuePair("REPEAT_SUBMIT_TOKEN", forQueueRequest.getToken().getToken())
            });
            HttpResponse httpResponse = httpClient.execute(post);
            String body = HttpClientUtils.getResponseBody(httpResponse);
            QueueCountResponse response = fromJson(body, QueueCountResponse.class);
            if (!response.isStatus())
                throw new RuntimeException(response.getMessage());
            return response;
        }
        catch (Exception e) {
            LOGGER.error("查询排队信息失败,错误原因： {}", e.getMessage());
        }
        return null;
    }
    
    @Override
    public ConfirmSingleForQueueResponse confirm(Ticket4jHttpResponse ticket4jHttpResponse,
            ConfirmSingleForQueueRequest forQueueRequest) {
        HttpClient httpClient = ticket4jHttpClient.buildHttpClient();
        HttpPost post = ticket4jHttpClient.buildPostMethod(uriConfirmSingleForQueue, ticket4jHttpResponse);
        try {
            Preconditions.checkNotNull(forQueueRequest, "订单信息不完整，请检查!");
            addPair(post, new NameValuePair[] {
                    new BasicNameValuePair("passengerTicketStr", forQueueRequest.getPassengerTicketStr()),
                    new BasicNameValuePair("oldPassengerStr", forQueueRequest.getOldPassengerStr()),
                    new BasicNameValuePair("randCode", forQueueRequest.getRandCode()),
                    new BasicNameValuePair("purpose_codes", forQueueRequest.getPurposeCodes()),
                    new BasicNameValuePair("key_check_isChange", forQueueRequest.getKeyCheckIsChange()),
                    new BasicNameValuePair("leftTicketStr", forQueueRequest.getLeftTicketStr()),
                    new BasicNameValuePair("train_location", forQueueRequest.getTrainLocation()),
                    new BasicNameValuePair("REPEAT_SUBMIT_TOKEN", forQueueRequest.getRepeatSubmitToken())
            });
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("正在提交车票订单，请稍候...");
            }
            HttpResponse httpResponse = httpClient.execute(post);
            String body = HttpClientUtils.getResponseBody(httpResponse);
            ConfirmSingleForQueueResponse response = fromJson(body, ConfirmSingleForQueueResponse.class);
            return response;
        }
        catch (Exception e) {
            LOGGER.error("提交车票订单失败,错误原因： {}", e.getMessage());
        }
        return null;
    }

    @Override
    public OrderWaitTimeResponse queryOrderWaitTime(Ticket4jHttpResponse ticket4jHttpResponse, Token token) {
        HttpClient httpClient = ticket4jHttpClient.buildHttpClient();
        HttpGet get = ticket4jHttpClient.buildGetMethod(uriQueryOrderWaitTime, ticket4jHttpResponse);
        try {
            addPair(get, new NameValuePair[] {
                    new BasicNameValuePair("random", String.valueOf(RandomUtils.nextDouble())),
                    new BasicNameValuePair("tourFlag", Ticket4jDefaults.TOUR_FLAG_DC),
                    new BasicNameValuePair("REPEAT_SUBMIT_TOKEN", token.getToken())
            });
            HttpResponse response = httpClient.execute(get);
            String body = HttpClientUtils.getResponseBody(response);
            OrderWaitTimeResponse waitTime = fromJson(body, OrderWaitTimeResponse.class);
            return waitTime;
        }
        catch (URISyntaxException e) {
            LOGGER.error("等待订单结果失败,错误原因： {}", e.getMessage());
        }
        catch (ClientProtocolException e) {
            LOGGER.error("等待订单结果失败,错误原因： {}", e.getMessage());
        }
        catch (IOException e) {
            LOGGER.error("等待订单结果失败,错误原因： {}", e.getMessage());
        }
        return null;
    }

    @Override
    public NoCompleteOrderResponse queryNoComplete(Ticket4jHttpResponse ticket4jHttpResponse) {
        HttpClient httpClient = ticket4jHttpClient.buildHttpClient();
        HttpGet get = ticket4jHttpClient.buildGetMethod(uriQueryNoComplete, ticket4jHttpResponse);
        try {
            HttpResponse httpResponse = httpClient.execute(get);
            String body = HttpClientUtils.getResponseBody(httpResponse);
            return fromJson(body, NoCompleteOrderResponse.class);
        }
        catch (Exception e) {
            LOGGER.error("查询未完成订单失败,错误原因： {}", e.getMessage());
        }
        return null;
    }

    public void setUriSubmitOrder(String uriSubmitOrder) {
        this.uriSubmitOrder = uriSubmitOrder;
    }

    public void setUriInitDc(String uriInitDc) {
        this.uriInitDc = uriInitDc;
    }

    public void setUriCheckOrderInfo(String uriCheckOrderInfo) {
        this.uriCheckOrderInfo = uriCheckOrderInfo;
    }

    public void setUriConfirmSingleForQueue(String uriConfirmSingleForQueue) {
        this.uriConfirmSingleForQueue = uriConfirmSingleForQueue;
    }

    public void setUriQueryNoComplete(String uriQueryNoComplete) {
        this.uriQueryNoComplete = uriQueryNoComplete;
    }

    public void setUriQueryOrderWaitTime(String uriQueryOrderWaitTime) {
        this.uriQueryOrderWaitTime = uriQueryOrderWaitTime;
    }

    public void setUriGetQueueCount(String uriGetQueueCount) {
        this.uriGetQueueCount = uriGetQueueCount;
    }
}
