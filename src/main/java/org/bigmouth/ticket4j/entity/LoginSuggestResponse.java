package org.bigmouth.ticket4j.entity;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang3.StringUtils;

public class LoginSuggestResponse extends Response {

    private static final long serialVersionUID = -5468445397808461171L;

    private Data data = new Data();

    public class Data {

        private String loginCheck = CHECK_FAIL;

        public String getLoginCheck() {
            return loginCheck;
        }

        public void setLoginCheck(String loginCheck) {
            this.loginCheck = loginCheck;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @Override
    public boolean isContinue() {
        return StringUtils.equals(getData().getLoginCheck(), CHECK_OK);
    }

}
