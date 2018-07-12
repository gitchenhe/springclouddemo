package com.chenhe.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author chenhe
 * @Date 2018-07-11 17:20
 * @desc zuul 过滤器
 **/
@Component
public class DefaultFilter extends ZuulFilter{
    private Logger logger = LoggerFactory.getLogger(DefaultFilter.class);

    enum FilterType{
        pre,//路由之前
        routing,//路由之时候
        post,//路由之后
        error,//发生错误调用
    }

    /**
     * 过滤类型(小写)
     * @return
     */
    @Override
    public String filterType() {

        return "pre";
    }

    /**
     * 过滤的顺序
     * @return
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 是否需要过滤
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 过滤的具体逻辑
     * @return
     */
    @Override
    public Object run() {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        logger.info("request : {} >>> {}",request.getMethod(),request.getRequestURL());

        Object accessToken = request.getParameter("token");
        if (accessToken == null){
            logger.warn("token is empty");
            context.setSendZuulResponse(false);
            context.setResponseStatusCode(401);

            try{
                context.getResponse().getWriter().write("token is empty");
            }catch (Exception e){}
            return null;
        }
        logger.info("ok");
        return null;
    }
}
