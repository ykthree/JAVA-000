package learn.spring.data.datasource.routing;

import org.springframework.core.NamedThreadLocal;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * 线程本地数据源标识变量设置器，参考了 dynamic datasource 的实现。
 *
 * @see <a href  = "https://github.com/baomidou/dynamic-datasource-spring-boot-starter">dynamic datasource</a>
 * @apiNote 主要用来设置和清除本地线程数据源标识变量（注意要确保 {@link this#remove()} 方法被调用，防止内存泄漏）。本地变量使用了栈存放数据
 * 源标识，为了避免嵌套调用带有动态数据源注解的方法标识被中途清除问题。
 */
public final class DynamicDSContextHolder {

    private static final ThreadLocal<Deque<String>> LOOKUP_KEY_HOLDER = new NamedThreadLocal<Deque<String>>("dynamic-datasource") {

        @Override
        protected Deque<String> initialValue() {
            return new ArrayDeque<>();
        }
    };

    private DynamicDSContextHolder() {
    }

    public static String peek() {
        return LOOKUP_KEY_HOLDER.get().peek();
    }

    public static void push(String ds) {
        Deque<String> flags = LOOKUP_KEY_HOLDER.get();
        if (flags == null) {
            LOOKUP_KEY_HOLDER.set(new ArrayDeque<>());
        } else {
            flags.push(ds);
        }
    }

    public static void poll() {
        Deque<String> deque = LOOKUP_KEY_HOLDER.get();
        deque.poll();
        if (deque.isEmpty()) {
            LOOKUP_KEY_HOLDER.remove();
        }
    }

    public static void remove() {
        LOOKUP_KEY_HOLDER.remove();
    }

    public static boolean empty() {
        return LOOKUP_KEY_HOLDER.get().isEmpty();
    }

}