package org.bigmouth.ticket4j.entity;

import java.util.List;

import org.apache.commons.lang.StringUtils;



public enum Seat {

    SWZ, TDZ, YDZ, EDZ, GJRW, RW, YW, RZ, YZ, WZ;
    
    public static final Seat[] ALL = {SWZ, TDZ, YDZ, EDZ, GJRW, RW, YW, RZ, YZ, WZ};
    
    /**
     * 返回在提交订单时使用的席别常量
     * 
     * @param seat 需要购买的系别类型
     * @param seatType 该车次的系别类型
     * @return
     */
    public static String getSubmitSeatType(Seat seat, String seatTypes) {
        if (seat == Seat.SWZ) return "9";
        if (seat == Seat.TDZ) return "P";
        if (seat == Seat.YDZ) return StringUtils.indexOf(seatTypes, "M") != -1 ? "M" : "7";
        if (seat == Seat.EDZ) return StringUtils.indexOf(seatTypes, "O") != -1 ? "O" : "8";
        if (seat == Seat.GJRW) return "6";
        if (seat == Seat.RW) return "4";
        if (seat == Seat.YW) return "3";
        if (seat == Seat.RZ) return "2";
        if (seat == Seat.YZ) return "1";
        if (seat == Seat.WZ) return "1";
        return "1";
    }
    
    public static String getDescription(Seat seat) {
        if (seat == Seat.SWZ) return "商务座";
        if (seat == Seat.TDZ) return "特等座";
        if (seat == Seat.YDZ) return "一等(软)座";
        if (seat == Seat.EDZ) return "二等(软)座";
        if (seat == Seat.GJRW) return "高级软卧";
        if (seat == Seat.RW) return "软卧";
        if (seat == Seat.YW) return "硬卧";
        if (seat == Seat.RZ) return "软座";
        if (seat == Seat.YZ) return "硬座";
        if (seat == Seat.WZ) return "无座";
        return "其他";
    }
    
    public static String getDescription(List<Seat> seats) {
        StringBuilder string = new StringBuilder(64);
        for (Seat seat : seats) {
            string.append(getDescription(seat)).append(",");
        }
        return string.length() > 0 ? string.substring(0, string.length() - 1) : string.toString();
    }
    
    public static void main(String[] args) {
        System.out.println(Seat.SWZ.name());
    }
}
