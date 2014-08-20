package org.bigmouth.ticket4j.entity;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

public class Token implements Serializable {

    private static final long serialVersionUID = -6914733738186563288L;

    /** 检查订单时需要的Token */
    private String token;
    private String orderKey;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getOrderKey() {
        return orderKey;
    }

    public void setOrderKey(String orderKey) {
        this.orderKey = orderKey;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
