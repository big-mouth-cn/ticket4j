package org.bigmouth.ticket4j.entity.response;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.bigmouth.ticket4j.entity.Response;

public class QueueCountResponse extends Response {

    private static final long serialVersionUID = 3431695775221191927L;

    private QueueCount data = new QueueCount();

    public class QueueCount {

        private String count;
        private String ticket;
        private String op_2 = "false";
        private int countT = 1;
        private String op_1;

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getTicket() {
            return ticket;
        }

        public void setTicket(String ticket) {
            this.ticket = ticket;
        }

        public String getOp_2() {
            return op_2;
        }

        public void setOp_2(String op_2) {
            this.op_2 = op_2;
        }

        public int getCountT() {
            return countT;
        }

        public void setCountT(int countT) {
            this.countT = countT;
        }

        public String getOp_1() {
            return op_1;
        }

        public void setOp_1(String op_1) {
            this.op_1 = op_1;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
        }
    }

    @Override
    public boolean isContinue() {
        return (data.getCountT() <= 0);
    }

    public QueueCount getData() {
        return data;
    }

    public void setData(QueueCount data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
