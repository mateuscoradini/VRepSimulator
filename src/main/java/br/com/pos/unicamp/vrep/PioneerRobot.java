package br.com.pos.unicamp.vrep;

import java.util.Vector;

import br.com.pos.unicamp.vrep.detectors.RequestSensorUltrassonic;
import coppelia.BoolW;
import coppelia.FloatWA;
import coppelia.IntW;
import coppelia.remoteApi;

public class PioneerRobot {

    private static final remoteApi VREP_API = new remoteApi();
    private static final String IP = "127.0.0.1";
    private static final int PORT = 25000;

    //private static final float NO_DETECT_DIST = 0.7f;
    private static final float MAX_DETECTION_DIST = 0.5f;
    private static final float MAX_DETECTION_DIST_FRONT = 0.6f;
    private static final float MIN_DETECTION_DIST = 0.45f;

    private static final float MIN_DETECTION_DIST_TURN = 0.25f;
    
    private static final float MIN_COLISION_DETECTION_DIST_TURN = 0.12f;

    // private static final float MAX_DETECTION_DIST_VERTICAL = 0.4f;
    private static final float INITIAL_VELOCITY = 0.5f;

    private static final int SENSOR_1_NUMBER = 1;
    private static final int SENSOR_4_NUMBER = 3;
    private static final int SENSOR_5_NUMBER = 4;

    private static final int SENSOR_3_NUMBER = 2;
    private static final int SENSOR_6_NUMBER = 5;
    private static final int SENSOR_7_NUMBER = 6;
    private static final int SENSOR_8_NUMBER = 7;
    
    
    private static final int SENSOR_12_NUMBER = 11;
    private static final int SENSOR_13_NUMBER = 12;
    
    
    //private Vector<FloatW> braitenbergL = new Vector<FloatW>();

    // private float[] detect = new Vector<Float>();
    //private Vector<FloatW> braitenbergR = new Vector<FloatW>();
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

        // braitenbergL = fillBraitenbergL();
        // braitenbergR = fillBraitenbergR();

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

    boolean encontrouObstaculo = false;
    boolean jaVoltou = false;

    public void actuation() throws InterruptedException {
        while (VREP_API.simxGetConnectionId(clientID) != -1) {

            float leftMotorVelocity = INITIAL_VELOCITY;
            float rightMotorVelocity = INITIAL_VELOCITY;
            
            
            RequestSensorUltrassonic sensor3 = requestSensorUltrassonicDetectetion(SENSOR_3_NUMBER);

            //Frontais
            RequestSensorUltrassonic sensor4 = requestSensorUltrassonicDetectetion(SENSOR_4_NUMBER);
            RequestSensorUltrassonic sensor5 = requestSensorUltrassonicDetectetion(SENSOR_5_NUMBER);

            RequestSensorUltrassonic sensor6 = requestSensorUltrassonicDetectetion(SENSOR_6_NUMBER);

            //Direito
            RequestSensorUltrassonic sensor7 = requestSensorUltrassonicDetectetion(SENSOR_7_NUMBER);
            RequestSensorUltrassonic sensor8 = requestSensorUltrassonicDetectetion(SENSOR_8_NUMBER);

            //Tem não tem algo na frente
            if (seNaoHaObstaculoAFrente(sensor4, sensor5, sensor6)) {

                if (seHaAlgoDetectadoEntreDireita(sensor7) && seHaAlgoDetectadoEntreDireita(sensor8)) {
                    System.out.println("1");
                    setTargetVelocity(leftMotor, leftMotorVelocity);
                    setTargetVelocity(rightMotor, rightMotorVelocity);

                }

                if (distMinDireitaFollow(sensor7, sensor8)) {
                    System.out.println("2");
                    setTargetVelocity(leftMotor, 0.5f);
                    setTargetVelocity(rightMotor, 0f);
                    Thread.sleep(1500);
                    setTargetVelocity(leftMotor, 0.5f);
                    setTargetVelocity(rightMotor, 0.5f);
                    Thread.sleep(1500);
                }

                if (distMaxDireitaFollow(sensor7, sensor8) && encontrouObstaculo) {
                    System.out.println("3");
                    setTargetVelocity(leftMotor, 0.5f);
                    setTargetVelocity(rightMotor, 0f);
                    Thread.sleep(1500);
                    setTargetVelocity(leftMotor, 0.5f);
                    setTargetVelocity(rightMotor, 0.5f);
                    Thread.sleep(1000);
                }

                System.out.println("|||||||||||||||||||||||||||||||||||||");

            } else {
                
                //TODO Construir inteligencia para fazer rollback da sala que entrou

                if (distMinDireita(sensor7)) {
                    System.out.println("0");
                    
                    //NOTA: É mais facil o robo aprender a contornar o objeto do que controlar a precisão dos sensores.
                    virar90praEsquerdaRapida();                   
                    encontrouObstaculo = true;
                    
//                    RequestSensorUltrassonic sensor12 = requestSensorUltrassonicDetectetion(SENSOR_12_NUMBER);
//                    RequestSensorUltrassonic sensor13 = requestSensorUltrassonicDetectetion(SENSOR_13_NUMBER);
//                    if(!distMinCentral(sensor12) || !distMinCentral(sensor13) && (!(distMinCentral(sensor4) || distMinCentral(sensor3) || distMinCentral(sensor5)))){
//                        voltar();
//                    }
                }
                
//                //Ele se auto-corrige em 0.1 ms pra tras, caso ele nao consiga dar a volta na sala, a sala é um ambiente não exploravel por limitação de detecao dos sensores (programacao)
//                if((distMinCentral(sensor4) || distMinCentral(sensor3) || distMinCentral(sensor5)) && !distMinDireita(sensor7) && !jaVoltou){
//                    voltar();
//                    jaVoltou = true;                   
//                }else{
//                    RequestSensorUltrassonic sensor12 = requestSensorUltrassonicDetectetion(SENSOR_12_NUMBER);
//                    RequestSensorUltrassonic sensor13 = requestSensorUltrassonicDetectetion(SENSOR_13_NUMBER);
//                    if(distMinCentral(sensor12) || distMinCentral(sensor13)){
//                        setTargetVelocity(leftMotor, 0.1f);
//                        setTargetVelocity(rightMotor, 0.1f);
//                    }
//                    
//                }
            }

        }

        closeOpenedConnections();
        System.out.println("Connection closed.");
    }

