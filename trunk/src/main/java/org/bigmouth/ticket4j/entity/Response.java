/*
 * 文件名称: Response.java
 * 版权信息: Copyright 2013-2014 By Allen Hu. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: Allen.Hu
 * 修改日期: 2014-1-3
 * 修改内容: 
 */
package org.bigmouth.ticket4j.entity;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

/**
 * Response entity support abstract class.
 * 
 * @author Allen.Hu / 2014-8-15
 */
public abstract class Response implements Serializable {

    private static final long serialVersionUID = 5130678897561624363L;
    private static final Logger LOGGER = LoggerFactory.getLogger(Response.class); 
    public static final String CHECK_OK = "Y";
    public static final String CHECK_FAIL = "N";

    private String validateMessagesShowId;
    private boolean status;
    private int httpstatus;
    private List<String> messages = Lists.newArrayList();
    
    public abstract boolean isContinue();
    
    public void printMessage() {
        StringBuilder msg = new StringBuilder(64);
        for (String message : messages) {
            msg.append(message);
        }
        if (StringUtils.isNotEmpty(msg)) {
            if (LOGGER.isWarnEnabled()) {
                LOGGER.warn(msg.toString());
            }
        }
    }

    public String getValidateMessagesShowId() {
        return validateMessagesShowId;
    }

    public void setValidateMessagesShowId(String validateMessagesShowId) {
        this.validateMessagesShowId = validateMessagesShowId;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getHttpstatus() {
        return httpstatus;
    }

    public void setHttpstatus(int httpstatus) {
        this.httpstatus = httpstatus;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
