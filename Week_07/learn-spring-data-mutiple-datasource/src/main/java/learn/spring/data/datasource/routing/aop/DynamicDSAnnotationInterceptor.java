package learn.spring.data.datasource.routing.aop;

import learn.spring.data.datasource.routing.DynamicDSContextHolder;
import learn.spring.data.datasource.routing.annotation.DynamicDS;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * 动态数据源注解拦截器
 */
@Slf4j
public class DynamicDSAnnotationInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            String dsFlag = invocation.getMethod().getAnnotation(DynamicDS.class).value();
            DynamicDSContextHolder.push(dsFlag);
            return invocation.proceed();
        } finally {
            if (DynamicDSContextHolder.empty()) {
                DynamicDSContextHolder.remove();
            } else {
                DynamicDSContextHolder.poll();
            }
        }
    }

}