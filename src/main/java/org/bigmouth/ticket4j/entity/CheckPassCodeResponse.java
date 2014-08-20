package org.bigmouth.ticket4j.entity;

import org.apache.commons.lang.StringUtils;

public class CheckPassCodeResponse extends Response {

    private static final long serialVersionUID = 696082241899256800L;
    
    private String data = CHECK_FAIL;
    /** User input passcode */
    private String input;

    public boolean isContinue() {
        return StringUtils.equals(data, CHECK_OK);
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }
}
