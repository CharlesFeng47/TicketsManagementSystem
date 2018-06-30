package cn.edu.nju.charlesfeng.util.exceptions;

/**
 * @author Shenmiu
 * @date 2018/06/30
 * <p>
 * 异常信息
 */
public class ErrorInfo {

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 异常信息
     */
    private String message;

    public ErrorInfo(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
