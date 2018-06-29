package cn.edu.nju.charlesfeng.interceptor;

import cn.edu.nju.charlesfeng.util.enums.RequestReturnObjectState;
import cn.edu.nju.charlesfeng.util.helper.RequestReturnObject;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用于拦截需要传输program_id的接口
 */
@Component
public class ProgramIDInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String id = request.getParameter("program_id");
        String ids[] = id.split("-");
        if (!id.contains("-") || ids.length != 2) { //不符合系统指定id格式
            JSONObject jsonObject = new JSONObject();
            response.setStatus(200);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            jsonObject.put("programIdInterceptorInfo", new RequestReturnObject(RequestReturnObjectState.WRONG_PROGRAM_ID));
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
