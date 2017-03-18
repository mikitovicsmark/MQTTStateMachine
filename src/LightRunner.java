import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class LightRunner {
	
    public static void main(String[] args) throws InterruptedException {

        String topic        = "DummyTopic";
        int qos             = 2;
        String broker       = "tcp://localhost:1883";
        String clientId     = "DummyClient";
        MemoryPersistence persistence = new MemoryPersistence();

        try {

        	MQTTLight sampleClient2 = new MQTTLight(broker, clientId + '2', persistence);
        	sampleClient2.init();
            sampleClient2.subscribe(topic, qos);
            
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
