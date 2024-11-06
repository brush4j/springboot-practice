package org.lyflexi.eventv5.event;

import lombok.Data;
import org.lyflexi.eventv5.entity.Person;

/**
 * @Description:
 * @Author: lyflexi
 * @project: springboot-practice
 * @Date: 2024/11/1 9:06
 */
@Data
public class PersonEvent extends BaseEvent<Person>{
    public PersonEvent(Person person, String addOrUpdate){
        super(person,addOrUpdate);
    }
}