    private boolean distMinCentral(RequestSensorUltrassonic sensor) {
        float dist = sensor.getCoord().getArray()[2];
        boolean detected = sensor.getState().getValue();
        if (detected && (dist <= MIN_COLISION_DETECTION_DIST_TURN)) {
            System.out.println("Detectado distancia de colisao:" + sensor.getSensorName());
            return true;
        }
        return false;
    }

    private boolean noDetectedRight(RequestSensorUltrassonic sensor8) {

        boolean detected8 = sensor8.getState().getValue();
        float dist8 = sensor8.getCoord().getArray()[2];

        if (detected8) {
            if (dist8 > MIN_DETECTION_DIST) {
                return true;
            }
            return false;

        }
        return false;

    }

    private boolean distMaxDireitaFollow(RequestSensorUltrassonic sensor7, RequestSensorUltrassonic sensor8) {

        boolean detected7 = sensor7.getState().getValue();
        boolean detected8 = sensor8.getState().getValue();

        float dist8 = sensor8.getCoord().getArray()[2];
        float dist7 = sensor7.getCoord().getArray()[2];
        
        //Caso queira uma distancia menor de seguir parede, temos que calibrar o tempo de resposta do motores pra ser mais devagar para fazer as curvas sem penalizacao
        float minimaDistanciaTeste = MIN_DETECTION_DIST - 0.1f;

        if ((detected7 && dist7 >= minimaDistanciaTeste) && (detected8 && dist8 >= minimaDistanciaTeste)) {
            return true;
        }
        return false;

    }

    private boolean distMinDireitaFollow(RequestSensorUltrassonic sensor7, RequestSensorUltrassonic sensor8) {
        float dist7 = sensor7.getCoord().getArray()[2];
        float dist8 = sensor8.getCoord().getArray()[2];
        boolean detected7 = sensor7.getState().getValue();
        
        //float minimaDistanciaTeste = MIN_DETECTION_DIST - 0.1f;
        
        if ((!detected7 && dist7 <= MIN_DETECTION_DIST)) {
            return true;
        }
        return false;

    }    

    private boolean distMinDireita(RequestSensorUltrassonic sensor) {
        float dist = sensor.getCoord().getArray()[2];
        boolean detected = sensor.getState().getValue();
        if (detected && (dist <= MIN_DETECTION_DIST)) {
            return true;
        }
        return false;

    }

    private boolean seHaAlgoDetectadoEntreDireita(RequestSensorUltrassonic sensor) {
        float dist = sensor.getCoord().getArray()[2];
        boolean detected = sensor.getState().getValue();
        if (detected && (dist >= MAX_DETECTION_DIST_FRONT)) {
            return true;
        }
        return false;

    }

    private boolean seNaoHaObstaculoAFrente(RequestSensorUltrassonic sensor4, RequestSensorUltrassonic sensor5, RequestSensorUltrassonic sensor6) {
        return !seCaminhoAFrente(sensor4) && !seCaminhoAFrente(sensor5) && !seCaminhoAFrente(sensor6);
    }

    private boolean seCaminhoAFrente(RequestSensorUltrassonic sensor) {
        float dist = sensor.getCoord().getArray()[2];
        boolean detected = sensor.getState().getValue();
        if (detected && dist < MAX_DETECTION_DIST) {
            System.out.println("Há algo na frente:" + sensor.getSensorName());
            return true;
        }
        System.out.println("Não Há algo na frente:" + sensor.getSensorName());
        return false;
    }

    private void virar90praEsquerdaRapida() {
        setTargetVelocity(leftMotor, 0.1f);
        setTargetVelocity(rightMotor, 0.7f);
    }
    
    private void voltar() {
        setTargetVelocity(leftMotor, -0.2f);
        setTargetVelocity(rightMotor, -0.1f);
    }

    private void virar90praDireita() {
        setTargetVelocity(leftMotor, 0.8f);
        setTargetVelocity(rightMotor, -0.2f);
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

}
