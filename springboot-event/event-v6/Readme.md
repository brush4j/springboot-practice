Spring所有的监听器都被注册为集合，事件触发的时候Spring会按照集合顺序同步通知监听器，过程中可能会由于某个监听器业务长耗时，导致阻塞其他的监听器。

## 笨方法@Async异步消费
一种解决办法是异步消费，在每个监听器处都指定@Async注解，并开启异步功能@EnableAsync，这种办法太不优雅了
```java
@Async("指定自定义线程池")
@EventListener
public void handleEvent(CustomEvent customerEvent) {
  log.info("EventListenerService2监听到MyCustomEvent: {}", customerEvent);
}
```

我们自定义了两个线程池用来测试，分别是taskExecutor1和fakeTaskExecutor，然后使用@Async注解来消费，但是事件发布的时候控制台有提示：

为什么Spring的@Async注解非要名字为taskExecutor的线程池呢！我定义了两个taskExecutor1和fakeTaskExecutor都不用
```shell
2025-05-17T20:04:13.591+08:00  INFO 16744 --- [cutor1-thread-2] .s.a.AnnotationAsyncExecutionInterceptor : More than one TaskExecutor bean found within the context, and none is named 'taskExecutor'. Mark one of them as primary or name it 'taskExecutor' (possibly as an alias) in order to use it for async processing: [taskExecutor1, fakeTaskExecutor]
2025-05-17T20:04:13.593+08:00  INFO 16744 --- [cTaskExecutor-2] o.l.e.listener.EventListenerService2     : EventListenerService2监听到MyCustomEvent: org.lyflexi.eventv6.event.CustomEvent[source=str]
2025-05-17T20:04:13.593+08:00  INFO 16744 --- [cTaskExecutor-1] o.l.e.listener.EventListenerService1     : EventListenerService1监听到MyCustomEvent: org.lyflexi.eventv6.event.CustomEvent[source=str]
2025-05-17T20:04:13.594+08:00  INFO 16744 --- [cTaskExecutor-3] o.l.e.listener.EventListenerService3     : EventListenerService3监听到MyCustomEvent: org.lyflexi.eventv6.event.CustomEvent[source=str]
2025-05-17T20:04:23.609+08:00  INFO 16744 --- [cTaskExecutor-1] o.l.e.listener.EventListenerService1     : EventListenerService1处理完MyCustomEvent: org.lyflexi.eventv6.event.CustomEvent[source=str]
```

控制台显示只有cTaskExecutor线程，其实cTaskExecutor线程对应于线程池SimpleAsyncTaskExecutor

SimpleAsyncTaskExecutor线程池是当Spring找不到用户的线程池的时候Sping给我们默认兜底创建的线程池，自己可以看看源码
```java
	@Override
	@Nullable
	protected Executor getDefaultExecutor(@Nullable BeanFactory beanFactory) {
		Executor defaultExecutor = super.getDefaultExecutor(beanFactory);
		return (defaultExecutor != null ? defaultExecutor : new SimpleAsyncTaskExecutor());
	}
```

说明getDefaultExecutor为空，顺着源码找答案AnnotationAsyncExecutionInterceptor-->AsyncExecutionInterceptor->AsyncExecutionAspectSupport

有这么一段代码
```java
    @Nullable
    protected Executor getDefaultExecutor(@Nullable BeanFactory beanFactory) {
        if (beanFactory != null) {
            try {
                return (Executor)beanFactory.getBean(TaskExecutor.class);
            } catch (NoUniqueBeanDefinitionException ex) {
                this.logger.debug("Could not find unique TaskExecutor bean. Continuing search for an Executor bean named 'taskExecutor'", ex);

                try {
                    return (Executor)beanFactory.getBean("taskExecutor", Executor.class);
                } catch (NoSuchBeanDefinitionException var4) {
                    if (this.logger.isInfoEnabled()) {
                        this.logger.info("More than one TaskExecutor bean found within the context, and none is named 'taskExecutor'. Mark one of them as primary or name it 'taskExecutor' (possibly as an alias) in order to use it for async processing: " + ex.getBeanNamesFound());
                    }
                }
            } catch (NoSuchBeanDefinitionException ex) {
                this.logger.debug("Could not find default TaskExecutor bean. Continuing search for an Executor bean named 'taskExecutor'", ex);

                try {
                    return (Executor)beanFactory.getBean("taskExecutor", Executor.class);
                } catch (NoSuchBeanDefinitionException var5) {
                    this.logger.info("No task executor bean found for async processing: no bean of type TaskExecutor and no bean named 'taskExecutor' either");
                }
            }
        }

        return null;
    }
```

