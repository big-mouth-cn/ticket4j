package org.bigmouth.ticket4j;

import java.io.File;

import org.bigmouth.ticket4j.entity.Response;
import org.bigmouth.ticket4j.entity.Token;
import org.bigmouth.ticket4j.http.Ticket4jHttpResponse;


public interface PassCode {

    File getPassCode(Ticket4jHttpResponse response);
    
    Response checkLogin(Ticket4jHttpResponse response, String passCode);
    
    Response checkOrder(Ticket4jHttpResponse response, String passCode, Token token);
    
    Response check(Ticket4jHttpResponse response, String type, String passCode, Token token);
}
