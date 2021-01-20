package learn.mq.manual.core;

import lombok.Data;

import java.util.HashMap;

/**
 * 消息
 *
 * @param <T> 消息体类型
 */
@Data
public class Message<T> {

    private HashMap<String,Object> headers;

    private T body;

    @Override
    public String toString() {
        return "" + body;
    }
}