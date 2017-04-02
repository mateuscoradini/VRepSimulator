package br.com.pos.unicamp.vrep;

import java.util.Vector;

import coppelia.BoolW;
import coppelia.FloatW;
import coppelia.FloatWA;
import coppelia.IntW;
import coppelia.remoteApi;

public class PioneerRobot {

    private static final remoteApi VREP_API = new remoteApi();
    private static final String IP = "127.0.0.1";
    private static final int PORT = 25000;

    private static final float NO_DETECT_DIST = 0.5f;
    private static final float MAX_DETECTION_DIST = 0.2f;
    private static final float INITIAL_VELOCITY = 2f;

    private Vector<FloatW> braitenbergL = new Vector<FloatW>();

    // private float[] detect = new Vector<Float>();
    private Vector<FloatW> braitenbergR = new Vector<FloatW>();
    private Vector<IntW> proximitySensors = new Vector<IntW>();
    private float[] detect = new float[16];
    private IntW leftMotor;
    private IntW rightMotor;
    private int clientID;

    public PioneerRobot() {
        initializeRobot();
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

        initMotor(clientID,
                  "Pioneer_p3dx_leftMotor",
                  leftMotor);

        // Right motor
        initMotor(clientID,
                  "Pioneer_p3dx_rightMotor",
                  rightMotor);
    }

    private void initMotor(final int clientID,
                           final String motorId,
                           final IntW motor) {
        int resRightMotor = VREP_API.simxGetObjectHandle(clientID,
                                                         motorId,
                                                         motor,
                                                         VREP_API.simx_opmode_oneshot_wait);
        if (resRightMotor == VREP_API.simx_return_ok) {
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
            int respost = VREP_API.simxGetObjectHandle(clientID,
                                                       sensorName,
                                                       handleSensor,
                                                       VREP_API.simx_opmode_oneshot_wait);
            if (respost == VREP_API.simx_return_ok) {
                System.out.println("Successfully connected to the sensor: " + sensorName);
                proximitySensors.add(handleSensor);
            }
        }
    }

    public void actuation() throws InterruptedException {
        while (VREP_API.simxGetConnectionId(clientID) != -1) {
            for (int i = 0; i < 16; i++) {
                BoolW state = new BoolW(true);
                FloatWA coord = new FloatWA(0);

                IntW detectedObjectHandle = new IntW(0);
                FloatWA detectedSurfaceNormalVector = new FloatWA(0);
                int respost = VREP_API.simxReadProximitySensor(clientID,
                                                               proximitySensors.get(i).getValue(),
                                                               state,
                                                               coord,
                                                               detectedObjectHandle,
                                                               detectedSurfaceNormalVector,
                                                               VREP_API.simx_opmode_buffer);
                if (respost == VREP_API.simx_return_ok) {
                    float dist = coord.getArray()[2];

                    if (!state.getValue() && (dist < NO_DETECT_DIST)) {

                        if (dist < MAX_DETECTION_DIST) {
                            dist = MAX_DETECTION_DIST;
                        }
                        detect[i] = 1 - ((dist - MAX_DETECTION_DIST) / (NO_DETECT_DIST - MAX_DETECTION_DIST));
                    } else {
                        detect[i] = 0;
                    }
                } else {
                    detect[i] = 0;
                }
            }

            float vLeft = INITIAL_VELOCITY;
            float vRight = INITIAL_VELOCITY;

            for (int i = 0; i < 16; i++) {
                vLeft = vLeft + braitenbergL.get(i).getValue() * detect[i];
                vRight = vRight + braitenbergR.get(i).getValue() * detect[i];
            }

            VREP_API.simxSetJointTargetVelocity(clientID,
                                                leftMotor.getValue(),
                                                vLeft,
                                                VREP_API.simx_opmode_streaming);
            VREP_API.simxSetJointTargetVelocity(clientID,
                                                rightMotor.getValue(),
                                                vRight,
                                                VREP_API.simx_opmode_streaming);

            // Wait before restarts the sensor reading cycle
            Thread.sleep(5000);
        }

        closeOpenedConnections();
        System.out.println("Connection closed.");
    }

    private void fillDetectSensors() {
        for (int i = 0; i < 16; i++) {
            float handleSensor = 1f;
            detect[i] = handleSensor;
        }
    }

    private int openConnection() {
        return VREP_API.simxStart(IP,
                                  PORT,
                                  true,
                                  true,
                                  2000,
                                  5);
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
        final FloatW w1 = new FloatW(-0.2f);
        final FloatW w2 = new FloatW(-0.4f);
        final FloatW w3 = new FloatW(-0.6f);
        final FloatW w4 = new FloatW(-0.8f);
        final FloatW w5 = new FloatW(-1f);
        final FloatW w6 = new FloatW(-1.2f);
        final FloatW w7 = new FloatW(-1.4f);
        final FloatW w8 = new FloatW(-1.6f);
        final FloatW w0 = new FloatW(-0f);

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
