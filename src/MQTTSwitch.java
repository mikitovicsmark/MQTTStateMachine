import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.yakindu.scr.RuntimeService;
import org.yakindu.scr.TimerService;
import org.yakindu.scr.switchsm.SwitchSMStatemachine;
import org.yakindu.scr.switchsm.ISwitchSMStatemachine.SCIPublishListener;

public class MQTTSwitch implements MqttCallback{
	SwitchSMStatemachine statemachine;
	MqttClient myClient;
	MqttConnectOptions connOpts;
	boolean started = false;
	
	public MQTTSwitch(String broker, String clientId, MemoryPersistence persistence) {
		 try {
			statemachine = new SwitchSMStatemachine();
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
			statemachine.setTimer(new TimerService());
			statemachine.getSCIPublish().getListeners().add(new SCIPublishListener() {

				public void onOnTurnOnRaised() {
					String topic = "DummyTopic";
					String content = "Turned On!";
					MqttMessage message = new MqttMessage(content.getBytes());
					try {
						myClient.publish(topic, message);
					} catch (MqttPersistenceException e) {
						e.printStackTrace();
					} catch (MqttException e) {
						e.printStackTrace();
					}
				}

				public void onOnTurnOffRaised() {
					String topic = "DummyTopic";
					String content =  "Turned Off!";
					MqttMessage message = new MqttMessage(content.getBytes());
					try {
						myClient.publish(topic, message);
					} catch (MqttPersistenceException e) {
						e.printStackTrace();
					} catch (MqttException e) {
						e.printStackTrace();
					}				
				}
				
			});
			statemachine.init();
			statemachine.enter();
			RuntimeService.getInstance().registerStatemachine(statemachine, 100);
			started = true;
		}
	}

	public void turnOn() {
		statemachine.getSCIControls().raiseTurnOn();
		statemachine.runCycle();
	}
	
	public void connectionLost(Throwable arg0) {
		System.out.println("Connection lost.");
	}

	public void deliveryComplete(IMqttDeliveryToken arg0) {
		System.out.println("Delivery complete.");
	}

	public void messageArrived(String topic, MqttMessage msg) throws Exception {
		System.out.println("Got message: Topic: " + topic + "\n\tMessage: " + new String(msg.getPayload()));
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
