package org.bigmouth.ticket4j.entity.request;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.bigmouth.ticket4j.Ticket4jDefaults;
import org.bigmouth.ticket4j.entity.OrderBy;
import org.bigmouth.ticket4j.entity.Request;
import org.bigmouth.ticket4j.entity.Seat;

import com.google.common.collect.Lists;

/**
 * Query Ticket Request Entity.
 * 
 * @author Allen.Hu / 2014-8-15
 */
public class QueryTicketRequest extends Request {

    private static final long serialVersionUID = -1946001474474831121L;

    /** Train date, Pattern 'yyyy-MM-dd' */
    private String trainDate;
    /** From station (City Code) */
    private String fromStation;
    /** To station (City Code) */
    private String toStation;
    /** Constant, Don't know its effect.  */
    private String purposeCodes = Ticket4jDefaults.PURPOSE_CODE_ADULT;
    
    /** Include trains */
    private List<String> includeTrain = Lists.newArrayList();
    /** Exclude trains */
    private List<String> excludeTrain = Lists.newArrayList();
    /** Given seats */
    private List<Seat> seats = Lists.newArrayList(Seat.ALL);
    /** Ticket quantity */
    private int ticketQuantity = 0;
    /** Order By */
    private OrderBy orderBy = OrderBy.ORDER_SEAT;

    public String getTrainDate() {
        return trainDate;
    }

    public void setTrainDate(String trainDate) {
        this.trainDate = trainDate;
    }

    public String getFromStation() {
        return fromStation;
    }

    public void setFromStation(String fromStation) {
        this.fromStation = fromStation;
    }

    public String getToStation() {
        return toStation;
    }

    public void setToStation(String toStation) {
        this.toStation = toStation;
    }

    public String getPurposeCodes() {
        return purposeCodes;
    }

    public void setPurposeCodes(String purposeCodes) {
        this.purposeCodes = purposeCodes;
    }

    public List<String> getIncludeTrain() {
        return includeTrain;
    }

    public void setIncludeTrain(List<String> includeTrain) {
        this.includeTrain = includeTrain;
    }

    public List<String> getExcludeTrain() {
        return excludeTrain;
    }

    public void setExcludeTrain(List<String> excludeTrain) {
        this.excludeTrain = excludeTrain;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    public int getTicketQuantity() {
        return ticketQuantity;
    }

    public void setTicketQuantity(int ticketQuantity) {
        this.ticketQuantity = ticketQuantity;
    }
    
    public OrderBy getOrderBy() {
        return orderBy;
    }
    
    public void setOrderBy(OrderBy orderBy) {
        this.orderBy = orderBy;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
