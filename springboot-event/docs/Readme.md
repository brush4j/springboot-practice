形如下面的发布消息的代码：
```java
@RestController
@Slf4j
public class MockController {
    @Autowired
    ApplicationContext applicationContext;
    
    @GetMapping("/publishEvent")
    public void publishEvent(){
        applicationContext.publishEvent(new BaseEvent<>(new Order("order"),"add"));
//        applicationContext.publishEvent(new BaseEvent<>(new Person("person"),"add"));
    }
}
```
new出来的`BaseEvent<>(new Order("order"),"add")`或者是`BaseEvent<>(new Person("person"),"add")`,在编译时泛型信息Order和Person会被擦除

这也是为什么下面的监听逻辑没有生效的原因：
```java
@Slf4j
@Component
public class EventListenerService {

    @EventListener
    public void handlePersonEvent(BaseEvent<Person> baseEvent) {
        log.info("监听到PersonEvent: {}", baseEvent);
    }

    @EventListener
    public void handleOrderEvent(BaseEvent<Order> baseEvent) {
        log.info("监听到OrderEvent: {}", baseEvent);
    }
}

```

Spring在泛型情况下的事件处理解决方案如下：

> https://docs.spring.io/spring-framework/reference/core/beans/context-introduction.html#context-functionality-events-generics

# 实现接口ResolvableTypeProvider
```java
@Data
public class BaseEvent<T>  implements ResolvableTypeProvider {
    private T data;
    private String addOrUpdate;

    public BaseEvent(T data, String addOrUpdate) {
        this.data = data;
        this.addOrUpdate = addOrUpdate;
    }

    @Override
    public ResolvableType getResolvableType() {
        return ResolvableType.forClassWithGenerics(getClass(), ResolvableType.forInstance(getData()));
    }
}
```
# 断点调试
## 在方法实现的入口处打上断点
![img_1.png](img_1.png)

跟踪堆栈信息，定位到ResolvableType.java, 在静态方法forInstance中找到了ResolvableTypeProvider#getResolvableType方法的调用
![img.png](img.png)

弹栈追踪 PayloadApplicationEvent.java用于保存通过ResolvableType.forInstance生成的带有泛型信息的event

- private final ResolvableType payloadType;此时为org.lyflexi.eventv4.event.BaseEvent<org.lyflexi.eventv3.entity.Order>

![img_2.png](img_2.png)

弹栈追踪AbstractApplicationContext.java

![img_3.png](img_3.png)
这里的PayloadApplicationEvent又对org.lyflexi.eventv4.event.BaseEvent<org.lyflexi.eventv3.entity.Order>通过接口ResolvableTypeProvider包装了一层

因此PayloadApplicationEvent#getResolvableType()返回的是：

- org.springframework.context.PayloadApplicationEvent<org.lyflexi.eventv4.event.BaseEvent<org.lyflexi.eventv3.entity.Order>>

```shell
org.springframework.context.PayloadApplicationEvent<org.lyflexi.eventv4.event.BaseEvent<org.lyflexi.eventv3.entity.Order>>
```
![img_4.png](img_4.png)
解决了泛型擦除了问题


# 如果说不实现接口ResolvableTypeProvider再次调试
```java
@Data
public class BaseEvent<T>  /*implements ResolvableTypeProvider */{
    private T data;
    private String addOrUpdate;

    public BaseEvent(T data, String addOrUpdate) {
        this.data = data;
        this.addOrUpdate = addOrUpdate;
    }

/*    @Override
    public ResolvableType getResolvableType() {
        return ResolvableType.forClassWithGenerics(getClass(), ResolvableType.forInstance(getData()));
    }*/
}
```
发现这里的泛型信息Order被擦除成？了，这也就是泛型event不生效的原因
```shell
org.springframework.context.PayloadApplicationEvent<org.lyflexi.eventv4.event.BaseEvent<?>>
```
![img_5.png](img_5.png)

继续退栈，来到事件发布的入口处，发现spring一直加工的就是我们publish的BaseEvent<Order>

正常情况下泛型信息Order是会被擦除的

但是经过spring上述一系列的操作之后，泛型信息Order保留了下来

![img_6.png](img_6.png)

# 话说回来，进一步探究我们`BaseEvent<T>`的实现中到底做了什么？
BaseEvent<T>#getResolvableType
```java
    @Override
    public ResolvableType getResolvableType() {
        return ResolvableType.forClassWithGenerics(getClass(), ResolvableType.forInstance(getData()));
    }
```
- ResolvableType.forInstance(getData())
- ResolvableType.forClassWithGenerics
## 1.ResolvableType.forInstance(getData())

就是返回了目标泛型的具体类型：Order
## 2.ResolvableType.forClassWithGenerics

为我们创建好带有目标泛型的BaseEvent<Order>：org.lyflexi.eventv4.event.BaseEvent<org.lyflexi.eventv3.entity.Order>

所以彻底清楚了，在事件发布的时候，spring对带有泛型的event进行了偷天换日，避免了Java泛型信息的擦除

此后就按照spring正常的事件发布流程往下走，本文就不再赘述...
