package cn.edu.nju.charlesfeng.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;

/**
 * @author Shenmiu
 * @date 2018/06/04
 * <p>
 * fastjson配置文件
 */
@Configuration
public class WebMvcConfig {


    /**
     * 引入Fastjson解析json，不使用默认的jackson
     * 必须在pom.xml引入fastjson的jar包，并且版必须大于1.2.10
     */
    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters() {
        // 1. 定义一个convert转换消息的对象
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        // 2. 添加fastjson的配置信息
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        // 3. serialFeatures
        SerializerFeature[] serializerFeatures = new SerializerFeature[]{
                SerializerFeature.WriteNonStringKeyAsString,
                SerializerFeature.QuoteFieldNames,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.PrettyFormat
        };
        fastJsonConfig.setSerializerFeatures(serializerFeatures);
        // 4. 在convert中添加配置信息
        fastConverter.setFastJsonConfig(fastJsonConfig);
        // 5. 将convert添加到converters中
        HttpMessageConverter<?> converter = fastConverter;
        return new HttpMessageConverters(converter);
    }
}
