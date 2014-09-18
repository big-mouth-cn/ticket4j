package org.bigmouth.ticket4j.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.message.BasicNameValuePair;
import org.bigmouth.framework.util.DateUtils;
import org.bigmouth.ticket4j.Ticket;
import org.bigmouth.ticket4j.Ticket4jDefaults;
import org.bigmouth.ticket4j.entity.OrderBy;
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
    private static SimpleDateFormat SDF = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
    private static String available;
    
    private String uriQueryTicketAddrs = Ticket4jDefaults.URI_QUERY_TICKETS;
    
    /** 当前服务器时间 */
    private Date serverTime = null;

    public DefaultTicket(Ticket4jHttpClient ticket4jHttpClient) {
        super(ticket4jHttpClient);
        SDF.setTimeZone(TimeZone.getTimeZone("GMT"));
    }
    
    @Override
    public QueryTicketResponse query(Ticket4jHttpResponse ticket4jHttpResponse, QueryTicketRequest condition) {
        QueryTicketResponse response = null;
        if (StringUtils.isNotBlank(available)) {
            response = queryTicket(available, ticket4jHttpResponse, condition);
            if (isNotSwitchURL(response)) 
                return response;
        }
        String[] uris = StringUtils.split(uriQueryTicketAddrs, ",");
        for (String uri : uris) {
            response = queryTicket(uri, ticket4jHttpResponse, condition);
            if (isNotSwitchURL(response)) {
                available = uri;
                return response;
            }
            else {
                LOGGER.warn("查票地址 {} 无效，正在切换..", uri);
            }
        }
        if (null != response) {
            // 没有有效的查票地址，那么试图从返回的结果中获取
            String c_url = new StringBuilder(Ticket4jDefaults.URI).append("/").append(response.getC_url()).toString();
            LOGGER.warn("试图从返回的结果中获取查票地址...", c_url);
            available = c_url;
        }
        return response;
    }

    private boolean isNotSwitchURL(QueryTicketResponse response) {
        if (null == response) 
            return false;
        return (null != response && response.isStatus()) 
                    || StringUtils.isBlank(response.getC_url());
    }

    private QueryTicketResponse queryTicket(String uri, Ticket4jHttpResponse ticket4jHttpResponse, QueryTicketRequest condition) {
        HttpClient httpClient = ticket4jHttpClient.buildHttpClient(true);
        HttpGet get = ticket4jHttpClient.buildGetMethod(uri, ticket4jHttpResponse);
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
            long start = System.currentTimeMillis();
            HttpResponse httpResponse = httpClient.execute(get);
            
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Query tickets at " + (System.currentTimeMillis() - start) + " ms");
            
            Date crtServerTime = printCacheHeaders(httpResponse);
            if (null != crtServerTime) {
                serverTime = crtServerTime;
            }
            
            String body = HttpClientUtils.getResponseBody(httpResponse);
            QueryTicketResponse result = fromJson(body, QueryTicketResponse.class);
            if (!result.isStatus()) {
                return result;
            }
            List<String> includes = condition.getIncludeTrain();
            List<String> excludes = condition.getExcludeTrain();
            List<Seat> seats = condition.getSeats();
            int ticketQuantity = condition.getTicketQuantity();
            boolean orderByIncludes = condition.getOrderBy() == OrderBy.ORDER_TRAIN;
            List<Train> allows = result.filter(includes, excludes, seats, ticketQuantity, orderByIncludes);
            
            // 将所有未过滤的车次打印出来
            if (LOGGER.isDebugEnabled()) {
                for (Train train : result.getData()) {
                    LOGGER.debug(train.toString());
                }
            }
            
            if (CollectionUtils.isEmpty(allows))
                return result;
            // 按照席别进行车次排序
            if (condition.getOrderBy() == OrderBy.ORDER_SEAT) {
                List<Train> snapshot = sort(seats, ticketQuantity, allows);
                result.setAllows(snapshot);
            } else {
                result.setAllows(allows);
            }
            
            // 打印排序后的车次顺序
            if (LOGGER.isInfoEnabled()) {
                StringBuilder message = new StringBuilder(); 
                for (Train train : result.getAllows()) {
                    message.append(train.getQueryLeftNewDTO().getStation_train_code()).append(">");
                }
                LOGGER.info("根据您的订票席别的优先顺序，系统将车次进行了排序：" + message.substring(0, message.length() - 1));
            }
            return result;
        }
        catch (Exception e) {
            LOGGER.error("查询车票失败!", e);
            throw new RuntimeException(e);
        }
        finally {
            httpClient.getConnectionManager().shutdown();
        }
    }

    private Date printCacheHeaders(HttpResponse httpResponse) {
        StringBuilder hdMessage = new StringBuilder();
        boolean isCache = true;
        Date crtServerTime = null;
        for (Header header : httpResponse.getAllHeaders()) {
            if (StringUtils.equals("X-Via", header.getName())) {
                hdMessage.append(header.getValue());
            }
            else if (StringUtils.equals("Date", header.getName())) {
                // Mon, 01 Sep 2014 06:55:22 GMT
                String time = header.getValue();
                try {
                    crtServerTime = SDF.parse(header.getValue());
                    time = DateUtils.convertDate2String(crtServerTime, "yyyy/MM/dd HH:mm:ss");
                }
                catch (Exception e) {
                }
                hdMessage.append(time).append(" ");
            }
            else if (StringUtils.equals("Server", header.getName())) {
                // Cache
                isCache = false;
            }
        }
        if (LOGGER.isInfoEnabled())
            LOGGER.info("{} [{}]", (isCache) ? "Cache" : "Data ", hdMessage.toString());
        return (isCache) ? null : crtServerTime;
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

    public Date getServerTime() {
        return serverTime;
    }
    
    public void setUriQueryTicketAddrs(String uriQueryTicketAddrs) {
        this.uriQueryTicketAddrs = uriQueryTicketAddrs;
    }
}
