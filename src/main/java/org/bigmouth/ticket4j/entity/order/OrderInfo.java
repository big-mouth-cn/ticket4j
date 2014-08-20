package org.bigmouth.ticket4j.entity.order;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;


public class OrderInfo implements Serializable {

    private static final long serialVersionUID = 8971615217035076321L;

    private List<TicketInfo> tickets = Lists.newArrayList();
    
    private String sequence_no;
    private String order_date;
    private int ticket_totalnum;
    private long ticket_price_all ;
    private String cancel_flag;
    private String resign_flag;
    private String return_flag;
    private String print_eticket_flag;
    private String pay_flag;
    private String pay_resign_flag;
    private String confirm_flag;
    private String reserve_flag_query;
    private String if_show_resigning_info;
    private String recordCount;
    private String isNeedSendMailAndMsg;
    private String[] array_passser_name_page;
    private String[] from_station_name_page;
    private String[] to_station_name_page;
    private String start_train_date_page;
    private String start_time_page;
    private String arrive_time_page;
    private String train_code_page;
    private String ticket_total_price_page;
    private String come_go_traveller_order_page;
    private String canOffLinePay;
    private String if_deliver;

    public List<TicketInfo> getTickets() {
        return tickets;
    }

    public void setTickets(List<TicketInfo> tickets) {
        this.tickets = tickets;
    }

    public String getSequence_no() {
        return sequence_no;
    }

    public void setSequence_no(String sequence_no) {
        this.sequence_no = sequence_no;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public int getTicket_totalnum() {
        return ticket_totalnum;
    }

    public void setTicket_totalnum(int ticket_totalnum) {
        this.ticket_totalnum = ticket_totalnum;
    }

    public long getTicket_price_all() {
        return ticket_price_all;
    }

    public void setTicket_price_all(long ticket_price_all) {
        this.ticket_price_all = ticket_price_all;
    }

    public String getCancel_flag() {
        return cancel_flag;
    }

    public void setCancel_flag(String cancel_flag) {
        this.cancel_flag = cancel_flag;
    }

    public String getResign_flag() {
        return resign_flag;
    }

    public void setResign_flag(String resign_flag) {
        this.resign_flag = resign_flag;
    }

    public String getReturn_flag() {
        return return_flag;
    }

    public void setReturn_flag(String return_flag) {
        this.return_flag = return_flag;
    }

    public String getPrint_eticket_flag() {
        return print_eticket_flag;
    }

    public void setPrint_eticket_flag(String print_eticket_flag) {
        this.print_eticket_flag = print_eticket_flag;
    }

    public String getPay_flag() {
        return pay_flag;
    }

    public void setPay_flag(String pay_flag) {
        this.pay_flag = pay_flag;
    }

    public String getPay_resign_flag() {
        return pay_resign_flag;
    }

    public void setPay_resign_flag(String pay_resign_flag) {
        this.pay_resign_flag = pay_resign_flag;
    }

    public String getConfirm_flag() {
        return confirm_flag;
    }

    public void setConfirm_flag(String confirm_flag) {
        this.confirm_flag = confirm_flag;
    }

    public String getReserve_flag_query() {
        return reserve_flag_query;
    }

    public void setReserve_flag_query(String reserve_flag_query) {
        this.reserve_flag_query = reserve_flag_query;
    }

    public String getIf_show_resigning_info() {
        return if_show_resigning_info;
    }

    public void setIf_show_resigning_info(String if_show_resigning_info) {
        this.if_show_resigning_info = if_show_resigning_info;
    }

    public String getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(String recordCount) {
        this.recordCount = recordCount;
    }

    public String getIsNeedSendMailAndMsg() {
        return isNeedSendMailAndMsg;
    }

    public void setIsNeedSendMailAndMsg(String isNeedSendMailAndMsg) {
        this.isNeedSendMailAndMsg = isNeedSendMailAndMsg;
    }

    public String[] getArray_passser_name_page() {
        return array_passser_name_page;
    }

    public void setArray_passser_name_page(String[] array_passser_name_page) {
        this.array_passser_name_page = array_passser_name_page;
    }

    public String[] getFrom_station_name_page() {
        return from_station_name_page;
    }

    public void setFrom_station_name_page(String[] from_station_name_page) {
        this.from_station_name_page = from_station_name_page;
    }

    public String[] getTo_station_name_page() {
        return to_station_name_page;
    }

    public void setTo_station_name_page(String[] to_station_name_page) {
        this.to_station_name_page = to_station_name_page;
    }

    public String getStart_train_date_page() {
        return start_train_date_page;
    }

    public void setStart_train_date_page(String start_train_date_page) {
        this.start_train_date_page = start_train_date_page;
    }

    public String getStart_time_page() {
        return start_time_page;
    }

    public void setStart_time_page(String start_time_page) {
        this.start_time_page = start_time_page;
    }

    public String getArrive_time_page() {
        return arrive_time_page;
    }

    public void setArrive_time_page(String arrive_time_page) {
        this.arrive_time_page = arrive_time_page;
    }

    public String getTrain_code_page() {
        return train_code_page;
    }

    public void setTrain_code_page(String train_code_page) {
        this.train_code_page = train_code_page;
    }

    public String getTicket_total_price_page() {
        return ticket_total_price_page;
    }

    public void setTicket_total_price_page(String ticket_total_price_page) {
        this.ticket_total_price_page = ticket_total_price_page;
    }

    public String getCome_go_traveller_order_page() {
        return come_go_traveller_order_page;
    }

    public void setCome_go_traveller_order_page(String come_go_traveller_order_page) {
        this.come_go_traveller_order_page = come_go_traveller_order_page;
    }

    public String getCanOffLinePay() {
        return canOffLinePay;
    }

    public void setCanOffLinePay(String canOffLinePay) {
        this.canOffLinePay = canOffLinePay;
    }

    public String getIf_deliver() {
        return if_deliver;
    }

    public void setIf_deliver(String if_deliver) {
        this.if_deliver = if_deliver;
    }
}
