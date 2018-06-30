package cn.edu.nju.charlesfeng.config;

import cn.edu.nju.charlesfeng.interceptor.CorsInterceptor;
import cn.edu.nju.charlesfeng.interceptor.ProgramIDInterceptor;
import cn.edu.nju.charlesfeng.interceptor.UserInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import static org.springframework.web.cors.CorsConfiguration.ALL;

@Configuration
public class WebInterceptorConfig extends WebMvcConfigurationSupport {

    @Override
    protected void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(ALL)
                .allowedMethods(ALL)
                .allowedHeaders(ALL)
                .allowCredentials(true);
        super.addCorsMappings(registry);
    }

    /**
     * 拦截器配置
     *
     * @param registry InterceptorRegistry
     */
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        //Cors
        registry.addInterceptor(new CorsInterceptor()).addPathPatterns("/**");
        //UserInterceptor
        registry.addInterceptor(new UserInterceptor())
                //member
                .addPathPatterns("/member/logout")
                .addPathPatterns("/member/token")
                .addPathPatterns("/member/star")
                .addPathPatterns("/member/cancelStar")
                .addPathPatterns("/member/getStarPrograms")
                .addPathPatterns("/member/modifyPortrait")
                .addPathPatterns("/member/modifyPassword")
                .addPathPatterns("/member/modifyName")
                //order
                .addPathPatterns("/order/getOneOrder")
                .addPathPatterns("/order/getMyOrdersByState")
                .addPathPatterns("/order/generateOrder")
                .addPathPatterns("/order/cancelOrder")
                .addPathPatterns("/order/unsubscribeOrder")
                .addPathPatterns("/order/payOrder")
                //program
                .addPathPatterns("/program/getProgramDetailByToken");
        //ProgramIDInterceptor
        registry.addInterceptor(new ProgramIDInterceptor())
                .addPathPatterns("/program/getProgramDetail")
                .addPathPatterns("/program/getProgramDetailByToken")
                .addPathPatterns("/member/star")
                .addPathPatterns("/member/cancelStar")
                .addPathPatterns("/order/generateOrder");
        super.addInterceptors(registry);
    }

}
