package org.bigmouth.ticket4j.entity.response;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.bigmouth.ticket4j.entity.Response;

public class ConfirmSingleForQueueResponse extends Response {

    private static final long serialVersionUID = 3010182131454799729L;

    private Data data = new Data();

    public class Data {

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
    
    public String getErrorMessage() {
        return this.data.getErrMsg();
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @Override
    public boolean isContinue() {
        return data.isSubmitStatus();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
