package org.bigmouth.ticket4j.http;

import java.io.Serializable;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.ParseException;
import org.apache.http.message.BasicHeaderValueParser;

import com.google.common.base.Preconditions;

public class Ticket4jHeader implements Header, Serializable {

    private static final long serialVersionUID = 4007663717913377765L;
    private String name;
    private String value;

    /**
     * Constructor with name and value
     * 
     * @param name the header name
     * @param value the header value
     */
    public Ticket4jHeader(final String name, final String value) {
        super();
        if (name == null) {
            throw new IllegalArgumentException("Name may not be null");
        }
        this.name = name;
        this.value = value;
    }
    
    public Ticket4jHeader(final Header header) {
        Preconditions.checkNotNull(header, "header");
        this.name = header.getName();
        this.value = header.getValue();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public HeaderElement[] getElements() throws ParseException {
        if (this.value != null) {
            // result intentionally not cached, it's probably not used again
            return BasicHeaderValueParser.parseElements(this.value, null);
        } else {
            return new HeaderElement[] {};
        }
    }
}
