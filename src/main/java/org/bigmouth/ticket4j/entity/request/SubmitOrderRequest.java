package org.bigmouth.ticket4j.entity.request;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.bigmouth.ticket4j.Ticket4jDefaults;
import org.bigmouth.ticket4j.entity.Request;
import org.bigmouth.ticket4j.entity.train.Train;

public class SubmitOrderRequest extends Request {

    private static final long serialVersionUID = -6168337289295454691L;

    private String trainDate;
    private String backTrainDate;
    private String tourFlag = Ticket4jDefaults.TOUR_FLAG_DC;
    private String purposeCodes = Ticket4jDefaults.PURPOSE_CODE_ADULT;

    /** 将预订的车次 */
    private Train train;

    public SubmitOrderRequest() {
    }

    public SubmitOrderRequest(String trainDate, String backTrainDate, String purposeCodes, Train train) {
        this.trainDate = trainDate;
        this.backTrainDate = backTrainDate;
        this.purposeCodes = purposeCodes;
        this.train = train;
    }

    public String getTrainDate() {
        return trainDate;
    }

    public void setTrainDate(String trainDate) {
        this.trainDate = trainDate;
    }

    public String getBackTrainDate() {
        return backTrainDate;
    }

    public void setBackTrainDate(String backTrainDate) {
        this.backTrainDate = backTrainDate;
    }

    public String getTourFlag() {
        return tourFlag;
    }

    public void setTourFlag(String tourFlag) {
        this.tourFlag = tourFlag;
    }

    public String getPurposeCodes() {
        return purposeCodes;
    }

    public void setPurposeCodes(String purposeCodes) {
        this.purposeCodes = purposeCodes;
    }

    public Train getTrain() {
        return train;
    }

    public void setTrain(Train train) {
        this.train = train;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
