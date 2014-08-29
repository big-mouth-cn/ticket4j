package org.bigmouth.ticket4j.entity;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;


public class Passenger implements Serializable {

    private static final long serialVersionUID = 8089089170056540612L;

    private String code;
    /** 姓名 */
    private String passenger_name;
    /** 性别 */
    private String sex_name;
    private String born_date;
    /** ID类型 */
    private String passenger_id_type_code;
    /** ID名称 */
    private String passenger_id_type_name;
    /** 号码 */
    private String passenger_id_no;
    private String passenger_type;
    private String passenger_flag;
    /** 成人 */
    private String passenger_type_name;
    private String first_letter;
    private String recordCount;
    
    public boolean equals(Person person) {
        if (null == person)
            return false;
        if (StringUtils.equals(person.getName(), this.getPassenger_name()) &&
                StringUtils.equals(person.getCard(), this.getPassenger_id_no()))
            return true;
        return false;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getPassenger_name() {
        return passenger_name;
    }
    
    public void setPassenger_name(String passenger_name) {
        this.passenger_name = passenger_name;
    }
    
    public String getSex_name() {
        return sex_name;
    }
    
    public void setSex_name(String sex_name) {
        this.sex_name = sex_name;
    }
    
    public String getBorn_date() {
        return born_date;
    }
    
    public void setBorn_date(String born_date) {
        this.born_date = born_date;
    }
    
    public String getPassenger_id_type_code() {
        return passenger_id_type_code;
    }
    
    public void setPassenger_id_type_code(String passenger_id_type_code) {
        this.passenger_id_type_code = passenger_id_type_code;
    }
    
    public String getPassenger_id_type_name() {
        return passenger_id_type_name;
    }
    
    public void setPassenger_id_type_name(String passenger_id_type_name) {
        this.passenger_id_type_name = passenger_id_type_name;
    }
    
    public String getPassenger_id_no() {
        return passenger_id_no;
    }
    
    public void setPassenger_id_no(String passenger_id_no) {
        this.passenger_id_no = passenger_id_no;
    }
    
    public String getPassenger_type() {
        return passenger_type;
    }
    
    public void setPassenger_type(String passenger_type) {
        this.passenger_type = passenger_type;
    }
    
    public String getPassenger_flag() {
        return passenger_flag;
    }
    
    public void setPassenger_flag(String passenger_flag) {
        this.passenger_flag = passenger_flag;
    }
    
    public String getPassenger_type_name() {
        return passenger_type_name;
    }
    
    public void setPassenger_type_name(String passenger_type_name) {
        this.passenger_type_name = passenger_type_name;
    }
    
    public String getFirst_letter() {
        return first_letter;
    }
    
    public void setFirst_letter(String first_letter) {
        this.first_letter = first_letter;
    }
    
    public String getRecordCount() {
        return recordCount;
    }
    
    public void setRecordCount(String recordCount) {
        this.recordCount = recordCount;
    }
}
