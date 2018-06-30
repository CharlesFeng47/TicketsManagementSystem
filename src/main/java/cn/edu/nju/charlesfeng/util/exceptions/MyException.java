package cn.edu.nju.charlesfeng.util.exceptions;

import cn.edu.nju.charlesfeng.util.enums.ExceptionCode;

/**
 * @author Shenmiu
 * @date 2018/06/30
 *
 * 异常处理的根异常
 */
public class MyException extends Exception {

    /**
     * 错误代码
     */
    private ExceptionCode exceptionCode;

    public MyException(ExceptionCode exceptionCode) {
        this.exceptionCode = exceptionCode;
    }

    public MyException(ExceptionCode exceptionCode, String message) {
        super(message);
        this.exceptionCode = exceptionCode;
    }

    public ExceptionCode getExceptionCode() {
        return exceptionCode;
    }

    public void setExceptionCode(ExceptionCode exceptionCode) {
        this.exceptionCode = exceptionCode;
    }
}
