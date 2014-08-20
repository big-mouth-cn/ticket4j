package org.bigmouth.ticket4j.entity.order;

public class Passenger {

    private String passenger_name;
    private String passenger_id_type_code;
    private String passenger_id_type_name;
    private String passenger_id_no;
    private int total_times;

    public String getPassenger_name() {
        return passenger_name;
    }

    public void setPassenger_name(String passenger_name) {
        this.passenger_name = passenger_name;
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

    public int getTotal_times() {
        return total_times;
    }

    public void setTotal_times(int total_times) {
        this.total_times = total_times;
    }

}
