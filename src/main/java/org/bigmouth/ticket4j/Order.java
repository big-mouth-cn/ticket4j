package org.bigmouth.ticket4j;

import org.bigmouth.ticket4j.entity.Response;
import org.bigmouth.ticket4j.entity.Token;
import org.bigmouth.ticket4j.entity.request.CheckOrderInfoRequest;
import org.bigmouth.ticket4j.entity.request.ConfirmSingleForQueueRequest;
import org.bigmouth.ticket4j.entity.request.SubmitOrderRequest;
import org.bigmouth.ticket4j.entity.response.CheckOrderInfoResponse;
import org.bigmouth.ticket4j.entity.response.ConfirmSingleForQueueResponse;
import org.bigmouth.ticket4j.entity.response.NoCompleteOrderResponse;
import org.bigmouth.ticket4j.entity.response.OrderWaitTimeResponse;
import org.bigmouth.ticket4j.http.Ticket4jHttpResponse;


public interface Order {

    Response submit(Ticket4jHttpResponse ticket4jHttpResponse, SubmitOrderRequest submitOrderRequest);
    
    Token getToken(Ticket4jHttpResponse ticket4jHttpResponse);
    
    CheckOrderInfoResponse checkOrderInfo(Ticket4jHttpResponse ticket4jHttpResponse, CheckOrderInfoRequest infoRequest);
    
    ConfirmSingleForQueueResponse confirm(Ticket4jHttpResponse ticket4jHttpResponse, ConfirmSingleForQueueRequest forQueueRequest);
    
    NoCompleteOrderResponse queryNoComplete(Ticket4jHttpResponse ticket4jHttpResponse);
    
    OrderWaitTimeResponse queryOrderWaitTime(Ticket4jHttpResponse ticket4jHttpResponse, Token token);
}
