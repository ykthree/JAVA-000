package learn.java.jvm;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 作业2：自定义一个 Classloader，加载一个Hello.xlass文件，执行 hello 方法，此文件内容是一个Hello.class文件所有字节（x=255-x）
 * 处理后的文件。<br>
 *
 * Hello.xlass文件类加载器
 *
 * @author ykthree
 * @date 2020/10/21 13:36
 */
public class HelloClassLoader extends ClassLoader {

    public static void main(String[] args) throws IOException {
        try {
            String className = "Hello";
            String methodName = "hello";
            Class<?> helloClass = new HelloClassLoader().findClass(className);
            Object hello = helloClass.newInstance();
            Method helloMethod = helloClass.getMethod(methodName);
            helloMethod.invoke(hello);
        } catch (InstantiationException | ClassNotFoundException | IllegalAccessException | NoSuchMethodException |
                InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] encryption = readHelloClassFile("Hello.xlass");
        if (encryption != null) {
            byte[] decryption = new byte[encryption.length];
            for (int i = 0; i < encryption.length; i++) {
                decryption[i] = (byte) (255 - encryption[i]);
            }
            return defineClass(name, decryption, 0, decryption.length);
        }
        return super.findClass(name);
    }

    /**
     * 读取和{@link HelloClassLoader}同一目录下的Hello.xlass文件
     *
     * @param file 文件名（带后缀）
     * @return {@code 字节数组} 文件内容
     */
    private byte[] readHelloClassFile(String file) {
        try (InputStream inputStream = getClass().getResourceAsStream(file)) {
            if (inputStream != null) {
                byte[] data = new byte[inputStream.available()];
                int read = inputStream.read(data);
                return data;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
