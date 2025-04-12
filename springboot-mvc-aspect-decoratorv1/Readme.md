装饰器模式（组合之后进行增强），实现@RequestBody注解的增强

WebMvcConfigurer是什么时候注入容器的：答案是DelegatingWebMvcConfiguration构造参数注入
```java
  @Configuration(proxyBeanMethods = false)
  public class DelegatingWebMvcConfiguration extends WebMvcConfigurationSupport {

    private final WebMvcConfigurerComposite configurers = new WebMvcConfigurerComposite();


    @Autowired(required = false)
    public void setConfigurers(List<WebMvcConfigurer> configurers) {
        if (!CollectionUtils.isEmpty(configurers)) {
            this.configurers.addWebMvcConfigurers(configurers);
        }
    }
    //
}


class WebMvcConfigurerComposite implements WebMvcConfigurer {

    private final List<WebMvcConfigurer> delegates = new ArrayList<>();


    public void addWebMvcConfigurers(List<WebMvcConfigurer> configurers) {
        if (!CollectionUtils.isEmpty(configurers)) {
            this.delegates.addAll(configurers);
        }
    }


    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        for (WebMvcConfigurer delegate : this.delegates) {
            delegate.configurePathMatch(configurer);
        }
    }
```
DelegatingWebMvcConfiguration->WebMvcConfigurerComposite->WebMvcConfigurer，这套流程是委托模式+组合模式（装饰器增强）+进一步委托模式的炉火纯青的设计体现：

- 委托模式Delegating：DelegatingWebMvcConfiguration 是一个“调度员”，它不亲自上场不亲自执行WebMvcConfigurer内部方法，而是把任务交给“代表团”——WebMvcConfigurerComposite
- 组合模式用户装饰器增强：这个代表团组合模式肚子里含有List<WebMvcConfigurer> delegates，因此是把任务分发给真正的干活人——delegates 列表里的各个 WebMvcConfigurer。
- 进一步委托模式 ：一个对象（WebMvcConfigurerComposite）实现接口 WebMvcConfigurer，并且肚子里含有List<WebMvcConfigurer> delegates，代表团把接口的每个方法简单重写为“遍历调用它所包含的多个 WebMvcConfigurer”

犹如一副美丽的外包传导链

上面仅仅是注册了WebMvcConfigurer，接下来要实现自定义请求参数解析器，还需要解决几个问题：

- 解决第一个问题，将我们自己的参数解析器TimeStampRequestBodyResolver注册到Spring MVC中
- 解决第二个问题，实现@RequestBody注解的增强，所以要把RequestResponseBodyMethodProcessor传给我们的TimeStampRequestBodyResolver

因此通过组合，具体有两种表现形式：
- 委托
- 装饰器