意思是优先找TaskExecutor.class类型的Bean，我看了下我们定义的TaskExecutor确实是Spring自己家的ThreadPoolTaskExecutor，那问题出在哪里了呢？
```java
package org.springframework.core.task;
```

原来我们定义了多个ThreadPoolTaskExecutor，导致这行代码抛了异常：
```java
return (Executor)beanFactory.getBean(TaskExecutor.class);

No qualifying bean of type 'org.springframework.core.task.TaskExecutor' available: expected single matching bean but found 2: taskExecutor1,fakeTaskExecutor
```

下面进入Spring的异常处理，接下来就必须找到名字为taskExecutor的线程池
```java
            } catch (NoUniqueBeanDefinitionException ex) {
                this.logger.debug("Could not find unique TaskExecutor bean. Continuing search for an Executor bean named 'taskExecutor'", ex);

                try {
                    return (Executor)beanFactory.getBean("taskExecutor", Executor.class);
                } catch (NoSuchBeanDefinitionException var4) {
                    if (this.logger.isInfoEnabled()) {
                        this.logger.info("More than one TaskExecutor bean found within the context, and none is named 'taskExecutor'. Mark one of them as primary or name it 'taskExecutor' (possibly as an alias) in order to use it for async processing: " + ex.getBeanNamesFound());
                    }
                }
            }
```

所以解决办法就是
- 要么是将taskExecutor1重命名为taskExecutor
- 要么是将taskExecutor1指定为@Primary


## @Async注解源码分析
分析前先声明，@Aysnc也属于切面源码，切面源码分两种：
- 动态匹配
- 静态匹配

@Async注解就属于静态匹配，对比需要动态匹配的场景
```java
  // 动态匹配的例子 - 需要在运行时判断
  @Aspect
  public class DynamicAspect {
    @Around("execution(* *.*(..)) && args(name)")  // 参数名为name
    public Object around(String name) {  // 需要在运行时检查参数
      if (name != null) {  // 动态判断
        // 处理逻辑
      }
    }
  }
  
  // 静态匹配的例子 - 编译时就能确定
  @Aspect
  public class StaticAspect {
    @Around("execution(* com.example.UserService.*(..))") // 固定的类和方法
    public Object around(ProceedingJoinPoint pjp) {
      // 直接执行，因为已经知道要拦截的是UserService的所有方法
    }
  }
   
  // @Async不需要这种动态检查
  @Async
  public void process() {
     // 方法声明就足够了
  }
```

@Async是静态匹配的关键原因是：
- 异步执行是一个**二元判断**（要么异步，要么不异步）
- 这个判断**完全基于方法的声明**
- 不依赖于：
    - 方法参数
    - 运行时条件
    - 上下文状态

所以运行请求的时候程序的断点就来到了下面这里ReflectiveMethodInvocation#proceed()最下面的else
```java
	@Override
	@Nullable
	public Object proceed() throws Throwable {
		// We start with an index of -1 and increment early.
		if (this.currentInterceptorIndex == this.interceptorsAndDynamicMethodMatchers.size() - 1) {
			return invokeJoinpoint();
		}

		Object interceptorOrInterceptionAdvice =
				this.interceptorsAndDynamicMethodMatchers.get(++this.currentInterceptorIndex);
		if (interceptorOrInterceptionAdvice instanceof InterceptorAndDynamicMethodMatcher dm) {
			// Evaluate dynamic method matcher here: static part will already have
			// been evaluated and found to match.
			Class<?> targetClass = (this.targetClass != null ? this.targetClass : this.method.getDeclaringClass());
			if (dm.methodMatcher.matches(this.method, targetClass, this.arguments)) {
				return dm.interceptor.invoke(this);
			}
			else {
				// Dynamic matching failed.
				// Skip this interceptor and invoke the next in the chain.
				return proceed();
			}
		}
		else {
			// It's an interceptor, so we just invoke it: The pointcut will have
			// been evaluated statically before this object was constructed.
			return ((MethodInterceptor) interceptorOrInterceptionAdvice).invoke(this);
		}
	}
```

