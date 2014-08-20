package org.bigmouth.ticket4j.entity;

public class Person {

    private Seat seat;
    private String name;
    private String card;

    /**
     * 车票类型：1 成人票 2 儿童票 3 学生票 4 残军票
     */
    private int ticketType = 1;

    public Person() {
        super();
    }

    public Person(String name, String card) {
        super();
        this.name = name;
        this.card = card;
    }

    public Person(String name, String card, int ticketType) {
        super();
        this.name = name;
        this.card = card;
        this.ticketType = ticketType;
    }

    public Person(Seat seat, String name, String card) {
        super();
        this.seat = seat;
        this.name = name;
        this.card = card;
    }

    public Person(Seat seat, String name, String card, int ticketType) {
        super();
        this.seat = seat;
        this.name = name;
        this.card = card;
        this.ticketType = ticketType;
    }

    public Seat getSeat() {
        return seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public int getTicketType() {
        return ticketType;
    }

    public void setTicketType(int ticketType) {
        this.ticketType = ticketType;
    }

    /**
     * @param seatTypes {@linkplain org.bigmouth.ticket4j.entity.train.TrainDetails#getSeat_types()}
     * @return
     */
    public String toPassengerTicketStr(String seatTypes) {
        return new StringBuilder(128).append(Seat.getSubmitSeatType(seat, seatTypes)).append(",").append("0,")
                .append(ticketType).append(",").append(name).append(",1,").append(card).append(",,N").toString();
    }

    public String toPassengerTicket() {
        return "_____";
    }

}
