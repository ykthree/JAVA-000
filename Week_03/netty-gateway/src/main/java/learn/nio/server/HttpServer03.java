package learn.nio.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * HttpServer3(localhost:8803)，使用{@link ServerSocket}，异步处理请求，每接受一个请求，从线程池里获取一个线程去处理。
 */
public class HttpServer03 {

    private static final Logger log = LoggerFactory.getLogger(HttpServer03.class);

    private final static AtomicInteger COUNT = new AtomicInteger();

    public static void main(String[] args) throws IOException {
        ExecutorService executorService = Executors.newFixedThreadPool(40);
        final ServerSocket serverSocket = new ServerSocket(8803);
        while (true) {
            try {
                final Socket socket = serverSocket.accept();
                executorService.execute(() -> service(socket));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void service(Socket socket) {
        try {
            Thread.sleep(20);
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            printWriter.println("HTTP/1.1 200 OK");
            printWriter.println("Content-Type:text/html;charset=utf-8");
            String body = "hello,nio";
            printWriter.println("Content-Length:" + body.getBytes().length);
            printWriter.println();
            printWriter.write(body);
            printWriter.close();
            socket.close();

            log.info("HttpServer03 service:[{}]", COUNT.getAndIncrement());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}