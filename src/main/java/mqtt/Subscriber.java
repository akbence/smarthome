package mqtt;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Callable;

public class Subscriber implements Callable<Void> {

    public static String DEFAULT_TOPIC = "test";
    public static String DEFAULT_SERVER_URI = "tcp://broker.hivemq.com";

    private String server;
    private String topic;

    private IMqttClient client;
    private Random rnd = new Random();

    public static class SubscriberBuilder {

        private String builderTopic;
        private String builderServer;

        public SubscriberBuilder withTopic(String topic) {
            builderTopic = topic;
            return this;
        }

        public SubscriberBuilder withServer(String server) {
            builderServer = server;
            return this;
        }

        public Subscriber build() throws MqttException {
            if (builderTopic == null) {
                builderTopic = DEFAULT_TOPIC;
            }
            if (builderServer == null) {
                builderServer = DEFAULT_SERVER_URI;
            }
            return new Subscriber(this);
        }

    }

    private Subscriber(SubscriberBuilder builder) throws MqttException {
        topic = builder.builderTopic;
        server = builder.builderServer;
        String publisherId = UUID.randomUUID().toString();
        client = new MqttClient(server, publisherId);
        this.client = client;
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setConnectionTimeout(10);
        client.connect(options);

    }

    public void subscribe() throws MqttException {
        client.subscribe(topic, (top, msg) -> {
            String payload = new String(msg.getPayload());
            System.out.println("[I82] Message received: topic={}, payload={}" + payload);
        });
    }

    public void unsubscribe() throws MqttException {
        client.unsubscribe(topic);
    }

    public Void call() throws Exception {

        if (!client.isConnected()) {
            return null;
        }

        MqttMessage msg = new MqttMessage(String.format("pélód").getBytes());
        msg.setQos(0);
        msg.setRetained(true);

        return null;
    }

    /**
     * This method simulates reading the engine temperature
     * 
     * @return
     */
    private MqttMessage readEngineTemp() {
        double temp = 80 + rnd.nextDouble() * 20.0;
        byte[] payload = String.format("T:%04.2f", temp).getBytes();
        MqttMessage msg = new MqttMessage(payload);
        return msg;
    }
}