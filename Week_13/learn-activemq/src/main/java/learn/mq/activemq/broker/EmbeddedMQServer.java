package learn.mq.activemq.broker;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;

/**
 * 创建内嵌 MQ server
 *
 * @author ykthree
 * 2021/1/13 17:50
 * @see <a href="http://activemq.apache.org/how-do-i-embed-a-broker-inside-a-connection.html">How do I embed a Broker inside a Connection.</a>
 */
public class EmbeddedMQServer {

    /**
     * 显示创建
     *
     * @param bindAddress url
     * @return BrokerService
     * @throws Exception Exception
     */
    public static BrokerService createServerExplicitly(String bindAddress) throws Exception {
        BrokerService broker = new BrokerService();
        broker.addConnector(bindAddress);
        broker.setBrokerName("test");
        return broker;
    }

    /**
     * 使用 BrokerFactory 创建
     *
     * @param brokerUrl brokerUrl
     * @return BrokerService
     * @throws Exception Exception
     */
    public static BrokerService createServerByBrokerFactory(String brokerUrl) throws Exception {
        BrokerService broker = BrokerFactory.createBroker("broker:" + brokerUrl);
        return broker;
    }

    /**
     * 使用 ActiveMQConnectionFactory 创建
     *
     * @return ActiveMQConnectionFactory
     */
    public static ActiveMQConnectionFactory createServerByActiveMQConnectionFactory() {
        ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory("vm://localhost?broker.persistent=false");
        return cf;
    }

}
