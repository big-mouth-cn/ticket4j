package org.bigmouth.ticket4j.entity.train;

import java.io.Serializable;


/**
 * Train.
 * 
 * @author Allen.Hu / 2014-8-15
 */
public class Train implements Serializable {

    private static final long serialVersionUID = 4504738960553138048L;

    /** Train detail */
    private TrainDetails queryLeftNewDTO;
    /** 秘钥串，在购买本趟车次时需要用到 */
    private String secretStr;
    /** 按钮描述 */
    private String buttonTextInfo;
    
    public TrainDetails getQueryLeftNewDTO() {
        return queryLeftNewDTO;
    }
    
    public void setQueryLeftNewDTO(TrainDetails queryLeftNewDTO) {
        this.queryLeftNewDTO = queryLeftNewDTO;
    }
    
    public String getSecretStr() {
        return secretStr;
    }
    
    public void setSecretStr(String secretStr) {
        this.secretStr = secretStr;
    }
    
    public String getButtonTextInfo() {
        return buttonTextInfo;
    }
    
    public void setButtonTextInfo(String buttonTextInfo) {
        this.buttonTextInfo = buttonTextInfo;
    }

    @Override
    public String toString() {
        return new StringBuilder().append(getQueryLeftNewDTO()).append("\t").append(getButtonTextInfo()).toString();
    }
}
