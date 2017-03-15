import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Main {
	
    public static void main(String[] args) throws InterruptedException {

        String topic        = "DummyTopic";
        String content      = "Turn on!";
        int qos             = 2;
        String broker       = "tcp://localhost:1883";
        String clientId     = "DummyClient";
        //MemoryPersistence persistence = new MemoryPersistence();

        try {

        	MQTTSwitch sampleClient1 = new MQTTSwitch(broker, clientId + '1');
        	sampleClient1.init();
        	DummyClient sampleClient2 = new DummyClient(broker, clientId + '2');
        	
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            message.setRetained(false);
 
            sampleClient2.subscribe(topic, qos);
            sampleClient1.turnOn();
            
        } catch(MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        }
    }
}