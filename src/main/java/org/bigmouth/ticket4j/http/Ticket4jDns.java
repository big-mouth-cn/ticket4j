package org.bigmouth.ticket4j.http;

import java.net.InetAddress;
import java.util.Comparator;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class Ticket4jDns implements Comparator<Ticket4jDns> {

    private String ip;
    private InetAddress[] inetAddresses;
    private long consumeTimeInMillis;
    private boolean timeout = true;

    public Ticket4jDns() {
        super();
    }

    public Ticket4jDns(String ip, InetAddress[] inetAddresses, long consumeTimeInMillis) {
        super();
        this.ip = ip;
        this.inetAddresses = inetAddresses;
        this.consumeTimeInMillis = consumeTimeInMillis;
    }

    @Override
    public int compare(Ticket4jDns o1, Ticket4jDns o2) {
        return o1.getConsumeTimeInMillis() > o2.getConsumeTimeInMillis() ? 1 : (o1.getConsumeTimeInMillis() == o2.getConsumeTimeInMillis()) ? 0 : -1;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public InetAddress[] getInetAddresses() {
        return inetAddresses;
    }

    public void setInetAddresses(InetAddress[] inetAddresses) {
        this.inetAddresses = inetAddresses;
    }

    public long getConsumeTimeInMillis() {
        return consumeTimeInMillis;
    }

    public void setConsumeTimeInMillis(long consumeTimeInMillis) {
        this.consumeTimeInMillis = consumeTimeInMillis;
    }

    public boolean isTimeout() {
        return timeout;
    }

    public void setTimeout(boolean timeout) {
        this.timeout = timeout;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
