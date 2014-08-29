package org.bigmouth.ticket4j.entity.response;

import java.util.List;

import org.bigmouth.ticket4j.entity.Passenger;
import org.bigmouth.ticket4j.entity.Person;
import org.bigmouth.ticket4j.entity.Response;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;

public class QueryPassengerResponse extends Response {

    private static final long serialVersionUID = 6757744617360601970L;

    private QueryPassenger data = new QueryPassenger();

    public class QueryPassenger {

        private List<Passenger> datas = Lists.newArrayList();

        public List<Passenger> getDatas() {
            return datas;
        }

        public void setDatas(List<Passenger> datas) {
            this.datas = datas;
        }
    }

    public QueryPassenger getData() {
        return data;
    }

    public void setData(QueryPassenger data) {
        this.data = data;
    }
    
    public boolean contains(List<Person> persons, Person invalid) {
        if (CollectionUtils.isEmpty(persons))
            return false;
        for (Person person : persons) {
            boolean flag = contains(person);
            if (!flag) {
                invalid.setName(person.getName());
                invalid.setCard(person.getCard());
                return false;
            }
        }
        return true;
    }
    
    public boolean contains(Person person) {
        if (null == person)
            return false;
        if (CollectionUtils.isEmpty(this.getData().datas)) {
            return false;
        }
        for (Passenger passenger : this.getData().datas) {
            if (passenger.equals(person))
                return true;
        }
        return false;
    }

    @Override
    public boolean isContinue() {
        return false;
    }
}
