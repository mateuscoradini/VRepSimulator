package br.com.pos.unicamp.vrep;

import java.util.Vector;

import br.com.pos.unicamp.vrep.sensor.handle.RequestSensorUltrassonic;
import br.com.pos.unicamp.vrep.utils.ImageDetector;
import coppelia.BoolW;
import coppelia.FloatWA;
import coppelia.IntW;
import coppelia.remoteApi;

public class PioneerRobot {

	// Connection params
	private static final remoteApi VREP_API = new remoteApi();
	private static final String IP = "127.0.0.1";
	private static final int PORT = 25000;

	// Calibration sensors and motor
	private static final float MAX_DETECTION_DIST = 0.5f;
	private static final float MAX_DETECTION_DIST_FRONT = 0.5f;
	private static final float MIN_DETECTION_DIST = 0.45f;
	private static final float INITIAL_VELOCITY = 0.4f;

	// Sensors number -1 in Vector (Sensor 1..16)
	private static final int SENSOR_4_NUMBER = 3;
	private static final int SENSOR_5_NUMBER = 4;
	private static final int SENSOR_3_NUMBER = 2;
	private static final int SENSOR_6_NUMBER = 5;
	private static final int SENSOR_7_NUMBER = 6;
	private static final int SENSOR_8_NUMBER = 7;
	private static final int SENSOR_9_NUMBER = 8;

	private Vector<RequestSensorUltrassonic> proximitySensors = new Vector<RequestSensorUltrassonic>();
	private float[] detect = new float[16];

	// Motor handle API IntW
	private IntW leftMotor;
	private IntW rightMotor;

	// ClientID (Connection ID)
	private int clientID;

	private ImageDetector imageDetector;

	// First Step Wall Following algorithm
	private boolean encontrouObstaculo = false;

	int stepNone = 0;

	public PioneerRobot() {
		initializeRobot();
		initializeCamera();
		initializeSensors();
		initializeActuators();
	}

	private void initializeRobot() {
		closeAllOpenedConnections();
		clientID = openConnection();
	}

	/**
	 * Initialize motor handles
	 */
	private void initializeActuators() {
		leftMotor = new IntW(-1);
		rightMotor = new IntW(-1);

		initMotor("Pioneer_p3dx_leftMotor", leftMotor);
		initMotor("Pioneer_p3dx_rightMotor", rightMotor);
	}

	/**
	 * Get motor handle in VREP API
	 * 
	 * @param motorId
	 * @param motor
	 */
	private void initMotor(final String motorId, final IntW motor) {
		int response = VREP_API.simxGetObjectHandle(clientID, motorId, motor, VREP_API.simx_opmode_oneshot_wait);
		if (response == VREP_API.simx_return_ok) {
			System.out.println("Successfully connected to the motor: " + motorId);
		}
	}

	/**
	 * Get ultrassonic sensors in handle in VREP API
	 */
	private void initializeSensors() {
		fillDetectSensors();

		// Ultrasonic sensors
		for (int i = 0; i < 16; i++) {
			String sensorName = "Pioneer_p3dx_ultrasonicSensor" + (i + 1);
			IntW handleSensor = new IntW(0);
			int respost = VREP_API.simxGetObjectHandle(clientID, sensorName, handleSensor,
					VREP_API.simx_opmode_oneshot_wait);
			if (respost == VREP_API.simx_return_ok) {
				System.out.println("Successfully connected to the sensor: " + sensorName);
				RequestSensorUltrassonic ultrassonic = new RequestSensorUltrassonic();
				ultrassonic.setHandle(handleSensor);
				ultrassonic.setSensorName(sensorName);
				proximitySensors.add(ultrassonic);
			}
		}
	}

	private void turnCameraOn() {
		imageDetector.turnOn();
	}

	private void turnCameraOff() {
		imageDetector.turnOff();
	}

	private boolean isHealthyPlantDetected() {
		return imageDetector.isHealthyPlantDetected();
	}

	private boolean isWeakPlantDetected() {
		return imageDetector.isWeakPlantDetected();
	}

	private void initializeCamera() {
		this.imageDetector = new ImageDetector(clientID, VREP_API);
		startThread(imageDetector);
	}

	private void startThread(final ImageDetector imageDetector) {
		final Thread imageDetectorThread = new Thread(imageDetector);
		imageDetectorThread.start();
	}
	
	boolean state = true;

