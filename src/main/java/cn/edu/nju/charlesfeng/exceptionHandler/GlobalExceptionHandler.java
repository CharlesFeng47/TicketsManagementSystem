package cn.edu.nju.charlesfeng.controller.exceptionHandler;

import cn.edu.nju.charlesfeng.util.enums.ExceptionCode;
import cn.edu.nju.charlesfeng.util.exceptions.ErrorInfo;
import cn.edu.nju.charlesfeng.util.exceptions.MyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Shenmiu
 * @date 2018/06/30
 *
 * 全局异常处理
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = MyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorInfo errorHandler(MyException e) throws Exception {
        ExceptionCode exceptionCode = e.getExceptionCode();
        return new ErrorInfo(exceptionCode.getRepre(), exceptionCode.getMessage());
    }


}
