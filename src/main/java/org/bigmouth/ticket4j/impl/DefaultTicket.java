package org.bigmouth.ticket4j.impl;

import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.bigmouth.ticket4j.Ticket;
import org.bigmouth.ticket4j.Ticket4jDefaults;
import org.bigmouth.ticket4j.entity.QueryTicketRequest;
import org.bigmouth.ticket4j.entity.QueryTicketResponse;
import org.bigmouth.ticket4j.entity.Seat;
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
        HttpPost post = ticket4jHttpClient.buildPostMethod(uriQueryTickets, ticket4jHttpResponse);
        try {
            Preconditions.checkNotNull(condition, "查询的车次信息不能为空!");
            addPair(post, new NameValuePair[] {
                    new BasicNameValuePair("leftTicketDTO.train_date", condition.getTrainDate()),
                    new BasicNameValuePair("leftTicketDTO.from_station", condition.getFromStation()),
                    new BasicNameValuePair("leftTicketDTO.to_station", condition.getToStation()),
                    new BasicNameValuePair("purpose_codes", condition.getPurposeCodes())
            });
            HttpResponse httpResponse = httpClient.execute(post);
            String body = HttpClientUtils.getResponseBody(httpResponse);
            QueryTicketResponse result = fromJson(body, QueryTicketResponse.class);
            
            List<String> includes = condition.getIncludeTrain();
            List<String> excludes = condition.getExcludeTrain();
            List<Seat> seats = condition.getSeats();
            int ticketQuantity = condition.getTicketQuantity();
            List<Train> allows = result.allows(includes, excludes, seats, ticketQuantity);
            result.setAllows(allows);
            return result;
        }
        catch (Exception e) {
            LOGGER.error("query: ", e);
        }
        return null;
    }
}
