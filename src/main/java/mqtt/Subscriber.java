package mqtt;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Callable;


public class Subscriber  implements Callable<Void> {

    public static final String TOPIC = "engine/temperature";

    private IMqttClient client;
    private Random rnd = new Random();

    public Subscriber() throws MqttException {
        String publisherId = UUID.randomUUID().toString();
        IMqttClient client = new MqttClient("tcp://iot.eclipse.org:1883",publisherId);
        this.client = client;
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setConnectionTimeout(10);
        client.connect(options);
    }


    public Void call() throws Exception {

        if ( !client.isConnected()) {
            return null;
        }

        MqttMessage msg = readEngineTemp();
        msg.setQos(0);
        msg.setRetained(true);
       // client.publish(TOPIC,msg);

        return null;
    }

    /**
     * This method simulates reading the engine temperature
     * @return
     */
    private MqttMessage readEngineTemp() {
        double temp =  80 + rnd.nextDouble() * 20.0;
        byte[] payload = String.format("T:%04.2f",temp).getBytes();
        MqttMessage msg = new MqttMessage(payload);
        return msg;
    }
}