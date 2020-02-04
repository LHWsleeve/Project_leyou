package cn.itcast.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;

@Component

public class LoginFilter extends ZuulFilter {
    @Override
    public String filterType() {
        //指定过滤器的类型：pre,routpost,error
        return "pre";
    }

    @Override
    public int filterOrder() {
        //执行顺序
        return 10;
    }

    @Override
    public boolean shouldFilter() {
        //是否执行run方法。true执行
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        //执行拦截的业务逻辑，null：啥都不干
       //zuul网关的上下文对象.不要导错包
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        String token = request.getParameter("token");
        if (StringUtils.isEmpty(token)){
            context.setSendZuulResponse(false);//不转发请求
            //设置响应状态码，告诉浏览器
            context.setResponseStatusCode(HttpStatus.SC_UNAUTHORIZED);
            context.setResponseBody("相应错误");
        }
        return null;
    }
}