接下来跟着进入
```java
		else {
			// It's an interceptor, so we just invoke it: The pointcut will have
			// been evaluated statically before this object was constructed.
			return ((MethodInterceptor) interceptorOrInterceptionAdvice).invoke(this);
		}
```
来到了AsyncExecutionInterceptor#invoke
```java
	@Override
	@Nullable
	public Object invoke(final MethodInvocation invocation) throws Throwable {
		Class<?> targetClass = (invocation.getThis() != null ? AopUtils.getTargetClass(invocation.getThis()) : null);
		Method specificMethod = ClassUtils.getMostSpecificMethod(invocation.getMethod(), targetClass);
		final Method userDeclaredMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);

		AsyncTaskExecutor executor = determineAsyncExecutor(userDeclaredMethod);
		if (executor == null) {
			throw new IllegalStateException(
					"No executor specified and no default executor set on AsyncExecutionInterceptor either");
		}

		Callable<Object> task = () -> {
			try {
				Object result = invocation.proceed();
				if (result instanceof Future) {
					return ((Future<?>) result).get();
				}
			}
			catch (ExecutionException ex) {
				handleError(ex.getCause(), userDeclaredMethod, invocation.getArguments());
			}
			catch (Throwable ex) {
				handleError(ex, userDeclaredMethod, invocation.getArguments());
			}
			return null;
		};

		return doSubmit(task, executor, invocation.getMethod().getReturnType());
	}
```

可以清楚的看到AsyncTaskExecutor executor = determineAsyncExecutor(userDeclaredMethod);

这行代码就与最开始选择Spring异步线程池的逻辑对应上了
```java
```java
    @Nullable
    protected Executor getDefaultExecutor(@Nullable BeanFactory beanFactory) {
        if (beanFactory != null) {
            try {
                return (Executor)beanFactory.getBean(TaskExecutor.class);
            } catch (NoUniqueBeanDefinitionException ex) {
                this.logger.debug("Could not find unique TaskExecutor bean. Continuing search for an Executor bean named 'taskExecutor'", ex);

                try {
                    return (Executor)beanFactory.getBean("taskExecutor", Executor.class);
                } catch (NoSuchBeanDefinitionException var4) {
                    if (this.logger.isInfoEnabled()) {
                        this.logger.info("More than one TaskExecutor bean found within the context, and none is named 'taskExecutor'. Mark one of them as primary or name it 'taskExecutor' (possibly as an alias) in order to use it for async processing: " + ex.getBeanNamesFound());
                    }
                }
            } catch (NoSuchBeanDefinitionException ex) {
                this.logger.debug("Could not find default TaskExecutor bean. Continuing search for an Executor bean named 'taskExecutor'", ex);

                try {
                    return (Executor)beanFactory.getBean("taskExecutor", Executor.class);
                } catch (NoSuchBeanDefinitionException var5) {
                    this.logger.info("No task executor bean found for async processing: no bean of type TaskExecutor and no bean named 'taskExecutor' either");
                }
            }
        }

        return null;
    }
```

请求时的异步源码已经分析完了，最后让我们回过头往前分析下程序运行时的Spring异步注解源码吧
```java
    @EventListener
    @Async //自定义的publishTaskExecutor-thread-生效
    public void handleEvent(CustomEvent customerEvent) {
        log.info("EventListenerService3监听到MyCustomEvent: {}", customerEvent);
    }
