package com.lyflexi.springbootpure.config;

import com.lyflexi.springbootpure.resolver.TimeStampRequestBodyResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.util.List;

/**
 * 解决第一个问题，将我们自己的参数解析器TimeStampRequestBodyResolver注册到Spring MVC中
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    /*
    *
    * 解决第二个问题：实现@RequestBody注解的增强，所以要把RequestResponseBodyMethodProcessor传给我们的TimeStampRequestBodyResolver
    *
    * 怎么拿到RequestResponseBodyMethodProcessor
    * ---
    * public class RequestMappingHandlerAdapter extends AbstractHandlerMethodAdapter
	*	implements BeanFactoryAware, InitializingBean
    *
    * 即使RequestMappingHandlerAdapter没有@Component注解，但实现了BeanFactoryAware, InitializingBean，说明后期肯定要被spring管理。
    *
    * 答案是WebMvcAutoConfiguration自动装配了RequestMappingHandlerAdapter
    *
    * 因此可以直接@Autowired注入RequestMappingHandlerAdapter
    * */

    @Autowired
    private RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        //spring给customArgumentResolver默认提供了一堆， 其中就包括RequestResponseBodyMethodProcessor
        for (HandlerMethodArgumentResolver customArgumentResolver : requestMappingHandlerAdapter.getArgumentResolvers()) {
            if (customArgumentResolver instanceof RequestResponseBodyMethodProcessor) {
                // 传给TimeStampRequestBodyResolver
                resolvers.add(new TimeStampRequestBodyResolver((RequestResponseBodyMethodProcessor)customArgumentResolver));
                break;
            }
        }
    }

    /*
    * 这样思路是正确的，但是很不幸发生了循环依赖：
    * ┌─────┐
|  webConfig (field private org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter com.lyflexi.springbootpure.config.WebConfig.requestMappingHandlerAdapter)
↑     ↓
|  org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration$EnableWebMvcConfiguration
└─────┘
    *
    * 原因是RequestMappingHandlerAdapter的自动装配依赖WebMvcAutoConfiguration$EnableWebMvcConfiguration
    *
    * 而EnableWebMvcConfiguration又依赖你自己写的WebConfig，具体原因是EnableWebMvcConfiguration实现了DelegatingWebMvcConfiguration（它组合了WebMvcConfigurerComposite），在容器初始化的时候要把所有的WebMvcConfigurer实现类装进WebMvcConfigurerComposit
    * 	@Autowired(required = false)
	*   public void setConfigurers(List<WebMvcConfigurer> configurers) {
	*	if (!CollectionUtils.isEmpty(configurers)) {
	*		this.configurers.addWebMvcConfigurers(configurers);
	*	}
	*}
    *
    * */
}
