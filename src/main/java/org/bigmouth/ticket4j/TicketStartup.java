package org.bigmouth.ticket4j;

import java.util.List;
import java.util.Scanner;

import org.bigmouth.framework.core.SpringContextHolder;
import org.bigmouth.ticket4j.entity.Person;
import org.bigmouth.ticket4j.entity.Response;
import org.bigmouth.ticket4j.entity.Seat;
import org.bigmouth.ticket4j.entity.Token;
import org.bigmouth.ticket4j.entity.request.CheckOrderInfoRequest;
import org.bigmouth.ticket4j.entity.request.ConfirmSingleForQueueRequest;
import org.bigmouth.ticket4j.entity.request.QueryTicketRequest;
import org.bigmouth.ticket4j.entity.request.SubmitOrderRequest;
import org.bigmouth.ticket4j.entity.response.CheckOrderInfoResponse;
import org.bigmouth.ticket4j.entity.response.CheckPassCodeResponse;
import org.bigmouth.ticket4j.entity.response.ConfirmSingleForQueueResponse;
import org.bigmouth.ticket4j.entity.response.NoCompleteOrderResponse;
import org.bigmouth.ticket4j.entity.response.QueryTicketResponse;
import org.bigmouth.ticket4j.entity.train.Train;
import org.bigmouth.ticket4j.http.Ticket4jHttpResponse;
import org.bigmouth.ticket4j.utils.JVMUtils;
import org.bigmouth.ticket4j.utils.PersonUtils;
import org.bigmouth.ticket4j.utils.StationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;


public class TicketStartup {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TicketStartup.class);

    public static void main(String[] args) {
        JVMUtils.bootUsingSpring(new String[] {
                "config/applicationContext.xml"
        }, args);
        
        List<Person> persons = Lists.newArrayList(new Person[] {
                new Person(Seat.YW, "XXX", "431224XXXXXXXX5057")
        });
        
        Initialize initialize = SpringContextHolder.getBean("initialize");
        PassCode passCode = SpringContextHolder.getBean("passCode");
        User user = SpringContextHolder.getBean("user");
        Ticket ticket = SpringContextHolder.getBean("ticket");
        Order order = SpringContextHolder.getBean("order");
        
        Ticket4jHttpResponse response = initialize.init();
        
        byte[] code = null;
        Response checkResponse = new CheckPassCodeResponse();
        while (!checkResponse.isContinue()) {
            passCode.getLoginPassCode(response);
            Scanner scanner = new Scanner(System.in);
            code = scanner.next().getBytes();
            checkResponse = passCode.checkLogin(response, new String(code));
        }
        
        Response login = user.login(new String(code), response);
        if (login.isContinue()) {
            String trainDate = "2014-08-29";
            
            QueryTicketRequest condition = new QueryTicketRequest();
            condition.setTrainDate(trainDate);
            condition.setFromStation(StationUtils.find("杭州"));
            condition.setToStation(StationUtils.find("怀化"));
            condition.setIncludeTrain(Lists.newArrayList(new String[] {}));
            condition.setExcludeTrain(Lists.newArrayList(new String[] {}));
            condition.setSeats(Lists.newArrayList(Seat.YW, Seat.RW));
            condition.setTicketQuantity(persons.size());
            QueryTicketResponse result = ticket.query(response, condition);
            List<Train> allows = result.getAllows();
            for (Train train : allows) {
                LOGGER.info(train.toString());
                
                SubmitOrderRequest submitOrderRequest = new SubmitOrderRequest(trainDate, trainDate, condition.getPurposeCodes(), train);
                Response submitResponse = order.submit(response, submitOrderRequest);
                if (submitResponse.isContinue()) {
                    Token token = order.getToken(response);
                    
                    passCode.getOrderPassCode(response);
                    Scanner scanner = new Scanner(System.in);
                    code = scanner.next().getBytes();
                    
                    String seatTypes = train.getQueryLeftNewDTO().getSeat_types();
                    String passengerTicketStr = PersonUtils.toPassengerTicketStr(persons, seatTypes);
                    
                    CheckOrderInfoRequest checkOrderInfoRequest = new CheckOrderInfoRequest();
                    checkOrderInfoRequest.setRandCode(new String(code));
                    checkOrderInfoRequest.setRepeatSubmitToken(token.getToken());
                    checkOrderInfoRequest.setPassengerTicketStr(passengerTicketStr);
                    checkOrderInfoRequest.setOldPassengerStr("_ _ _ _ _");
                    CheckOrderInfoResponse checkOrderInfo = order.checkOrderInfo(response, checkOrderInfoRequest);
                    if (checkOrderInfo.isContinue()) {
                        
                        ConfirmSingleForQueueRequest queueRequest = new ConfirmSingleForQueueRequest();
                        queueRequest.setKeyCheckIsChange(token.getOrderKey());
                        queueRequest.setLeftTicketStr(train.getQueryLeftNewDTO().getYp_info());
                        queueRequest.setOldPassengerStr("_ ");
                        queueRequest.setPassengerTicketStr(passengerTicketStr);
                        queueRequest.setRandCode(new String(code));
                        queueRequest.setRepeatSubmitToken(token.getToken());
                        queueRequest.setTrainLocation(train.getQueryLeftNewDTO().getLocation_code());
                        
                        ConfirmSingleForQueueResponse confirmResponse = order.confirm(response, queueRequest);
                        if (confirmResponse.isContinue()) {
                            
                            NoCompleteOrderResponse noComplete = new NoCompleteOrderResponse();
                            while (true) {
                                noComplete = order.queryNoComplete(response);
                                if (noComplete.isContinue()) {
                                    System.out.println(noComplete);
                                    break;
                                }
                                else {
                                    System.out.println(noComplete);
                                }
                                try {
                                    Thread.sleep(1000);
                                }
                                catch (InterruptedException e) {
                                }
                            }
                            
                            try {
                                Thread.sleep(Integer.MAX_VALUE);
                            }
                            catch (InterruptedException e) {
                            }
                        }
                        else {
                            System.out.println(confirmResponse);
                        }
                    }
                    else {
                        System.out.println(checkOrderInfo);
                    }
                }
                else {
                    System.out.println(submitResponse);
                }
            }
        }
    }
}
