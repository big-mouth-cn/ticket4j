package org.bigmouth.ticket4j;

import org.bigmouth.framework.core.SpringContextHolder;
import org.bigmouth.ticket4j.utils.JVMUtils;


public class TicketStartup {
    
    public static void main(String[] args) {
        JVMUtils.bootUsingSpring(new String[] {
                "config/applicationContext.xml"
        }, args);
        TicketProcess process = SpringContextHolder.getBean("ticketProcess");
        process.start();
    }
}
