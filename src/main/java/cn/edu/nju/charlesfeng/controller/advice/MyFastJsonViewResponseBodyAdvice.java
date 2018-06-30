package cn.edu.nju.charlesfeng.controller.advice;

import com.alibaba.fastjson.support.spring.FastJsonViewResponseBodyAdvice;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Shenmiu
 * @date 2018/06/30
 */
@RestControllerAdvice
public class MyFastJsonViewResponseBodyAdvice extends FastJsonViewResponseBodyAdvice {
}
