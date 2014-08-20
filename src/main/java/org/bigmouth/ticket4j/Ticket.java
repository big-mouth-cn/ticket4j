package org.bigmouth.ticket4j;

import org.bigmouth.ticket4j.entity.request.QueryTicketRequest;
import org.bigmouth.ticket4j.entity.response.QueryTicketResponse;
import org.bigmouth.ticket4j.http.Ticket4jHttpResponse;


public interface Ticket {

    /**
     * Query given then <code>condition</code> of train tickets.
     * 
     * @param ticket4jHttpResponse
     * @param condition
     * @return
     */
    QueryTicketResponse query(Ticket4jHttpResponse ticket4jHttpResponse, QueryTicketRequest condition);
}
