package main;

import mqtt.Subscriber;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws MqttException, InterruptedException {
        CountDownLatch receivedSignal = new CountDownLatch(10);
        Subscriber subscriber = new Subscriber();
        subscriber.subscribe(Subscriber.TOPIC, (topic, msg) -> {
            byte[] payload = msg.getPayload();
            // ... payload handling omitted
            receivedSignal.countDown();
        });
        receivedSignal.await(1, TimeUnit.MINUTES);

    }
}
