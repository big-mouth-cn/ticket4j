package org.bigmouth.ticket4j.entity.response;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.bigmouth.ticket4j.entity.Response;

public class CheckOrderInfoResponse extends Response {

    private static final long serialVersionUID = -6090849463467982744L;

    private SubmitStatus data = new SubmitStatus();

    public class SubmitStatus {

        private boolean submitStatus;

        private String errMsg;

        public boolean isSubmitStatus() {
            return submitStatus;
        }

        public void setSubmitStatus(boolean submitStatus) {
            this.submitStatus = submitStatus;
        }

        public String getErrMsg() {
            return errMsg;
        }

        public void setErrMsg(String errMsg) {
            this.errMsg = errMsg;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }
    }

    public SubmitStatus getData() {
        return data;
    }

    public void setData(SubmitStatus data) {
        this.data = data;
    }

    @Override
    public boolean isContinue() {
        return data.isSubmitStatus();
    }
    
    public String getMessage() {
        return this.getData().getErrMsg();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
