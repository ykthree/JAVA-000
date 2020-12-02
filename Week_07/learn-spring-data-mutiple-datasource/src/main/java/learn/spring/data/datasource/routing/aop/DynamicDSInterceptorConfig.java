package learn.spring.data.datasource.routing.aop;

import learn.spring.data.datasource.routing.annotation.DynamicDS;
import org.springframework.aop.Advisor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 动态数据源拦截器配置类，配置切点和拦截器
 */
//@Configuration
public class DynamicDSInterceptorConfig {

    @Bean
    public DynamicDSAnnotationInterceptor dynamicDSAnnotationInterceptor() {
        return new DynamicDSAnnotationInterceptor();
    }

    @Bean
    public Advisor dynamicDSAnnotationAdvisor(DynamicDSAnnotationInterceptor dynamicDSAnnotationInterceptor) {
        // 定义注解切点
        AnnotationMatchingPointcut pointcut = new AnnotationMatchingPointcut(null, DynamicDS.class);
        DefaultPointcutAdvisor dynamicDSAnnotationAdvisor = new DefaultPointcutAdvisor();
        dynamicDSAnnotationAdvisor.setPointcut(pointcut);
        dynamicDSAnnotationAdvisor.setAdvice(dynamicDSAnnotationInterceptor);
        return dynamicDSAnnotationAdvisor;
    }
}
