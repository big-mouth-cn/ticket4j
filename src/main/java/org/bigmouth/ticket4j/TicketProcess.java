package org.bigmouth.ticket4j;

import java.io.File;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;
import org.bigmouth.framework.core.SpringContextHolder;
import org.bigmouth.ticket4j.cookie.CookieCache;
import org.bigmouth.ticket4j.entity.Person;
import org.bigmouth.ticket4j.entity.Response;
import org.bigmouth.ticket4j.entity.Seat;
import org.bigmouth.ticket4j.entity.Token;
import org.bigmouth.ticket4j.entity.request.CheckOrderInfoRequest;
import org.bigmouth.ticket4j.entity.request.ConfirmSingleForQueueRequest;
import org.bigmouth.ticket4j.entity.request.QueryTicketRequest;
import org.bigmouth.ticket4j.entity.request.SubmitOrderRequest;
import org.bigmouth.ticket4j.entity.response.CheckOrderInfoResponse;
import org.bigmouth.ticket4j.entity.response.CheckUserResponse;
import org.bigmouth.ticket4j.entity.response.ConfirmSingleForQueueResponse;
import org.bigmouth.ticket4j.entity.response.NoCompleteOrderResponse;
import org.bigmouth.ticket4j.entity.response.QueryTicketResponse;
import org.bigmouth.ticket4j.entity.train.Train;
import org.bigmouth.ticket4j.http.Ticket4jHttpResponse;
import org.bigmouth.ticket4j.utils.AntiUtils;
import org.bigmouth.ticket4j.utils.CharsetUtils;
import org.bigmouth.ticket4j.utils.PersonUtils;
import org.bigmouth.ticket4j.utils.StationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class TicketProcess {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TicketProcess.class);
    
    private final CookieCache cookieCache;
    private final AntiUtils antiUtils;
    
    private String passengers;
    private String seatSource;
    private String trainDate;
    private String trainFrom;
    private String trainTo;
    private String includeSource;
    private String excludeSource;
    
    private int queryTicketSleepTime = 1000;
    
    private boolean recognition = true;

    public TicketProcess(CookieCache cookieCache, AntiUtils antiUtils) {
        this.cookieCache = cookieCache;
        this.antiUtils = antiUtils;
    }
    
    public void agreement() {
        System.out.println("《东皇钟》使用合约");
        System.out.println("您必须同意以下协议才可以使用本软件");
        System.out.println("1. 本软件所有购票接口基于12306官网，从下载、安装、升级完全免费；");
        System.out.println("2. 本软件只是一个辅助工具，其购票原理是减少人机交互，循环操作，并不能保证百分之百成功；");
        System.out.println("3. 不得对本软件进行恶意篡改、破解、反编译及倒卖软件等不法行为，否则我们有权无条件终止您的使用；");
        System.out.println("4. 因不可抗力因素导致本软件不能使用，本软件不承担任何责任；");
        System.out.println("5. 本软件未授权任何单位及个人，也未在任何第三方平台销售；");
        System.out.println("6. 使用本软件需遵守相关法律法规，不得利用本软件有非法倒票、卖票等行为；");
        System.out.println("7. 本软件可能会保存并上传用户的使用记录，并承诺将严格保障用户隐私权，对用户的个人信息保密，未经用户的同意不得向他人泄露，但法律另有规定的除外。只有当相关部门依照法定程序要求我们披露用户的个人资料时，我们才会依法或为维护公共安全之目的向执法单位提供用户的个人资料，且不承担任何法律责任。");
        System.out.println("--------------------------------------------------");
        System.out.print("如果您同意以上协议，继续使用请输入“Y”确认：");
        String input = new Scanner(System.in).next();
        if (!StringUtils.equalsIgnoreCase(input, "Y")) {
            System.out.println("再见!");
            System.exit(0);
        }
    }

    public void start() {
        agreement();
        
        try {
            List<Person> persons = Person.of(passengers);
            List<Seat> seats = Seat.of(seatSource);
    
            Initialize initialize = SpringContextHolder.getBean("initialize");
            PassCode passCode = SpringContextHolder.getBean("passCode");
            User user = SpringContextHolder.getBean("user");
            Ticket ticket = SpringContextHolder.getBean("ticket");
            Order order = SpringContextHolder.getBean("order");
            
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("将要预订的车票信息：{} 从 {} 到 {} 的 {}", new String[] {
                        trainDate, trainFrom, trainTo, Seat.getDescription(seats)
                });
                LOGGER.info("乘车人信息：{}", persons.toString());
            }
            
            // 初始化Cookie及登录
            Ticket4jHttpResponse response = initTicket4jHttpResponse(initialize, user);
            
            while (!response.isSignIn()) {
                byte[] code = null;
                File loginPassCode = passCode.getLoginPassCode(response);
                if (recognition) {
                    code = antiUtils.recognition(4, loginPassCode.getPath());
                }
                else {
                    System.out.println("请输入验证码(" + loginPassCode.getPath() + ")并回车确认：");
                    Scanner scanner = new Scanner(System.in);
                    code = scanner.next().getBytes();
                }
                Response login = user.login(new String(code), response);
                response.setSignIn(login.isContinue());
            }
            
            // 查票
            QueryTicketRequest condition = new QueryTicketRequest();
            List<Train> allows = null;
            do {
                condition.setTrainDate(trainDate);
                condition.setFromStation(StationUtils.find(trainFrom));
                condition.setToStation(StationUtils.find(trainTo));
                condition.setIncludeTrain(Lists.newArrayList(StringUtils.split(includeSource, ",")));
                condition.setExcludeTrain(Lists.newArrayList(StringUtils.split(excludeSource, ",")));
                condition.setSeats(seats);
                condition.setTicketQuantity(persons.size());
                QueryTicketResponse result = ticket.query(response, condition);
                allows = result.getAllows();
                if (CollectionUtils.isEmpty(allows)) {
                    LOGGER.info("暂时没有符合预订条件的车次。");
                    Thread.sleep(queryTicketSleepTime);
                }
            } while (CollectionUtils.isEmpty(allows));
            
            for (Train train : allows) {
                SubmitOrderRequest submitOrderRequest = new SubmitOrderRequest(trainDate, trainDate, condition.getPurposeCodes(), train);
                Response submitResponse = order.submit(response, submitOrderRequest);
                if (submitResponse.isContinue()) {
                    List<Seat> canBuySeats = train.getCanBuySeats(); // 允许购买的席别
                    Seat seat = canBuySeats.get(0);
                    if (LOGGER.isInfoEnabled()) {
                        LOGGER.info("乘车人为 {}，席别为 [{}]。", persons, Seat.getDescription(seat));
                    }
                    
                    Token token = order.getToken(response);
                    
                    
                    // 检查订单完整性
                    String seatTypes = train.getQueryLeftNewDTO().getSeat_types();
                    String passengerTicketStr = PersonUtils.toPassengerTicketStr(persons, seat, seatTypes);
                    
                    CheckOrderInfoRequest checkOrderInfoRequest = new CheckOrderInfoRequest();
                    checkOrderInfoRequest.setRepeatSubmitToken(token.getToken());
                    checkOrderInfoRequest.setPassengerTicketStr(passengerTicketStr);
                    CheckOrderInfoResponse checkOrderInfo = new CheckOrderInfoResponse();
                    byte[] code = null;
                    do {
                        File orderPassCode = passCode.getOrderPassCode(response);
                        if (recognition) {
                            code = antiUtils.recognition(4, orderPassCode.getPath());
                        }
                        else {
                            System.out.println("请输入验证码(" + orderPassCode.getPath() + ")并回车确认：");
                            Scanner scanner = new Scanner(System.in);
                            code = scanner.next().getBytes();
                        }
                        checkOrderInfoRequest.setRandCode(new String(code));
                        
                        checkOrderInfo = order.checkOrderInfo(response, checkOrderInfoRequest);
                        if (checkOrderInfo.isContinue()) {
                            break;
                        }
                        else {
                            LOGGER.warn(checkOrderInfo.getMessage());
                        }
                    } while (!checkOrderInfo.isContinue());
                    
                    
                    // 提交订单
                    ConfirmSingleForQueueRequest queueRequest = new ConfirmSingleForQueueRequest();
                    queueRequest.setKeyCheckIsChange(token.getOrderKey());
                    queueRequest.setLeftTicketStr(train.getQueryLeftNewDTO().getYp_info());
                    queueRequest.setPassengerTicketStr(passengerTicketStr);
                    queueRequest.setRandCode(new String(code));
                    queueRequest.setRepeatSubmitToken(token.getToken());
                    queueRequest.setTrainLocation(train.getQueryLeftNewDTO().getLocation_code());
                    
                    ConfirmSingleForQueueResponse confirmResponse = order.confirm(response, queueRequest);
                    if (confirmResponse.isContinue()) {
                        NoCompleteOrderResponse noComplete = new NoCompleteOrderResponse();
                        do {
                            noComplete = order.queryNoComplete(response);
                            if (noComplete.isContinue()) {
                                LOGGER.info("恭喜车票预订成功，请尽快登录12306客运服务后台进行支付。");
                                System.out.println();
                                System.out.println(noComplete.toString());
                            }
                        } while (!noComplete.isContinue());
                        System.exit(-1);
                    }
                    else {
                        LOGGER.warn(confirmResponse.toString());
                    }
                }
                else {
                    LOGGER.warn(submitResponse.getMessage());
                }
            }
        }
        catch (Exception e) {
        }
    }

    private Ticket4jHttpResponse initTicket4jHttpResponse(Initialize initialize, User user) {
        Ticket4jHttpResponse response = null;
        CheckUserResponse cur = user.check(cookieCache);
        if (!cur.isContinue()) {
            response = initialize.init();
            response.setSignIn(false);
            cookieCache.write(response.getHeaders(), user.getUsername());
        }
        else {
            response = cur.getTicket4jHttpResponse();
            response.setSignIn(true);
        }
        return response;
    }

    public void setPassengers(String passengers) {
        Preconditions.checkArgument(StringUtils.isNotBlank(passengers), "没有乘车人信息!");
        this.passengers = passengers;
    }

    public void setSeatSource(String seatSource) {
        this.seatSource = seatSource;
    }

    public void setTrainDate(String trainDate) {
        Preconditions.checkArgument(StringUtils.isNotBlank(trainDate), "没有设置乘车日期!");
        this.trainDate = CharsetUtils.convert(trainDate);
    }

    public void setTrainFrom(String trainFrom) {
        Preconditions.checkArgument(StringUtils.isNotBlank(trainFrom), "没有设置出发站!");
        this.trainFrom = CharsetUtils.convert(trainFrom);
    }

    public void setTrainTo(String trainTo) {
        Preconditions.checkArgument(StringUtils.isNotBlank(trainTo), "没有设置到达站!");
        this.trainTo = CharsetUtils.convert(trainTo);
    }

    public void setIncludeSource(String includeSource) {
        this.includeSource = includeSource;
    }

    public void setExcludeSource(String excludeSource) {
        this.excludeSource = excludeSource;
    }

    public void setQueryTicketSleepTime(int queryTicketSleepTime) {
        this.queryTicketSleepTime = queryTicketSleepTime;
    }

    public void setRecognition(boolean recognition) {
        this.recognition = recognition;
    }
}
