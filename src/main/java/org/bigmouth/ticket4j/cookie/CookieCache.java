package org.bigmouth.ticket4j.cookie;

import org.apache.http.Header;


public interface CookieCache {

    void write(Header[] headers, String id);
    
    Header[] read(String id);
}
