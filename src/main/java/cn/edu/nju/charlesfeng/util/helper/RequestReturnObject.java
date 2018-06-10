package cn.edu.nju.charlesfeng.util.helper;

import cn.edu.nju.charlesfeng.util.enums.RequestReturnObjectState;
import com.alibaba.fastjson.JSON;

/**
 * 在 RESTful 风格下，后端系统为服务提供返回对象封装
 */
public class RequestReturnObject {

    /**
     * 此次请求的状态
     */
    private RequestReturnObjectState state;

    /**
     * 状态则为此次请求返回的结果对象的JSON字符串
     */
    private String object;

    public RequestReturnObject(RequestReturnObjectState state, Object object) {
        this.state = state;
        this.object = JSON.toJSONString(object);
        System.out.println("已转化为：" + this.object);
    }

    /**
     * 因为各种原因，后端应用服务器无法正确返回
     *
     * @param state 错误的原因
     */
    public RequestReturnObject(RequestReturnObjectState state) {
        this.state = state;
    }

    public RequestReturnObjectState getState() {
        return state;
    }

    public void setState(RequestReturnObjectState state) {
        this.state = state;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }
}