	public void actuation() throws InterruptedException {

		while (VREP_API.simxGetConnectionId(clientID) != -1) {

			float leftMotorVelocity = INITIAL_VELOCITY;
			float rightMotorVelocity = INITIAL_VELOCITY;

			if (isHealthyPlantDetected()) {
				System.out.println("Healthy Plant Detected.");
			}

			if (isWeakPlantDetected()) {
				System.out.println("Weak Plant Detected.");
			}

			// Left
			RequestSensorUltrassonic sensor3 = requestSensorUltrassonicDetectetion(SENSOR_3_NUMBER);

			// Front
			RequestSensorUltrassonic sensor4 = requestSensorUltrassonicDetectetion(SENSOR_4_NUMBER);
			RequestSensorUltrassonic sensor5 = requestSensorUltrassonicDetectetion(SENSOR_5_NUMBER);

			RequestSensorUltrassonic sensor6 = requestSensorUltrassonicDetectetion(SENSOR_6_NUMBER);

			// Right
			RequestSensorUltrassonic sensor7 = requestSensorUltrassonicDetectetion(SENSOR_7_NUMBER);
			RequestSensorUltrassonic sensor8 = requestSensorUltrassonicDetectetion(SENSOR_8_NUMBER);
			RequestSensorUltrassonic sensor9 = requestSensorUltrassonicDetectetion(SENSOR_9_NUMBER);

			// Tem não tem algo na frente
			if (hasNotAheadObstacle(sensor4, sensor5, sensor6)) {
				
				System.out.println("Frontal options");

				if (hasSomethingDetectedInRight(sensor7) && hasSomethingDetectedInRight(sensor8)) {
					System.out.println("Step 1");

					System.out.println("There is an obstacle ahead: " + sensor7.getSensorName() + " Detecting: "
							+ sensor7.getState().getValue());
					System.out.println("There is an obstacle ahead: " + sensor8.getSensorName() + " Detecting: "
							+ sensor8.getState().getValue());
					if (distanceMaxSensor(sensor8) && distanceMaxSensor(sensor9)) {
						System.out.println("Step 4");
						setTargetVelocity(leftMotor, 0.4f);
						setTargetVelocity(rightMotor, 0.2f);
						Thread.sleep(500);

					} else {
						setTargetVelocity(leftMotor, leftMotorVelocity);
						setTargetVelocity(rightMotor, rightMotorVelocity);
					}

				}

				if (followDistanceMinRight(sensor7, sensor8)) {
					System.out.println("Step 2");
					setTargetVelocity(leftMotor, 0.5f);
                    setTargetVelocity(rightMotor, 0f);
                    Thread.sleep(1500);
                    setTargetVelocity(leftMotor, 0.5f);
                    setTargetVelocity(rightMotor, 0.5f);
					Thread.sleep(1500);
					System.out.println("There is an obstacle ahead: " + sensor7.getSensorName() + " Detecting: "
							+ sensor7.getState().getValue());
					System.out.println("There is an obstacle ahead: " + sensor8.getSensorName() + " Detecting: "
							+ sensor8.getState().getValue());

				}

				if (followDistanceMaxRight(sensor7, sensor8) && encontrouObstaculo) {
					System.out.println("Step 3");
					setTargetVelocity(leftMotor, 0.5f);
                    setTargetVelocity(rightMotor, -0.1f);
                    Thread.sleep(1500);
                    setTargetVelocity(leftMotor, 0.5f);
                    setTargetVelocity(rightMotor, 0.5f);
                    Thread.sleep(1000);
					Thread.sleep(1000);
					System.out.println("There is an obstacle ahead: " + sensor7.getSensorName() + " Detecting: "
							+ sensor7.getState().getValue());
					System.out.println("There is an obstacle ahead: " + sensor8.getSensorName() + " Detecting: "
							+ sensor8.getState().getValue());

				}

				System.out.println("Step None.");
				if(state){
					
					setTargetVelocity(leftMotor, 0.5f);
					setTargetVelocity(rightMotor, 0.5f);
					Thread.sleep(1000);
					state = false;
					
				}
				
				

			} else {

				// TODO Construir inteligencia para fazer rollback da sala que
				// entrou

				if (tooCloseRight45AngleSensor(sensor7)) {
					System.out.println("Step 45°.");

					// NOTA: É mais facil o robo aprender a contornar o objeto
					// do que controlar a precisão dos sensores.
					turnLeft90Angle();
					encontrouObstaculo = true;
					state = false;
				}
				
				
								
					
			}

		}

		closeOpenedConnections();
		System.out.println("Connection closed.");
	}

	private boolean distMaxRightWarning(RequestSensorUltrassonic sensor7, RequestSensorUltrassonic sensor8) {
		boolean detected7 = sensor7.getState().getValue();
		boolean detected8 = sensor8.getState().getValue();

		float dist8 = sensor8.getCoord().getArray()[2];
		float dist7 = sensor7.getCoord().getArray()[2];

		// Caso queira uma distancia menor de seguir parede, temos que calibrar
		// o tempo de resposta do motores pra ser mais devagar para fazer as
		// curvas sem penalizacao
		float minimaDistanciaTeste = MIN_DETECTION_DIST - 0.15f;

		if ((detected7 && dist7 <= minimaDistanciaTeste) && (detected8 && dist8 <= minimaDistanciaTeste)) {
			return true;
		}
		return false;
	}

	private boolean distanceMaxSensor(RequestSensorUltrassonic sensor) {

		boolean detected = sensor.getState().getValue();

		float dist = sensor.getCoord().getArray()[2];

		// Caso queira uma distancia menor de seguir parede, temos que calibrar
		// o tempo de resposta do motores pra ser mais devagar para fazer as
		// curvas sem penalizacao
		float minimaDistanciaTeste = MIN_DETECTION_DIST - 0.15f;

		if (detected && dist >= minimaDistanciaTeste) {
			return true;
		}
		return false;

	}

