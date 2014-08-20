package org.bigmouth.ticket4j.entity.response;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.bigmouth.ticket4j.entity.Response;
import org.bigmouth.ticket4j.entity.order.OrderInfo;
import org.bigmouth.ticket4j.entity.order.Passenger;
import org.bigmouth.ticket4j.entity.order.StationTrain;
import org.bigmouth.ticket4j.entity.order.TicketInfo;

import com.google.common.collect.Lists;

public class NoCompleteOrderResponse extends Response {

    private static final long serialVersionUID = 3065699090394405891L;

    private OrderDB data = new OrderDB();

    public class OrderDB {

        private List<OrderInfo> orderDBList = Lists.newArrayList();
        private String to_page;

        public String getTo_page() {
            return to_page;
        }

        public void setTo_page(String to_page) {
            this.to_page = to_page;
        }

        public List<OrderInfo> getOrderDBList() {
            return orderDBList;
        }

        public void setOrderDBList(List<OrderInfo> orderDBList) {
            this.orderDBList = orderDBList;
        }
    }

    public OrderDB getData() {
        return data;
    }

    public void setData(OrderDB data) {
        this.data = data;
    }

    @Override
    public boolean isContinue() {
        return CollectionUtils.isNotEmpty(data.getOrderDBList());
    }

    @Override
    public String toString() {
        List<OrderInfo> orderDBList = getData().getOrderDBList();
        if (CollectionUtils.isEmpty(orderDBList)) {
            return ToStringBuilder.reflectionToString(this);
        }
        StringBuilder builder = new StringBuilder(128);
        for (OrderInfo orderInfo : orderDBList) {
            builder.append(">>>>>> 订单号：").append(orderInfo.getSequence_no()).append("\t").append("\t订单日期：")
                    .append(orderInfo.getOrder_date()).append(" ").append("<<<<<<\r\n");

            for (TicketInfo ticket : orderInfo.getTickets()) {
                StationTrain train = ticket.getStationTrainDTO();
                Passenger passenger = ticket.getPassengerDTO();
                builder.append(ticket.getStart_train_date_page()).append("\t开 ").append(train.getStation_train_code())
                        .append(" ").append(train.getFrom_station_name()).append("-")
                        .append(train.getTo_station_name()).append("\t").append(ticket.getCoach_name()).append("车厢 ")
                        .append(ticket.getSeat_name()).append("(").append(ticket.getSeat_type_name()).append(")").append("\t").append(passenger.getPassenger_name()).append("\t")
                        .append(ticket.getStr_ticket_price_page()).append("(").append(ticket.getTicket_type_name())
                        .append(")").append("\r\n");
            }
        }
        return builder.toString();
    }
}
