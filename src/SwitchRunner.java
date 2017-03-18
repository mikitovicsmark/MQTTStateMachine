import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class SwitchRunner {
	
    public static void main(String[] args) throws InterruptedException {

        //String topic        = "DummyTopic";
        String content      = "Turn on!";
        int qos             = 2;
        String broker       = "tcp://localhost:1883";
        String clientId     = "DummyClient";
        MemoryPersistence persistence = new MemoryPersistence();


		MQTTSwitch sampleClient1 = new MQTTSwitch(broker, clientId + '1', persistence);
        sampleClient1.init();
        	
        MqttMessage message = new MqttMessage(content.getBytes());
        message.setQos(qos);
        message.setRetained(false);
 
        sampleClient1.turnOn();
            
    }
}
