package comleyou.gateway.filter;

import com.leyou.auth.utils.JwtUtils;
import com.leyou.common.pojo.utils.CookieUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import comleyou.gateway.config.JwtProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class LoginFilter extends ZuulFilter {
    @Autowired
    private JwtProperties jwtProperties;
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 10;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        //初始化zuul网关的上下文对象
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        //获取cookie中的token信息
        String token = CookieUtils.getCookieValue(request, this.jwtProperties.getCookieName());
        try {
            JwtUtils.getInfoFromToken(token,this.jwtProperties.getPublicKey());
        } catch (Exception e) {
            e.printStackTrace();
            //设置是否转发请求
            context.setSendZuulResponse(false);
            //设置响应状态码，401
            context.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
        }
        //啥都不做，继续访问
        return null;
    }
}
