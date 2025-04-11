package com.lyflexi.springbootpure.resolver;

import com.lyflexi.springbootpure.annotation.ReqParamAnno;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.HashMap;
import java.util.Map;

/**
 * 注解处理器，专属于MVC的切面实现
 */
public class ReqParamResolver implements HandlerMethodArgumentResolver {
    Map<String,String> user2Param = new HashMap<>();
    public ReqParamResolver(){
        user2Param.put("1","param1");
        user2Param.put("2","param2");
    }
    /**
     * 支持的参数注解
     * @param parameter
     * @return
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(ReqParamAnno.class);
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
        String userId = webRequest.getHeader("USER-ID");
        String reqParam = getByUserId(userId);
        return reqParam;
    }

    /**
     * 业务查询，根据请求头，可能查数据库
     * @param userId
     * @return
     */
    private String getByUserId(String userId) {
        return user2Param.get(userId);
    }
}
