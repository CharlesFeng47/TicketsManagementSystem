package cn.edu.nju.charlesfeng.spring.filter;

import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebFilter("/*")
public class CorsFilter implements Filter {

    private static final Logger logger = Logger.getLogger(CorsFilter.class);

    /**
     * 允许远程访问的来源源列表
     */
    private List<String> allowOrigin;

    /**
     * 允许远程访问的方法类型，支持的HTTP请求方法列表
     */
    private String allowMethods;

    /**
     * 确定浏览器是否应该包含与请求相关的任何cookie
     */
    private boolean allowCredentials;

    /**
     * 实际请求期间可以使用的请求标头列表
     */
    private String allowHeaders;

    /**
     * 浏览器允许客户端访问的响应头列表
     */
    private String exposeHeaders;

    @Override
    public void init(FilterConfig filterConfig) {
        logger.debug("Init Cors filter");

        String allowOriginString = "http://localhost:8080";
        allowOrigin = Arrays.asList(allowOriginString.split(","));
        allowMethods = "GET,POST,PUT,DELETE,OPTIONS";

        allowCredentials = true;
        allowHeaders = "Content-Type,X-Token";
        exposeHeaders = "";
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String curOrigin = request.getHeader("Origin");
        logger.debug("currentOrigin : " + curOrigin);

        // 跨域访问则增加相关的报文头信息
        if (allowOrigin != null && allowOrigin.contains(curOrigin)) {
            response.setHeader("Access-Control-Allow-Origin", curOrigin);
            response.setHeader("Access-Control-Allow-Credentials", String.valueOf(allowCredentials));

            if (allowMethods != null && !allowMethods.isEmpty())
                response.setHeader("Access-Control-Allow-Methods", allowMethods);
            if (allowHeaders != null && !allowHeaders.isEmpty())
                response.setHeader("Access-Control-Allow-Headers", allowHeaders);
            if (exposeHeaders != null && !exposeHeaders.isEmpty())
                response.setHeader("Access-Control-Expose-Headers", exposeHeaders);
        }

        logger.debug(((HttpServletResponse) res).getHeaderNames());
        filterChain.doFilter(req, res);
    }

    @Override
    public void destroy() {

    }
}
