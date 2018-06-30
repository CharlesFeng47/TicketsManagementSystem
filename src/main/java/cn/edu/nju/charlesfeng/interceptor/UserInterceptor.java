package cn.edu.nju.charlesfeng.interceptor;

import cn.edu.nju.charlesfeng.util.enums.ExceptionCode;
import cn.edu.nju.charlesfeng.util.helper.RequestReturnObject;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用于拦截需要传输token的接口
 */
@Component
public class UserInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getParameter("token");
        JSONObject jsonObject = new JSONObject();
        if (token == null) { //请求没有传送token
            response.setStatus(200);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            jsonObject.put("userInterceptorInfo", new RequestReturnObject(ExceptionCode.TOKEN_IS_NULL));
            response.getWriter().write(jsonObject.toJSONString());
            return false;
        }

        Object user = request.getSession().getAttribute(token);
        if (user == null) { //指定token没有登录
            response.setStatus(200);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            jsonObject.put("userInterceptorInfo", new RequestReturnObject(ExceptionCode.USER_NOT_EXIST));
            response.getWriter().write(jsonObject.toJSONString());
            return false;
        }
        return true;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
