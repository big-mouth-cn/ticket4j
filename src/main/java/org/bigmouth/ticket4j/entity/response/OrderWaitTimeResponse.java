package org.bigmouth.ticket4j.entity.response;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.bigmouth.ticket4j.entity.Response;

public class OrderWaitTimeResponse extends Response {

    private static final long serialVersionUID = -7247866516370688173L;

    private OrderWaitTime data = new OrderWaitTime();

    public class OrderWaitTime {

        private boolean queryOrderWaitTimeStatus;

        private int count;

        private int waitTime = 1;

        private String requestId;

        private int waitCount;

        private String tourFlag;

        private String orderId;

        public boolean isSuccessful() {
            return waitTime <= 0;
        }

        public boolean isQueryOrderWaitTimeStatus() {
            return queryOrderWaitTimeStatus;
        }

        public void setQueryOrderWaitTimeStatus(boolean queryOrderWaitTimeStatus) {
            this.queryOrderWaitTimeStatus = queryOrderWaitTimeStatus;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getWaitTime() {
            return waitTime;
        }

        public void setWaitTime(int waitTime) {
            this.waitTime = waitTime;
        }

        public String getRequestId() {
            return requestId;
        }

        public void setRequestId(String requestId) {
            this.requestId = requestId;
        }

        public int getWaitCount() {
            return waitCount;
        }

        public void setWaitCount(int waitCount) {
            this.waitCount = waitCount;
        }

        public String getTourFlag() {
            return tourFlag;
        }

        public void setTourFlag(String tourFlag) {
            this.tourFlag = tourFlag;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
        }
    }

    public OrderWaitTime getData() {
        return data;
    }

    public void setData(OrderWaitTime data) {
        this.data = data;
    }

    @Override
    public boolean isContinue() {
        return this.data.isSuccessful();
    }
}
