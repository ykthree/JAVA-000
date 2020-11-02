package learn.nio.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * HttpServer1(localhost:8801)，使用{@link ServerSocket}。
 */
public class HttpServer01 {

    private static final Logger log = LoggerFactory.getLogger(HttpServer01.class);

    private final static AtomicInteger COUNT = new AtomicInteger();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8801);
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                service(socket);
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

            log.info("HttpServer01 service:[{}]", COUNT.getAndIncrement());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}