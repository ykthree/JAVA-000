package learn.rpc.custom.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewConstructor;
import javassist.Modifier;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.Proxy;
import javassist.util.proxy.ProxyFactory;
import learn.rpc.custom.api.RpcfxRequest;
import learn.rpc.custom.api.RpcfxResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static sun.awt.image.PixelConverter.Argb.instance;

/**
 * 客户端代理存根，基于 Javassist 动态代理实现
 */
public final class JavassistRpcfxStub extends AbstractRpcfxStub {

    static {
        ParserConfig.getGlobalInstance().addAccept("learn.rpc");
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T create(final Class<T> serviceClass, final String url) throws Exception {
        return (T) new ProxyGenerator().generateProxy(AbstractRpcfxStub.class.getClassLoader(), serviceClass,
                new AccessSeverInterceptor(serviceClass, url));
    }


    class ProxyGenerator {

        /**
         * 用于生成代理类的名称
         */
        private final AtomicInteger counter = new AtomicInteger(1);

        /**
         * 创建代理类，代理类结构如下：
         *
         * <pre>
         *     class learn.proxy.IDemo$Proxy1 implements learn.Proxy.IDemo {
         *
         *         public static java.lang.reflect.Method[] methods;
         *
         *         private java.lang.reflect.InvocationHandler handler;
         *
         *         public A$Proxy1(private java.lang.reflect.InvocationHandler handler) {
         *             this.handler = handler;
         *         }
         *
         *         public A$Proxy1() {}
         *
         *         public T1 demo1(int arg0) throws Exception {
         *             Object[] args = new Object[1];
         *             args[0] = ($w)$1;
         *             Object ret = handler.invoke(this, methods[0], args);
         *             return (T1) ret;
         *         }
         *
         *        public T2 demo2(int arg0) throws Exception {
         *            Object[] args = new Object[1];
         *            args[0] = ($w)$1;
         *            Object ret = handler.invoke(this, methods[0], args);
         *            return (T2) ret;
         *        }
         *
         *     }
         *
         * </pre>
         */
        Object generateProxy(final ClassLoader classLoader, final Class<?> targetClass,
                             final InvocationHandler invocationHandler) throws Exception {
            ClassPool classPool = ClassPool.getDefault();
            String qualifiedName = generateClassQualifiedName(targetClass);
            CtClass proxy = classPool.makeClass(qualifiedName);
            proxy.addInterface(classPool.get(targetClass.getName()));

            CtField methodsField = CtField.make("public static java.lang.reflect.Method[] methods;", proxy);
            CtField handlerField = CtField.make("private " + InvocationHandler.class.getName() + " handler;", proxy);
            proxy.addField(methodsField);
            proxy.addField(handlerField);

            CtConstructor constructorWithHandlerField = new CtConstructor(new CtClass[]{classPool.get(InvocationHandler.class.getName())}, proxy);
            constructorWithHandlerField.setBody("this.handler=$1;");
            constructorWithHandlerField.setModifiers(Modifier.PUBLIC);
            CtConstructor defaultConstructor = CtNewConstructor.defaultConstructor(proxy);
            proxy.addConstructor(constructorWithHandlerField);
            proxy.addConstructor(defaultConstructor);

            List<Method> methods = new ArrayList<>();
            Method[] targetDeclaredMethods = targetClass.getDeclaredMethods();
            for (Method targetDeclaredMethod : targetDeclaredMethods) {
                int index = methods.size();
                // 构建代理方法
                Class<?> returnType = targetDeclaredMethod.getReturnType();
                Class<?>[] parameterTypes = targetDeclaredMethod.getParameterTypes();
                StringBuilder targetMethodInvocation = new StringBuilder("Object[] args = new Object[").append(parameterTypes.length).append("];");
                for (int i = 0; i < parameterTypes.length; i++) {
                    targetMethodInvocation.append(" args[").append(i).append("] = ($w)$").append(i + 1).append(";");
                }
                targetMethodInvocation.append(" Object ret = handler.invoke(this, methods[").append(index).append("], args);");
                if (!Void.TYPE.equals(returnType)) {
                    targetMethodInvocation.append(" return ").append(castReturnType(returnType, "ret")).append(";");
                }
                log.debug("InvokeTargetMethod: {}", targetMethodInvocation);

                StringBuilder proxyTargetMethod = new StringBuilder(1024);
                proxyTargetMethod.append(modifier(targetDeclaredMethod.getModifiers())).append(' ').append(getParameterType(returnType)).append(' ').append(targetDeclaredMethod.getName());
                proxyTargetMethod.append('(');
                for (int i = 0; i < parameterTypes.length; i++) {
                    if (i > 0)
                        proxyTargetMethod.append(',');
                    proxyTargetMethod.append(getParameterType(parameterTypes[i]));
                    proxyTargetMethod.append(" arg").append(i);
                }
                proxyTargetMethod.append(')');

                Class<?>[] ets = targetDeclaredMethod.getExceptionTypes();
                if (ets.length > 0) {
                    proxyTargetMethod.append(" throws ");
                    for (int i = 0; i < ets.length; i++) {
                        if (i > 0)
                            proxyTargetMethod.append(',');
                        proxyTargetMethod.append(getParameterType(ets[i]));
                    }
                }
                proxyTargetMethod.append('{').append(targetMethodInvocation.toString()).append('}');
                log.debug("TargetMethod: {}", proxyTargetMethod);
                CtMethod ctMethod = CtMethod.make(proxyTargetMethod.toString(), proxy);
                proxy.addMethod(ctMethod);
                methods.add(targetDeclaredMethod);
            }

            proxy.setModifiers(Modifier.PUBLIC);
            Class<?> proxyClass = proxy.toClass(classLoader, null);
            proxyClass.getField("methods").set(null, methods.toArray(new Method[0]));
            return proxyClass.getConstructor(InvocationHandler.class).newInstance(invocationHandler);
        }

        /**
         * 生成代理类的全限定名
         */
        private String generateClassQualifiedName(final Class<?> serviceClass) {
            return String.format("%s$Proxy%d", serviceClass.getName(), counter.getAndIncrement());
        }

        private String modifier(int mod) {
            if (Modifier.isPublic(mod)) {
                return "public";
            }
            if (Modifier.isProtected(mod)) {
                return "protected";
            }
            if (Modifier.isPrivate(mod)) {
                return "private";
            }
            return "";
        }

        private String getParameterType(Class<?> c) {
            if (c.isArray()) {   //数组类型
                StringBuilder sb = new StringBuilder();
                do {
                    sb.append("[]");
                    c = c.getComponentType();
                } while (c.isArray());

                return c.getName() + sb.toString();
            }
            return c.getName();
        }

        private String castReturnType(Class<?> cl, String name) {
            if( cl.isPrimitive() ) {
                if( Boolean.TYPE == cl )
                    return name + "==null?false:((Boolean)" + name + ").booleanValue()";
                if( Byte.TYPE == cl )
                    return name + "==null?(byte)0:((Byte)" + name + ").byteValue()";
                if( Character.TYPE == cl )
                    return name + "==null?(char)0:((Character)" + name + ").charValue()";
                if( Double.TYPE == cl )
                    return name + "==null?(double)0:((Double)" + name + ").doubleValue()";
                if( Float.TYPE == cl )
                    return name + "==null?(float)0:((Float)" + name + ").floatValue()";
                if( Integer.TYPE == cl )
                    return name + "==null?(int)0:((Integer)" + name + ").intValue()";
                if( Long.TYPE == cl )
                    return name + "==null?(long)0:((Long)" + name + ").longValue()";
                if( Short.TYPE == cl )
                    return name + "==null?(short)0:((Short)" + name + ").shortValue()";
                throw new RuntimeException(name+" is unknown primitive type.");
            }
            return "(" + getParameterType(cl) + ")"+name;
        }

    }

    class AccessSeverInterceptor implements InvocationHandler {

        private final Class<?> serviceClass;

        private final String url;

        public <T> AccessSeverInterceptor(Class<T> serviceClass, String url) {
            this.serviceClass = serviceClass;
            this.url = url;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] params) throws Throwable {
            RpcfxRequest request = new RpcfxRequest();
            request.setServiceClass(this.serviceClass.getName());
            request.setMethod(method.getName());
            request.setParams(params);
            RpcfxResponse response = doPost(request, this.url);
            if (!response.isStatus()) {
                throw response.getException();
            }
            return JSON.parse(response.getResult().toString());
        }
    }

}
