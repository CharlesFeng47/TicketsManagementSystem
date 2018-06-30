package cn.edu.nju.charlesfeng.util.serializer;

import cn.edu.nju.charlesfeng.util.enums.OrderState;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * @author Shenmiu
 * @date 2018/06/30
 */
public class OrderStateSerializer implements ObjectSerializer {
    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        serializer.write(((OrderState) object).getVal());
    }
}
