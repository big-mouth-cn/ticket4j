package org.bigmouth.ticket4j.http;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.http.Header;

public class Ticket4jHttpResponse implements Serializable {

    private static final long serialVersionUID = -7322329548468003481L;

    private String jsessionid;

    private Header[] headers;

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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
