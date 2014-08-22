package org.bigmouth.ticket4j.entity.response;

import org.bigmouth.ticket4j.entity.Response;
import org.bigmouth.ticket4j.http.Ticket4jHttpResponse;

public class CheckUserResponse extends Response {

    private static final long serialVersionUID = 3483014189041078617L;

    private CheckUserResult data = new CheckUserResult();
    private Ticket4jHttpResponse ticket4jHttpResponse;

    public class CheckUserResult {

        private boolean flag;

        public boolean isFlag() {
            return flag;
        }

        public void setFlag(boolean flag) {
            this.flag = flag;
        }
    }

    public CheckUserResult getData() {
        return data;
    }

    public void setData(CheckUserResult data) {
        this.data = data;
    }

    public Ticket4jHttpResponse getTicket4jHttpResponse() {
        return ticket4jHttpResponse;
    }

    public void setTicket4jHttpResponse(Ticket4jHttpResponse ticket4jHttpResponse) {
        this.ticket4jHttpResponse = ticket4jHttpResponse;
    }

    @Override
    public boolean isContinue() {
        return this.data.isFlag();
    }
}
