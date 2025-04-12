package com.lyflexi.springbootpure.resolver;

import com.lyflexi.springbootpure.annotation.TimeStampRequestBody;
import org.springframework.context.ApplicationContext;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.service.invoker.RequestBodyArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.util.HashMap;
import java.util.Map;

/**
 * 装饰器模式，实现@RequestBody注解的增强
 */
public class TimeStampRequestBodyResolver implements HandlerMethodArgumentResolver {

    /**
     * 第二个问题，如何拿到RequestResponseBodyMethodProcessor，只有拿到她，才能对其做增强
     */
    private RequestResponseBodyMethodProcessor requestResponseBodyMethodProcessor;
    /**
     * 通过applicationContext延迟加载RequestMappingHandlerAdapter
     */
    private ApplicationContext applicationContext;

    public TimeStampRequestBodyResolver(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;

    }

    /**
     * 支持的参数注解
     * @param parameter
     * @return
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(TimeStampRequestBody.class);
    }

    /**
     *
     * @param parameter
     * @param mavContainer
     * @param webRequest 请求对象
     * @param binderFactory
     * @return
     * @throws Exception
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        //延迟加载RequestMappingHandlerAdapter
        setUpRequestResponseBodyMethodProcessor();

        Object rs = requestResponseBodyMethodProcessor.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
        if (!(rs instanceof Map<?,?>)) {
            return rs;
        }
        //装饰器增强
        ((Map) rs).put("timestamp", System.currentTimeMillis());

        return rs;
    }

    /**
     * 通过ApplicationContext延迟加载RequestMappingHandlerAdapter，延迟到请求到来的时候，具体参数解析的时候动态创建。  可以解决下述循环依赖
     */
    private void setUpRequestResponseBodyMethodProcessor() {
        RequestMappingHandlerAdapter requestMappingHandlerAdapter = applicationContext.getBean(RequestMappingHandlerAdapter.class);
        for (HandlerMethodArgumentResolver resolver : requestMappingHandlerAdapter.getArgumentResolvers()) {
            if (resolver instanceof RequestResponseBodyMethodProcessor) {
                this.requestResponseBodyMethodProcessor = (RequestResponseBodyMethodProcessor) resolver;
                break;
            }
        }
    }


}
