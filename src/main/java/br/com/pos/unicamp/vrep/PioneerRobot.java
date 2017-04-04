package br.com.pos.unicamp.vrep;

import java.util.Vector;

import br.com.pos.unicamp.vrep.detectors.RequestSensorUltrassonic;
import coppelia.BoolW;
import coppelia.FloatW;
import coppelia.FloatWA;
import coppelia.IntW;
import coppelia.remoteApi;
import org.sonatype.plexus.components.sec.dispatcher.model.SettingsSecurity;

public class PioneerRobot {

    private static final remoteApi VREP_API = new remoteApi();
    private static final String IP = "127.0.0.1";
    private static final int PORT = 25000;

    private static final float NO_DETECT_DIST = 0.4f;
    private static final float MAX_DETECTION_DIST = 0.3f;
    private static final float MAX_DETECTION_DIST_VERTICAL = 0.4f;
    private static final float INITIAL_VELOCITY = 0.5f;

    private static final int SENSOR_4_NUMBER = 3;
    private static final int SENSOR_5_NUMBER = 4;

    private static final int SENSOR_3_NUMBER = 2;
    private static final int SENSOR_6_NUMBER = 5;
    private static final int SENSOR_7_NUMBER = 6;
    private static final int SENSOR_8_NUMBER = 7;
    private Vector<FloatW> braitenbergL = new Vector<FloatW>();

    // private float[] detect = new Vector<Float>();
    private Vector<FloatW> braitenbergR = new Vector<FloatW>();
    private Vector<RequestSensorUltrassonic> proximitySensors = new Vector<RequestSensorUltrassonic>();
    private float[] detect = new float[16];
    private IntW leftMotor;
    private IntW rightMotor;
    private int clientID;

    public PioneerRobot() {
        initializeRobot();
        //        initializeCamera();
        initializeSensors();
        initializeActuators();
    }

    private void initializeRobot() {
        closeAllOpenedConnections();

        clientID = openConnection();
    }

    private void initializeActuators() {
        leftMotor = new IntW(-1);
        rightMotor = new IntW(-1);

        initMotor("Pioneer_p3dx_leftMotor", leftMotor);

        initMotor("Pioneer_p3dx_rightMotor", rightMotor);
    }

    private void initMotor(final String motorId, final IntW motor) {
        int response = VREP_API.simxGetObjectHandle(clientID, motorId, motor, VREP_API.simx_opmode_oneshot_wait);
        if (response == VREP_API.simx_return_ok) {
            System.out.println("Successfully connected to the motor: " + motorId);
        }
    }

    private void initializeSensors() {
        fillDetectSensors();

        braitenbergL = fillBraitenbergL();
        braitenbergR = fillBraitenbergR();

        // Ultrasonic sensors
        for (int i = 0; i < 16; i++) {
            String sensorName = "Pioneer_p3dx_ultrasonicSensor" + (i + 1);
            IntW handleSensor = new IntW(0);
            int respost = VREP_API.simxGetObjectHandle(clientID, sensorName, handleSensor, VREP_API.simx_opmode_oneshot_wait);
            if (respost == VREP_API.simx_return_ok) {
                System.out.println("Successfully connected to the sensor: " + sensorName);
                RequestSensorUltrassonic ultrassonic = new RequestSensorUltrassonic();
                ultrassonic.setHandle(handleSensor);
                ultrassonic.setSensorName(sensorName);
                proximitySensors.add(ultrassonic);
            }
        }
    }

    private boolean isPlantDetected() {
        return false;
    }

    //    private void initializeCamera() {
    //        while(true) {
    //            //
    //        }
    //    }

    boolean reset = true;

