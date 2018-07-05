package cn.edu.nju.charlesfeng.config;

import cn.edu.nju.charlesfeng.interceptor.CorsInterceptor;
import cn.edu.nju.charlesfeng.interceptor.ProgramIDInterceptor;
import cn.edu.nju.charlesfeng.interceptor.UserInterceptor;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

import static org.springframework.web.cors.CorsConfiguration.ALL;

/**
 * @author Dong
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

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
                .addPathPatterns("/program/getSowingMapUrl")
                .addPathPatterns("/member/star")
                .addPathPatterns("/member/cancelStar")
                .addPathPatterns("/order/generateOrder");
        super.addInterceptors(registry);
    }

    @Override
    protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 1. 定义一个convert转换消息的对象
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        // 2. 添加fastjson的配置信息
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        // 3. serialFeatures
        SerializerFeature[] serializerFeatures = new SerializerFeature[]{
                SerializerFeature.WriteNonStringKeyAsString,
                SerializerFeature.QuoteFieldNames,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.PrettyFormat,
                SerializerFeature.WriteEnumUsingToString
        };
        fastJsonConfig.setSerializerFeatures(serializerFeatures);
        // 4. 在convert中添加配置信息
        fastConverter.setFastJsonConfig(fastJsonConfig);
        // 5. 将convert添加到converters中
        converters.add(fastConverter);
    }
}
