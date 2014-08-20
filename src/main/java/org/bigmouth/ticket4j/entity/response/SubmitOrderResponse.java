package org.bigmouth.ticket4j.entity.response;

import org.bigmouth.ticket4j.entity.Response;


public class SubmitOrderResponse extends Response {

    private static final long serialVersionUID = 423482551600759764L;

    @Override
    public boolean isContinue() {
        return isStatus();
    }
}