    public void actuation() throws InterruptedException {
        while (VREP_API.simxGetConnectionId(clientID) != -1) {

            float leftMotorVelocity = INITIAL_VELOCITY;
            float rightMotorVelocity = INITIAL_VELOCITY;

            RequestSensorUltrassonic sensor4 = requestSensorUltrassonicDetectetion(SENSOR_4_NUMBER);
            RequestSensorUltrassonic sensor5 = requestSensorUltrassonicDetectetion(SENSOR_5_NUMBER);
            RequestSensorUltrassonic sensor6 = requestSensorUltrassonicDetectetion(SENSOR_6_NUMBER);
            RequestSensorUltrassonic sensor7 = requestSensorUltrassonicDetectetion(SENSOR_7_NUMBER);
            RequestSensorUltrassonic sensor8 = requestSensorUltrassonicDetectetion(SENSOR_8_NUMBER);

            if ( (!checkDetectAndProximityFrontSensor(sensor4) || !checkDetectAndProximityFrontSensor(sensor5)) && checkDetectAndProximityFrontSensor(sensor6)  ) {
                setTargetVelocity(leftMotor, INITIAL_VELOCITY);
                setTargetVelocity(rightMotor, INITIAL_VELOCITY);
            } else if ( (!checkDetectAndProximityFrontSensor(sensor6) || !checkDetectAndProximityFrontSensor(sensor7)) && !checkDetectAndProximityFrontSensor(sensor8)) {

                float velocityLeft = INITIAL_VELOCITY - 0.4f;
                float velocityRigt = INITIAL_VELOCITY - 0.1f;
                setTargetVelocity(leftMotor, velocityLeft);
                setTargetVelocity(rightMotor, velocityRigt);
            } else if (checkDetectAndProximityFrontSensor(sensor6) || checkDetectAndProximityFrontSensor(sensor8)) {
                
                float velocityLeft = INITIAL_VELOCITY - 0.6f;
                float velocityRigt = INITIAL_VELOCITY - 0.2f;
                setTargetVelocity(leftMotor, velocityLeft);
                setTargetVelocity(rightMotor, velocityRigt);
                
                //TODO Mapear os sonares 8 como linha seguidora a partir desse ponto que encontrou parede
                
//                if(!checkDetectAndProximityFrontSensor(sensor7) || checkDetectSensor(sensor8)){
//                    
//                    
//                    velocityLeft = INITIAL_VELOCITY;
//                    velocityRigt = INITIAL_VELOCITY;
//                    setTargetVelocity(leftMotor, velocityLeft);
//                    setTargetVelocity(rightMotor, velocityRigt);
//                    
//                    
//                    
//                }
                
                

                
            }

            //            RequestSensorUltrassonic sensor8 = requestSensorUltrassonicDetectetion(7);
            //
            //            //Encontrar a primeiro obstaculo (parede)
            //            if (reset && !checkDetectAndProximitySensor(sensor8)) {
            //                virar90praDireita(reset);
            //                Thread.sleep(3600);
            //                parar();               
            //            }

            //
            //            RequestSensorUltrassonic sensor4 = requestSensorUltrassonicDetectetion(3);
            //            //Ler a distancia do sensor 4 e 5
            //            if (checkDetectAndProximityFrontSensor(sensor4)) {
            //                wallFollowing();
            //                Thread.sleep(1000);
            //            } else {
            //
            //                setTargetVelocity(leftMotor, 0.0f);
            //                setTargetVelocity(rightMotor, 0.0f);
            //                reset = true;
            //                virar90praEsquerda(reset);
            //                Thread.sleep(3600);
            //                parar();
            //                reset = false;
            //                
            //
            //            }

        }

        closeOpenedConnections();
        System.out.println("Connection closed.");
    }

    //    private boolean checkDetectAndProximitySensor(RequestSensorUltrassonic sensor) {
    //        boolean detected = sensor.getState().getValue();
    //        float dist = sensor.getCoord().getArray()[2];
    //
    //        if (detected && dist < NO_DETECT_DIST) {
    //            return true;
    //
    //        }
    //        return false;
    //    }
    
    private boolean checkDetectSensor(RequestSensorUltrassonic sensor) {
        boolean detected = sensor.getState().getValue();
        //float dist = sensor.getCoord().getArray()[2];

        if (detected) {
            return true;
        }
        return false;
    }