	private boolean followDistanceMaxRight(RequestSensorUltrassonic sensor7, RequestSensorUltrassonic sensor8) {

		boolean detected7 = sensor7.getState().getValue();
		boolean detected8 = sensor8.getState().getValue();

		float dist8 = sensor8.getCoord().getArray()[2];
		float dist7 = sensor7.getCoord().getArray()[2];

		// Caso queira uma distancia menor de seguir parede, temos que calibrar
		// o tempo de resposta do motores pra ser mais devagar para fazer as
		// curvas sem penalizacao
		float minimaDistanciaTeste = MIN_DETECTION_DIST - 0.15f;

		if ((detected7 && dist7 >= minimaDistanciaTeste) && (detected8 && dist8 >= minimaDistanciaTeste)) {
			return true;
		}
		return false;

	}

	private boolean followDistanceMinRight(RequestSensorUltrassonic sensor7, RequestSensorUltrassonic sensor8) {
		float dist7 = sensor7.getCoord().getArray()[2];
		float dist8 = sensor8.getCoord().getArray()[2];
		boolean detected7 = sensor7.getState().getValue();

		// float minimaDistanciaTeste = MIN_DETECTION_DIST - 0.1f;

		if ((!detected7 && dist7 <= MIN_DETECTION_DIST)) {
			return true;
		}
		return false;

	}

	private boolean tooCloseRight45AngleSensor(RequestSensorUltrassonic sensor) {
		float dist = sensor.getCoord().getArray()[2];
		boolean detected = sensor.getState().getValue();
		if (detected && (dist <= (MIN_DETECTION_DIST - 0.15f))) {
			return true;
		}
		return false;

	}

	private boolean hasSomethingDetectedInRight(RequestSensorUltrassonic sensor) {
		float dist = sensor.getCoord().getArray()[2];
		boolean detected = sensor.getState().getValue();
		if (detected && (dist >= MAX_DETECTION_DIST_FRONT)) {
			return true;
		}
		return false;

	}

	private boolean hasNotAheadObstacle(RequestSensorUltrassonic sensor4, RequestSensorUltrassonic sensor5,
			RequestSensorUltrassonic sensor6) {
		return !hasAheadObstacle(sensor4) && !hasAheadObstacle(sensor5) && !hasAheadObstacle(sensor6);
	}

	private boolean hasAheadObstacle(RequestSensorUltrassonic sensor) {
		float dist = sensor.getCoord().getArray()[2];
		boolean detected = sensor.getState().getValue();
		if (detected && dist < MAX_DETECTION_DIST) {
			System.out.println("Front Detect: " + sensor.getSensorName());
			return true;
		}
		System.out.println("Front sensors detected anything: " + sensor.getSensorName());
		return false;
	}

	private void turnLeft90Angle() {
		setTargetVelocity(leftMotor, -0.1f);
		setTargetVelocity(rightMotor, 0.3f);
	}

	private void setTargetVelocity(IntW motorHandle, float velocity) {
		VREP_API.simxSetJointTargetVelocity(clientID, motorHandle.getValue(), velocity, VREP_API.simx_opmode_streaming);
	}
	

	private RequestSensorUltrassonic requestSensorUltrassonicDetectetion(int sensorNumber) {
		BoolW state = new BoolW(true);
		FloatWA coord = new FloatWA(0);
		IntW detectedObjectHandle = new IntW(0);
		FloatWA detectedSurfaceNormalVector = new FloatWA(0);
		int respost = VREP_API.simxReadProximitySensor(clientID,
				proximitySensors.get(sensorNumber).getHandle().getValue(), state, coord, detectedObjectHandle,
				detectedSurfaceNormalVector,

				VREP_API.simx_opmode_oneshot_wait);

		RequestSensorUltrassonic ultrassonic = new RequestSensorUltrassonic();
		ultrassonic.setState(state);
		ultrassonic.setCoord(coord);
		ultrassonic.setDetectedSurfaceNormalVector(detectedSurfaceNormalVector);
		ultrassonic.setDetectedObjectHandle(detectedObjectHandle);
		ultrassonic.setSensorNumber(sensorNumber + 1);
		ultrassonic.setSensorName(proximitySensors.get(sensorNumber).getSensorName());

		return ultrassonic;
	}

	private void fillDetectSensors() {
		for (int i = 0; i < 16; i++) {
			float handleSensor = 1f;
			detect[i] = handleSensor;
		}
	}

	private int openConnection() {
		return VREP_API.simxStart(IP, PORT, true, true, 2000, 5);
	}

	private void closeAllOpenedConnections() {
		closeConnection(-1);
	}

	private void closeOpenedConnections() {
		closeConnection(clientID);
	}

	private void closeConnection(final int clientID) {
		VREP_API.simxFinish(clientID);
	}

}