```
跟着AsyncExecutionInterceptor的构造函数一路可以回查出，Spring容器启动的时候会初始化AsyncAnnotationBeanPostProcessor

AsyncAnnotationBeanPostProcessor在setBeanFactory的时候，会创建AsyncAnnotationAdvisor
```java
	@Override
public void setBeanFactory(BeanFactory beanFactory) {
  super.setBeanFactory(beanFactory);

  AsyncAnnotationAdvisor advisor = new AsyncAnnotationAdvisor(this.executor, this.exceptionHandler);
  if (this.asyncAnnotationType != null) {
    advisor.setAsyncAnnotationType(this.asyncAnnotationType);
  }
  advisor.setBeanFactory(beanFactory);
  this.advisor = advisor;
}
```

AsyncAnnotationAdvisorc创建的时候会构建this.advice = buildAdvice(executor, exceptionHandler);
```java
	public AsyncAnnotationAdvisor(
			@Nullable Supplier<Executor> executor, @Nullable Supplier<AsyncUncaughtExceptionHandler> exceptionHandler) {

		Set<Class<? extends Annotation>> asyncAnnotationTypes = new LinkedHashSet<>(2);
		asyncAnnotationTypes.add(Async.class);

		ClassLoader classLoader = AsyncAnnotationAdvisor.class.getClassLoader();
		try {
			asyncAnnotationTypes.add((Class<? extends Annotation>)
					ClassUtils.forName("jakarta.ejb.Asynchronous", classLoader));
		}
		catch (ClassNotFoundException ex) {
			// If EJB API not present, simply ignore.
		}
		try {
			asyncAnnotationTypes.add((Class<? extends Annotation>)
					ClassUtils.forName("jakarta.enterprise.concurrent.Asynchronous", classLoader));
		}
		catch (ClassNotFoundException ex) {
			// If Jakarta Concurrent API not present, simply ignore.
		}

		this.advice = buildAdvice(executor, exceptionHandler);
		this.pointcut = buildPointcut(asyncAnnotationTypes);
	}
```

advice构造的时候会创建AnnotationAsyncExecutionInterceptor
```java
	protected Advice buildAdvice(
			@Nullable Supplier<Executor> executor, @Nullable Supplier<AsyncUncaughtExceptionHandler> exceptionHandler) {

		AnnotationAsyncExecutionInterceptor interceptor = new AnnotationAsyncExecutionInterceptor(null);
		interceptor.configure(executor, exceptionHandler);
		return interceptor;
	}
```

AnnotationAsyncExecutionInterceptor会调用父类AnnotationAsyncExecutionInterceptor构造的父类AsyncExecutionAspectSupport的构造
```java
	public AnnotationAsyncExecutionInterceptor(@Nullable Executor defaultExecutor) {
		super(defaultExecutor);
	}
```

然后再给创建出的AnnotationAsyncExecutionInterceptor（爷类AsyncExecutionAspectSupport）配置下exceptionHandler：interceptor.configure(executor, exceptionHandler);

最终返回AnnotationAsyncExecutionInterceptor

下面利用自定义线程池和事件多播器来实现异步注册：


## 下面进入正文v6，异步广播模式
自定义广播器SimpleApplicationEventMulticaster，并设置自定义线程池taskExecutor1

注意坑，多播器的名字必须是：applicationEventMulticaster
```java
    @Bean("applicationEventMulticaster")
    public SimpleApplicationEventMulticaster simpleApplicationEventMulticaster(ThreadPoolTaskExecutor taskExecutor1) {
        SimpleApplicationEventMulticaster eventMulticaster = new SimpleApplicationEventMulticaster();
        eventMulticaster.setTaskExecutor(taskExecutor);
        return eventMulticaster;
    }
