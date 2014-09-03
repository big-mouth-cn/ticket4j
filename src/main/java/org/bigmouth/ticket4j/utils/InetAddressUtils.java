package org.bigmouth.ticket4j.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;


public final class InetAddressUtils {

    private InetAddressUtils() {
    }
    
    public static InetAddress[] getByAddress(final String ip) throws UnknownHostException {
        byte[] addr = toByte(ip);
        return new InetAddress[] { InetAddress.getByAddress(ip, addr) };
    }

    private static byte[] toByte(String ip) {
        String[] ipItems = ip.split("\\.");
        byte[] bytes = new byte[ipItems.length];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) (Integer.parseInt(ipItems[i]) & 0xff);
        }
        return bytes;
    }
}
