package org.bigmouth.ticket4j;

import java.util.Scanner;

import org.bigmouth.framework.core.SpringContextHolder;
import org.bigmouth.ticket4j.entity.CheckPassCodeResponse;
import org.bigmouth.ticket4j.entity.Response;
import org.bigmouth.ticket4j.http.Ticket4jHttpResponse;
import org.bigmouth.ticket4j.utils.JVMUtils;


public class TicketStartup {

    public static void main(String[] args) {
        JVMUtils.bootUsingSpring(new String[] {
                "config/applicationContext.xml"
        }, args);
        
        Initialize initialize = SpringContextHolder.getBean("initialize");
        PassCode passCode = SpringContextHolder.getBean("passCode");
        User user = SpringContextHolder.getBean("user");
        
        Ticket4jHttpResponse response = initialize.init();
        
        byte[] code = null;
        Response checkResponse = new CheckPassCodeResponse();
        while (!checkResponse.isContinue()) {
            passCode.getPassCode(response);
            Scanner scanner = new Scanner(System.in);
            code = scanner.next().getBytes();
            checkResponse = passCode.checkLogin(response, new String(code));
        }
        
        Response login = user.login(new String(code), response);
        if (login.isContinue()) {
            
        }
    }
}
