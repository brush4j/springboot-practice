# event-v5

最后这个版本是spring官方描述的tedious版本，也是公司内或者大多数人用的比较不优雅的泛型事件解决方案

> https://docs.spring.io/spring-framework/reference/core/beans/context-introduction.html#context-functionality-events-generics

Due to type erasure, this works only if the event that is fired resolves the generic parameters on which the event listener filters (that is, something like `class PersonCreatedEvent extends EntityCreatedEvent<Person> { … }`).

```java
@Data
@NoArgsConstructor
public class BaseEvent<T>  {
    private T data;
    private String addOrUpdate;

    public BaseEvent(T data, String addOrUpdate) {
        this.data = data;
        this.addOrUpdate = addOrUpdate;
    }

}

PersonEvent extends BaseEvent<Person>{}

OrderEvent extends BaseEvent<Order>{}
```


In certain circumstances, this may become quite tedious if all events follow the same structure (as should be the case for the event in the preceding example). 