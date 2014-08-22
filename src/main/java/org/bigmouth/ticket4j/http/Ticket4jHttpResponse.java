package org.bigmouth.ticket4j.http;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.http.Header;

public class Ticket4jHttpResponse implements Serializable {

    private static final long serialVersionUID = -7322329548468003481L;

    private String jsessionid;
    private Header[] headers;
    /** 是否已经登录 */
    private boolean signIn;

    public Ticket4jHttpResponse() {
        super();
    }

    public Ticket4jHttpResponse(Header[] headers) {
        super();
        this.headers = headers;
    }

    public String getJsessionid() {
        return jsessionid;
    }

    public void setJsessionid(String jsessionid) {
        this.jsessionid = jsessionid;
    }

    public Header[] getHeaders() {
        return headers;
    }

    public void setHeaders(Header[] headers) {
        this.headers = headers;
    }

    public boolean isSignIn() {
        return signIn;
    }

    public void setSignIn(boolean signIn) {
        this.signIn = signIn;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
