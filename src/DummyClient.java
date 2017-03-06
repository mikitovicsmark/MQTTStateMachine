import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

public class DummyClient implements MqttCallback {
    
	MqttClient myClient;
	MqttConnectOptions connOpts;
	
	public enum State {
		ON, OFF
	}
	
	State innerState;
	
	public DummyClient(String broker, String clientId) {
		innerState = State.OFF;
		 try {
			myClient = new MqttClient(broker, clientId);
			myClient.setCallback(this);
			connOpts = new MqttConnectOptions();
		
		    connOpts.setCleanSession(true);
		    connOpts.setKeepAliveInterval(30);
		    
		    myClient.connect(connOpts);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
	
	public void connectionLost(Throwable arg0) {
		System.out.println("Connection lost.");
		
	}

	public void deliveryComplete(IMqttDeliveryToken arg0) {
		System.out.println("Delivery complete.");
		
	}

	public void messageArrived(String topic, MqttMessage msg) throws Exception {
		System.out.println("Got message: Topic: " + topic + "\n\tMessage: " + new String(msg.getPayload()));
		innerState = State.ON;
	}

	public void subscribe(String topic, int qos) throws MqttException {
		myClient.subscribe(topic, qos);
		
	}

	public void publish(String topic, MqttMessage message) throws MqttPersistenceException, MqttException {
		myClient.publish(topic, message);
		
	}

	public void disconnect() throws MqttException {
		myClient.disconnect();
		
	}
}
