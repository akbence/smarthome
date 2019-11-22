package main;

import mqtt.Subscriber;
import org.eclipse.paho.client.mqttv3.MqttException;

public class Main {
    public static void main(String[] args) throws MqttException {
        Subscriber subscriber = new Subscriber.SubscriberBuilder().build();
        System.out.println("end");
    }
}
