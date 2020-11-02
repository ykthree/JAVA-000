package learn.nio.gateway;

import learn.nio.gateway.inbound.HttpInboundServer;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class NettyServerApplication {

    public final static String GATEWAY_NAME = "NIOGateway";

    public final static String GATEWAY_VERSION = "1.0.0";

    /**
     * 网关端口
     */
    private final static String GATEWAY_PORT = "8888";

    /**
     * 代理服务
     */
    public final static String PROXY_SERVER1 = "http://localhost:8801";

    public final static String PROXY_SERVER2 = "http://localhost:8802";

    public final static String PROXY_SERVER3 = "http://localhost:8803";

    /**
     * http://localhost:8888/  ==> gateway API
     * http://localhost:8801  ==> backend service
     */
    public static void main(String[] args) {
        String gatewayPort = System.getProperty("gatewayPort", GATEWAY_PORT);
        String proxyServer1 = System.getProperty("proxyServer", PROXY_SERVER1);
        String proxyServer2 = System.getProperty("proxyServer", PROXY_SERVER2);
        String proxyServer3 = System.getProperty("proxyServer", PROXY_SERVER3);
        String[] servers = {proxyServer1, proxyServer2, proxyServer3};

        int port = Integer.parseInt(gatewayPort);
        System.out.println(GATEWAY_NAME + " " + GATEWAY_VERSION + " starting...");
        HttpInboundServer server = new HttpInboundServer(port, servers);
        System.out.println(GATEWAY_NAME + " " + GATEWAY_VERSION + " started at http://localhost:" + port + " for servers:" + Arrays.toString(servers));
        try {
            server.run();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
