package org.bigmouth.ticket4j.entity.response;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.bigmouth.ticket4j.entity.Response;
import org.bigmouth.ticket4j.entity.Seat;
import org.bigmouth.ticket4j.entity.train.Train;
import org.bigmouth.ticket4j.entity.train.TrainDetails;

import com.google.common.collect.Lists;

/**
 * Query Ticket Response Entity.
 * 
 * @author Allen.Hu / 2014-8-15
 */
public class QueryTicketResponse extends Response {

    private static final long serialVersionUID = 8307990313426312543L;
    
    private String c_url;
    private String c_name;

    private List<Train> data = Lists.newArrayList();
    private List<Train> allows = Lists.newArrayList();

    public String getC_url() {
        return c_url;
    }

    public void setC_url(String c_url) {
        this.c_url = c_url;
    }

    public String getC_name() {
        return c_name;
    }
    
    public void setC_name(String c_name) {
        this.c_name = c_name;
    }

    public List<Train> getData() {
        return data;
    }

    public void setData(List<Train> data) {
        this.data = data;
    }
    
    public List<Train> getAllows() {
        return allows;
    }

    public void setAllows(List<Train> allows) {
        this.allows = allows;
    }

    @Override
    public boolean isContinue() {
        throw new RuntimeException("Not allowed.");
    }

    private boolean allowBuy() {
        if (CollectionUtils.isEmpty(data))
            return false;
        
        for (Train train : data) {
            TrainDetails details = train.getQueryLeftNewDTO();
            if (details.isContinue()) {
                return true;
            }
        }
        return false;
    }
    
    public List<Train> filter(List<String> includes, List<String> excludes, List<Seat> seats, int ticketQuantity) {
        if (!allowBuy())
            return Lists.newArrayList();
        List<Train> trains = Lists.newArrayList();
        filterTrains(includes, excludes, trains);
        filterSeats(seats, ticketQuantity, trains);
        return trains;
    }

    private void filterTrains(List<String> includes, List<String> excludes, List<Train> trains) {
        // 仅预定指定车次的车票
        if (CollectionUtils.isNotEmpty(includes)) {
            for (Train train : data) {
                TrainDetails dto = train.getQueryLeftNewDTO();
                if (dto.contains(includes)) {
                    trains.add(train);
                }
            }
        }
        // 预定排除外所有的车次
        else if (CollectionUtils.isNotEmpty(excludes)) {
            trains.addAll(data);
            for (Train train : data) {
                TrainDetails dto = train.getQueryLeftNewDTO();
                if (dto.contains(excludes)) {
                    trains.remove(train);
                }
            }
        }
        // 未设置指定车次并且未设定排除的车次，那么默认预定所有的车次
        else { 
            trains.addAll(data);
        }
    }

    private void filterSeats(List<Seat> seats, int ticketQuantity, List<Train> trains) {
        List<Train> removePrepare = Lists.newArrayList();
        for (Train train : trains) {
            TrainDetails details = train.getQueryLeftNewDTO();
            List<Seat> allows = details.filterSeats(seats, ticketQuantity);
            if (CollectionUtils.isEmpty(allows)) {
                removePrepare.add(train);
            }
        }
        trains.removeAll(removePrepare);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
