package org.bigmouth.ticket4j.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.message.BasicNameValuePair;
import org.bigmouth.ticket4j.Ticket;
import org.bigmouth.ticket4j.Ticket4jDefaults;
import org.bigmouth.ticket4j.entity.Seat;
import org.bigmouth.ticket4j.entity.request.QueryTicketRequest;
import org.bigmouth.ticket4j.entity.response.QueryTicketResponse;
import org.bigmouth.ticket4j.entity.train.Train;
import org.bigmouth.ticket4j.entity.train.TrainDetails;
import org.bigmouth.ticket4j.http.Ticket4jHttpClient;
import org.bigmouth.ticket4j.http.Ticket4jHttpResponse;
import org.bigmouth.ticket4j.utils.HttpClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;


public class DefaultTicket extends AccessSupport implements Ticket {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultTicket.class);
    
    private String uriQueryTickets = Ticket4jDefaults.URI_QUERY_TICKETS;

    public DefaultTicket(Ticket4jHttpClient ticket4jHttpClient) {
        super(ticket4jHttpClient);
    }

    @Override
    public QueryTicketResponse query(Ticket4jHttpResponse ticket4jHttpResponse, QueryTicketRequest condition) {
        HttpClient httpClient = ticket4jHttpClient.buildHttpClient();
        HttpGet get = ticket4jHttpClient.buildGetMethod(uriQueryTickets, ticket4jHttpResponse);
        try {
            Preconditions.checkNotNull(condition, "查询的车次信息不能为空!");
            Preconditions.checkArgument(StringUtils.isNotBlank(condition.getTrainDate()), "出发日期有误，请核实!");
            Preconditions.checkArgument(StringUtils.isNotBlank(condition.getFromStation()), "出发站有误，请核实!");
            Preconditions.checkArgument(StringUtils.isNotBlank(condition.getToStation()), "到达站有误，请核实!");
            try {
                String date = convert(condition.getTrainDate(), "yyyy-MM-dd");
                condition.setTrainDate(date);
            }
            catch (ParseException e) {
                throw new IllegalArgumentException("出发日期有误，请核实!");
            }
            
            addPair(get, new NameValuePair[] {
                    new BasicNameValuePair("leftTicketDTO.train_date", condition.getTrainDate()),
                    new BasicNameValuePair("leftTicketDTO.from_station", condition.getFromStation()),
                    new BasicNameValuePair("leftTicketDTO.to_station", condition.getToStation()),
                    new BasicNameValuePair("purpose_codes", condition.getPurposeCodes())
            });
            
            HttpResponse httpResponse = httpClient.execute(get);
            String body = HttpClientUtils.getResponseBody(httpResponse);
            QueryTicketResponse result = fromJson(body, QueryTicketResponse.class);
            if (!result.isStatus()) {
                throw new IllegalArgumentException(result.getMessage());
            }
            List<String> includes = condition.getIncludeTrain();
            List<String> excludes = condition.getExcludeTrain();
            List<Seat> seats = condition.getSeats();
            int ticketQuantity = condition.getTicketQuantity();
            List<Train> allows = result.filter(includes, excludes, seats, ticketQuantity);
            
            // 将所有未过滤的车次打印出来
            if (LOGGER.isInfoEnabled()) {
                for (Train train : result.getData()) {
                    LOGGER.info(train.toString());
                }
            }
            
            if (CollectionUtils.isEmpty(allows))
                return result;
            // 按照席别进行车次排序
            List<Train> snapshot = sort(seats, ticketQuantity, allows);
            result.setAllows(snapshot);
            
            // 打印排序后的车次顺序
            if (LOGGER.isInfoEnabled()) {
                StringBuilder message = new StringBuilder(); 
                for (Train train : snapshot) {
                    message.append(train.getQueryLeftNewDTO().getStation_train_code()).append(">");
                }
                LOGGER.info("根据您的订票席别的优先顺序，系统将车次进行了排序：" + message.substring(0, message.length() - 1));
            }
            return result;
        }
        catch (Exception e) {
            LOGGER.error("查询车票失败!错误原因：{}", e.getMessage());
        }
        return null;
    }

    /**
     * 按席别优先顺序对符合条件的车次进行排序
     * 
     * @param seats
     * @param ticketQuantity
     * @param allows
     * @return
     */
    private List<Train> sort(List<Seat> seats, int ticketQuantity, List<Train> allows) {
        List<Train> snapshot = Lists.newArrayList();
        for (Seat expect : seats) {
            for (Train train : allows) {
                TrainDetails details = train.getQueryLeftNewDTO();
                List<Seat> canBuy = details.filterSeats(seats, ticketQuantity);
                for (Seat seat : canBuy) {
                    if (expect == seat && !snapshot.contains(train)) {
                        snapshot.add(train);
                    }
                }
            }
        }
        return snapshot;
    }
    
    private static String convert(String stringDate, String dateFormat) throws ParseException {
        if (StringUtils.isBlank(stringDate)) {
            throw new IllegalArgumentException();
        }
        if (StringUtils.isBlank(dateFormat)) {
            throw new IllegalArgumentException();
        }
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        Date date = sdf.parse(stringDate);
        return sdf.format(date);
    }

    public void setUriQueryTickets(String uriQueryTickets) {
        this.uriQueryTickets = uriQueryTickets;
    }
}
