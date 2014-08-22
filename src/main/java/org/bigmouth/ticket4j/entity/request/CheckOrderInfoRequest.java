package org.bigmouth.ticket4j.entity.request;

import org.bigmouth.ticket4j.Ticket4jDefaults;
import org.bigmouth.ticket4j.entity.Request;

public class CheckOrderInfoRequest extends Request {

    private static final long serialVersionUID = -5796527594283471558L;

    private int cancelFlag = 2;
    private String bedLevelOrderNum = "000000000000000000000000000000";
    private String passengerTicketStr;
    private String oldPassengerStr = "_ _ _ _ _";
    private String tourFlag = Ticket4jDefaults.TOUR_FLAG_DC;
    private String randCode;
    private String jsonAtt;
    private String repeatSubmitToken;

    public int getCancelFlag() {
        return cancelFlag;
    }

    public void setCancelFlag(int cancelFlag) {
        this.cancelFlag = cancelFlag;
    }

    public String getBedLevelOrderNum() {
        return bedLevelOrderNum;
    }

    public void setBedLevelOrderNum(String bedLevelOrderNum) {
        this.bedLevelOrderNum = bedLevelOrderNum;
    }

    public String getPassengerTicketStr() {
        return passengerTicketStr;
    }

    public void setPassengerTicketStr(String passengerTicketStr) {
        this.passengerTicketStr = passengerTicketStr;
    }

    public String getOldPassengerStr() {
        return oldPassengerStr;
    }

    public void setOldPassengerStr(String oldPassengerStr) {
        this.oldPassengerStr = oldPassengerStr;
    }

    public String getTourFlag() {
        return tourFlag;
    }

    public void setTourFlag(String tourFlag) {
        this.tourFlag = tourFlag;
    }

    public String getRandCode() {
        return randCode;
    }

    public void setRandCode(String randCode) {
        this.randCode = randCode;
    }

    public String getJsonAtt() {
        return jsonAtt;
    }

    public void setJsonAtt(String jsonAtt) {
        this.jsonAtt = jsonAtt;
    }

    public String getRepeatSubmitToken() {
        return repeatSubmitToken;
    }

    public void setRepeatSubmitToken(String repeatSubmitToken) {
        this.repeatSubmitToken = repeatSubmitToken;
    }

}
