官方推荐的优雅的泛型事件解决方案

参考官方文档：> https://docs.spring.io/spring-framework/reference/core/beans/context-introduction.html#context-functionality-events-generics


其实也可以参考Spring源码ApplicationEventPublisher#publishEvent, 他说

<p>If the specified {@code event} is not an {@link ApplicationEvent},it is wrapped in a {@link PayloadApplicationEvent}.

也就是说，如果特定的事件不是ApplicationEvent类型，那么Spring会将其包装为PayloadApplicationEvent类型。

```java
@FunctionalInterface
public interface ApplicationEventPublisher {
    
	default void publishEvent(ApplicationEvent event) {
		publishEvent((Object) event);
	}

	/**
	 * Notify all <strong>matching</strong> listeners registered with this
	 * application of an event.
	 * <p>If the specified {@code event} is not an {@link ApplicationEvent},
	 * it is wrapped in a {@link PayloadApplicationEvent}.
	 * <p>Such an event publication step is effectively a hand-off to the
	 * multicaster and does not imply synchronous/asynchronous execution
	 * or even immediate execution at all. Event listeners are encouraged
	 * to be as efficient as possible, individually using asynchronous
	 * execution for longer-running and potentially blocking operations.
	 * @param event the event to publish
	 * @since 4.2
	 * @see #publishEvent(ApplicationEvent)
	 * @see PayloadApplicationEvent
	 */
	void publishEvent(Object event);

}

```

因此我们可以参考PayloadApplicationEvent，通过实现ResolvableTypeProvider接口的getResolvableType方法来实现我们自己的BaseEvent

```java

package org.springframework.context;

import java.util.function.Consumer;

import org.springframework.core.ResolvableType;
import org.springframework.core.ResolvableTypeProvider;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * An {@link ApplicationEvent} that carries an arbitrary payload.
 *
 * @author Stephane Nicoll
 * @author Juergen Hoeller
 * @author Qimiao Chen
 * @since 4.2
 * @param <T> the payload type of the event
 * @see ApplicationEventPublisher#publishEvent(Object)
 * @see ApplicationListener#forPayload(Consumer)
 */
@SuppressWarnings("serial")
public class PayloadApplicationEvent<T> extends ApplicationEvent implements ResolvableTypeProvider {

	private final T payload;

	private final ResolvableType payloadType;

	/**
	 * Create a new PayloadApplicationEvent.
	 * @param source the object on which the event initially occurred (never {@code null})
	 * @param payload the payload object (never {@code null})
	 * @param payloadType the type object of payload object (can be {@code null})
	 * @since 6.0
	 */
	public PayloadApplicationEvent(Object source, T payload, @Nullable ResolvableType payloadType) {
		super(source);
		Assert.notNull(payload, "Payload must not be null");
		this.payload = payload;
		this.payloadType = (payloadType != null) ? payloadType : ResolvableType.forInstance(payload);
	}

	/**
	 * Create a new PayloadApplicationEvent, using the instance to infer its type.
	 * @param source the object on which the event initially occurred (never {@code null})
	 * @param payload the payload object (never {@code null})
	 */
	public PayloadApplicationEvent(Object source, T payload) {
		this(source, payload, null);
	}


	@Override
	public ResolvableType getResolvableType() {
		return ResolvableType.forClassWithGenerics(getClass(), this.payloadType);
	}

	/**
	 * Return the payload of the event.
	 */
	public T getPayload() {
		return this.payload;
	}

}

```

即BaseEvent，实现接口ResolvableTypeProvider
```java
    @Override
    public ResolvableType getResolvableType() {
        return ResolvableType.forClassWithGenerics(getClass(), ResolvableType.forInstance(getData()));
    }
```