    private void wallFollowing() {
        RequestSensorUltrassonic sensor4 = requestSensorUltrassonicDetectetion(3);
        if (checkDetectAndProximityFrontSensor(sensor4)) {
            setTargetVelocity(leftMotor, 0.2f);
            setTargetVelocity(rightMotor, 0.2f);
        }

        RequestSensorUltrassonic sensor5 = requestSensorUltrassonicDetectetion(4);
        if (checkDetectAndProximityFrontSensor(sensor5)) {
            setTargetVelocity(leftMotor, 0.4f);
            setTargetVelocity(rightMotor, 0.4f);

        }

    }

    private void virar90praDireita(boolean reset) {
        if (reset) {
            setTargetVelocity(leftMotor, 1f);
            setTargetVelocity(rightMotor, -1f);
        }
    }

    private void virar90praEsquerda(boolean reset) {

        if (reset) {
            setTargetVelocity(leftMotor, -1f);
            setTargetVelocity(rightMotor, 1f);
        }

    }

    private void parar() {
        setTargetVelocity(leftMotor, 0f);
        setTargetVelocity(rightMotor, 0f);
        reset = false;
    }

    private boolean checkDetectAndProximitySensor(RequestSensorUltrassonic sensor) {
        boolean detected = sensor.getState().getValue();
        float dist = sensor.getCoord().getArray()[2];

        if (detected && dist < NO_DETECT_DIST) {
            return true;
        }
        return false;
    }

    private boolean checkDetectAndProximityFrontSensor(RequestSensorUltrassonic sensor) {
        boolean detected = sensor.getState().getValue();
        float dist = sensor.getCoord().getArray()[2];

        if (detected && dist > NO_DETECT_DIST) {
            return true;
        }
        return false;
    }

    

    private void setTargetVelocity(IntW motorHandle, float velocity) {
        VREP_API.simxSetJointTargetVelocity(clientID, motorHandle.getValue(), velocity, VREP_API.simx_opmode_streaming);

    }

    private RequestSensorUltrassonic requestSensorUltrassonicDetectetion(int sensorNumber) {
        BoolW state = new BoolW(true);
        FloatWA coord = new FloatWA(0);
        IntW detectedObjectHandle = new IntW(0);
        FloatWA detectedSurfaceNormalVector = new FloatWA(0);
        int respost = VREP_API.simxReadProximitySensor(clientID, proximitySensors.get(sensorNumber).getHandle().getValue(), state, coord, detectedObjectHandle, detectedSurfaceNormalVector,

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

    private Vector<FloatW> fillBraitenbergL() {
        Vector<FloatW> vector = new Vector<FloatW>();
        final FloatW w1 = new FloatW(-0.2f);
        final FloatW w2 = new FloatW(-0.4f);
        final FloatW w3 = new FloatW(-0.6f);
        final FloatW w4 = new FloatW(-0.8f);
        final FloatW w5 = new FloatW(-1f);
        final FloatW w6 = new FloatW(-1.2f);
        final FloatW w7 = new FloatW(-1.4f);
        final FloatW w8 = new FloatW(-1.6f);
        final FloatW w0 = new FloatW(-0f);

        vector.add(w1);
        vector.add(w2);
        vector.add(w3);
        vector.add(w4);
        vector.add(w5);
        vector.add(w6);
        vector.add(w7);
        vector.add(w8);

        for (int i = 0; i < 8; i++) {
            vector.addElement(w0);
        }

        return vector;
    }

    private Vector<FloatW> fillBraitenbergR() {
        final Vector<FloatW> vector = new Vector<FloatW>();

        final FloatW w0 = new FloatW(-0f);
        final FloatW w1 = new FloatW(-0.2f);
        final FloatW w2 = new FloatW(-0.4f);
        final FloatW w3 = new FloatW(-0.6f);
        final FloatW w4 = new FloatW(-0.8f);
        final FloatW w5 = new FloatW(-1f);
        final FloatW w6 = new FloatW(-1.2f);
        final FloatW w7 = new FloatW(-1.4f);
        final FloatW w8 = new FloatW(-1.6f);

        vector.add(w8);
        vector.add(w7);
        vector.add(w6);
        vector.add(w5);
        vector.add(w4);
        vector.add(w3);
        vector.add(w2);
        vector.add(w1);

        for (int i = 0; i < 8; i++) {
            vector.addElement(w0);
        }

        return vector;
    }
}
