package org.bigmouth.ticket4j.entity.request;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.bigmouth.ticket4j.Ticket4jDefaults;
import org.bigmouth.ticket4j.entity.Request;

public class ConfirmSingleForQueueRequest extends Request {

    private static final long serialVersionUID = -1523733273275888640L;

    private String passengerTicketStr;
    private String oldPassengerStr = "_ ";
    private String randCode;
    private String purposeCodes = Ticket4jDefaults.PURPOSE_CODE_00;
    private String keyCheckIsChange;
    private String leftTicketStr;
    private String trainLocation;
    private String repeatSubmitToken;

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

    public String getRandCode() {
        return randCode;
    }

    public void setRandCode(String randCode) {
        this.randCode = randCode;
    }

    public String getPurposeCodes() {
        return purposeCodes;
    }

    public void setPurposeCodes(String purposeCodes) {
        this.purposeCodes = purposeCodes;
    }

    public String getKeyCheckIsChange() {
        return keyCheckIsChange;
    }

    /**
     * @param keyCheckIsChange {@linkplain org.bigmouth.ticket4j.entity.Token#getOrderKey()}
     */
    public void setKeyCheckIsChange(String keyCheckIsChange) {
        this.keyCheckIsChange = keyCheckIsChange;
    }

    public String getLeftTicketStr() {
        return leftTicketStr;
    }

    /**
     * @param leftTicketStr {@linkplain org.bigmouth.ticket4j.entity.train.TrainDetails#getYp_info()}
     */
    public void setLeftTicketStr(String leftTicketStr) {
        this.leftTicketStr = leftTicketStr;
    }

    public String getTrainLocation() {
        return trainLocation;
    }

    /**
     * @param trainLocation {@linkplain org.bigmouth.ticket4j.entity.train.TrainDetails#getLocation_code()}
     */
    public void setTrainLocation(String trainLocation) {
        this.trainLocation = trainLocation;
    }

    public String getRepeatSubmitToken() {
        return repeatSubmitToken;
    }

    public void setRepeatSubmitToken(String repeatSubmitToken) {
        this.repeatSubmitToken = repeatSubmitToken;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
