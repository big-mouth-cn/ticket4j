package org.bigmouth.ticket4j.report;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.bigmouth.ticket4j.entity.order.OrderInfo;

public class Report implements Serializable {

    private static final long serialVersionUID = -6255942933590330425L;

    private String ip;
    private String mac;
    private String username;
    private List<OrderInfo> orders;
    private Date time = new Date();

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<OrderInfo> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderInfo> orders) {
        this.orders = orders;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
