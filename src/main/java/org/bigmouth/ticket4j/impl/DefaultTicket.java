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
import org.bigmouth.ticket4j.http.Ticket4jHttpClient;
import org.bigmouth.ticket4j.http.Ticket4jHttpResponse;
import org.bigmouth.ticket4j.utils.HttpClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;


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
                throw new IllegalArgumentException("query ticket error: " + body);
            }
            List<String> includes = condition.getIncludeTrain();
            List<String> excludes = condition.getExcludeTrain();
            List<Seat> seats = condition.getSeats();
            int ticketQuantity = condition.getTicketQuantity();
            List<Train> allows = result.filter(includes, excludes, seats, ticketQuantity);
            result.setAllows(allows);
            return result;
        }
        catch (Exception e) {
            LOGGER.error("query: ", e);
        }
        return null;
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
