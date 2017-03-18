import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.yakindu.scr.RuntimeService;
import org.yakindu.scr.light.LightStatemachine;

public class MQTTLight extends LightStatemachine implements MqttCallback{
	MqttClient myClient;
	MqttConnectOptions connOpts;
	LightStatemachine statemachine;
	boolean started = false;
	
	State innerState;
	
	public MQTTLight(String broker, String clientId, MemoryPersistence persistence) {
		 try {
			statemachine = new LightStatemachine();
			myClient = new MqttClient(broker, clientId, persistence);
			myClient.setCallback(this);
			connOpts = new MqttConnectOptions();
		
		    connOpts.setCleanSession(true);
		    connOpts.setKeepAliveInterval(30);
		    
		    myClient.connect(connOpts);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	public void init() {
		if (!started) {
			statemachine.init();
			statemachine.enter();
			RuntimeService.getInstance().registerStatemachine(statemachine, 100);
			started = true;
		}
	}
	
	public void connectionLost(Throwable arg0) {
		System.out.println(arg0.toString());
		System.out.println("Connection lost.");
		
	}

	public void deliveryComplete(IMqttDeliveryToken arg0) {
		System.out.println("Delivery complete.");
	}

	public void turnOn() {
		statemachine.getSCIControls().raiseTurnOn();
		statemachine.runCycle();
		//System.out.println(statemachine.isStateActive(LightStatemachine.State.main_region_On));
	}

	public void turnOff() {
		statemachine.getSCIControls().raiseTurnOff();
		statemachine.runCycle();
		//System.out.println(statemachine.isStateActive(LightStatemachine.State.main_region_Off));
	}
	
	public void messageArrived(String topic, MqttMessage msg) throws Exception {
		System.out.println("Light Got message: Topic: " + topic + "\n\tMessage: " + new String(msg.getPayload()));
		switch (msg.toString()) {
			case "Turned On!":
				turnOn();
				break;
			case "Turned Off!":
				turnOff();
				break;
			default:
				System.out.println("Unhandled message received: " + msg.toString());
				break;
		}
		if(msg.toString() == "Turned On!") {
			turnOn();
		}

		if(msg.toString() == "Turned On!") {
			turnOn();
		}	
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
