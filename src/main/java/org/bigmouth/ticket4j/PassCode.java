package org.bigmouth.ticket4j;

import java.io.File;

import org.bigmouth.ticket4j.entity.Token;
import org.bigmouth.ticket4j.entity.response.CheckPassCodeResponse;
import org.bigmouth.ticket4j.http.Ticket4jHttpResponse;


public interface PassCode {

    File getLoginPassCode(Ticket4jHttpResponse response);
    
    File getOrderPassCode(Ticket4jHttpResponse response);
    
    CheckPassCodeResponse checkLogin(Ticket4jHttpResponse response, String passCode);
    
    CheckPassCodeResponse checkOrder(Ticket4jHttpResponse response, String passCode, Token token);
    
    CheckPassCodeResponse check(Ticket4jHttpResponse response, String type, String passCode, Token token);
}