```

原因见这段源码，AbstractApplicationContext，如果多播器名字不是applicationEventMulticaster，我们自定义的线程池属性就不会生效
```java
	protected void initApplicationEventMulticaster() {
		ConfigurableListableBeanFactory beanFactory = getBeanFactory();
		if (beanFactory.containsLocalBean(APPLICATION_EVENT_MULTICASTER_BEAN_NAME)) {
			this.applicationEventMulticaster =
					beanFactory.getBean(APPLICATION_EVENT_MULTICASTER_BEAN_NAME, ApplicationEventMulticaster.class);
			if (logger.isTraceEnabled()) {
				logger.trace("Using ApplicationEventMulticaster [" + this.applicationEventMulticaster + "]");
			}
		}
		else {
			this.applicationEventMulticaster = new SimpleApplicationEventMulticaster(beanFactory);
			beanFactory.registerSingleton(APPLICATION_EVENT_MULTICASTER_BEAN_NAME, this.applicationEventMulticaster);
			if (logger.isTraceEnabled()) {
				logger.trace("No '" + APPLICATION_EVENT_MULTICASTER_BEAN_NAME + "' bean, using " +
						"[" + this.applicationEventMulticaster.getClass().getSimpleName() + "]");
			}
		}
	}
```

然后我们看效果，自定义广播器线程池之前,全部由默认同步消费线程：[nio-8080-exec-1]来执行监听器的触发消费

实现了我们带有异步线程池的事件广播器之后，所有的监听器都交给了异步线程池中的线程[ecutor-thread-1]来执行了，而且是多线程，每个监听器消费互不影响
```java
2025-05-17T21:43:50.483+08:00  INFO 19712 --- [cutor1-thread-3] o.l.e.listener.EventListenerService2     : EventListenerService2监听到MyCustomEvent: org.lyflexi.eventv6.event.CustomEvent[source=str]
2025-05-17T21:43:50.483+08:00  INFO 19712 --- [cutor1-thread-1] o.l.e.listener.EventListenerService3     : EventListenerService3监听到MyCustomEvent: org.lyflexi.eventv6.event.CustomEvent[source=str]
2025-05-17T21:43:50.483+08:00  INFO 19712 --- [cutor1-thread-2] o.l.e.listener.EventListenerService1     : EventListenerService1监听到MyCustomEvent: org.lyflexi.eventv6.event.CustomEvent[source=str]
2025-05-17T21:44:00.491+08:00  INFO 19712 --- [cutor1-thread-2] o.l.e.listener.EventListenerService1     : EventListenerService1处理完MyCustomEvent: org.lyflexi.eventv6.event.CustomEvent[source=str]
```

源码在这里，SimpleApplicationEventMulticaster#multicastEvent
```java
	@Override
	public void multicastEvent(ApplicationEvent event) {
		multicastEvent(event, resolveDefaultEventType(event));
	}

	@Override
	public void multicastEvent(final ApplicationEvent event, @Nullable ResolvableType eventType) {
		ResolvableType type = (eventType != null ? eventType : resolveDefaultEventType(event));
		Executor executor = getTaskExecutor();
		for (ApplicationListener<?> listener : getApplicationListeners(event, type)) {
			if (executor != null) {
				executor.execute(() -> invokeListener(listener, event));
			}
			else {
				invokeListener(listener, event);
			}
		}
	}
```

多播器的顶级接口是ApplicationEventMulticaster。解释下为什么Spring要将SpringEventPublisher和ApplicationEventMulticaster分开作为两个接口？

目的就是将ApplicationEventMulticaster留给用户自定义，实现异步通知等高级功能
```shell
ApplicationEventPublisher接口  // 顶级ApplicationEventPublisher接口
        ↓ 
AbstractApplicationContext(实现了接口SpringEventPublisher)   // 发布事件的入口，调用发布方法
        ↓ publishEvent()                     
getApplicationEventMulticaster()  //获取多播器来分发
        ↓ multicastEvent
getApplicationEventMulticaster()  //获取多播器来分发
        ↓                    
public void multicastEvent(final ApplicationEvent event, @Nullable ResolvableType eventType) {
    ResolvableType type = (eventType != null ? eventType : resolveDefaultEventType(event));
    Executor executor = getTaskExecutor();
    for (ApplicationListener<?> listener : getApplicationListeners(event, type)) {
        if (executor != null) {
            executor.execute(() -> invokeListener(listener, event));
        }
        else {
            invokeListener(listener, event);
        }
    }
}
```

