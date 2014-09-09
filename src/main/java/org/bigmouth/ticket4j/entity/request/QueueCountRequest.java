package org.bigmouth.ticket4j.entity.request;

import org.bigmouth.ticket4j.entity.Request;
import org.bigmouth.ticket4j.entity.Token;
import org.bigmouth.ticket4j.entity.train.TrainDetails;

public class QueueCountRequest extends Request {

    private static final long serialVersionUID = -4592833905244102209L;

    private String trainDate;
    private TrainDetails trainDetails;
    private Token token;

    public String getTrainDate() {
        return trainDate;
    }

    public void setTrainDate(String trainDate) {
        this.trainDate = trainDate;
    }

    public TrainDetails getTrainDetails() {
        return trainDetails;
    }

    public void setTrainDetails(TrainDetails trainDetails) {
        this.trainDetails = trainDetails;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }
}
