package org.bigmouth.ticket4j.cookie;

import org.bigmouth.ticket4j.http.Ticket4jHeader;


public interface CookieCache {

    void write(Ticket4jHeader[] headers, String id);
    
    Ticket4jHeader[] read(String id);
}
