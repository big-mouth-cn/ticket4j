package org.bigmouth.ticket4j.entity;

import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.bigmouth.ticket4j.utils.CharsetUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

public class Person {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Person.class);

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
    public String toPassengerTicketStr(Seat seat, String seatTypes) {
        return new StringBuilder(128).append(Seat.getSubmitSeatType(seat, seatTypes)).append(",").append("0,")
                .append(ticketType).append(",").append(name).append(",1,").append(card).append(",,N").toString();
    }

    public String toPassengerTicket(Seat seat, String seatTypes) {
        return new StringBuilder(128).append(name).append(",1,").append(card).append(",").append(Seat.getSubmitSeatType(seat, seatTypes)).toString();
    }

    public static List<Person> of(String string, boolean convert) {
        List<Person> persons = Lists.newArrayList();
        if (StringUtils.isBlank(string)) {
            return persons;
        }
        String[] personsString = StringUtils.split(convert ? CharsetUtils.convert(string) : string, ",");
        if (ArrayUtils.isEmpty(personsString))
            return persons;
        for (String personString : personsString) {
            String[] items = StringUtils.split(personString, "_");
            
            String name = null, card = null, ticketType = null;
            if (items.length > 2) {
                name = items[0];
                card = items[1];
                ticketType = items[2];
            }
            else if (items.length > 1) {
                name = items[0];
                card = items[1];
            }
            else {
                LOGGER.warn("联系人 {} 有误!", personString);
                continue;
            }
            Person person = new Person(name, card);
            int ticketTypeValue = NumberUtils.toInt(ticketType);
            person.setTicketType(ticketTypeValue > 0 && ticketTypeValue < 5 ? ticketTypeValue : 1);
            persons.add(person);
        }
        return persons;
    }
    
    public static List<Person> of(String string) {
        return of(string, true);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(32);
        return sb.append(this.name).append("(").append(this.card).append(")").toString();
    }
}
