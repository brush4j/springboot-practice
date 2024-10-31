package org.lyflexi.eventv1.event;

import lombok.Data;
import org.lyflexi.eventv1.entity.Person;

/**
 * @Description:
 * @Author: lyflexi
 * @project: springboot-practice
 * @Date: 2024/10/31 10:55
 */
@Data
public class PersonEvent {

    private Person person;

    private String addOrUpdate;

    public PersonEvent(Person person, String addOrUpdate) {
        this.person = person;
        this.addOrUpdate = addOrUpdate;
    }
}