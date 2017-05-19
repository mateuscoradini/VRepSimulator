package br.com.pos.unicamp.vrep.robots;

import br.com.pos.unicamp.vrep.actuators.Motors;
import br.com.pos.unicamp.vrep.sensors.image.ImageDetector;
import br.com.pos.unicamp.vrep.sensors.ultrasonic.UltrasonicSensor;
import br.com.pos.unicamp.vrep.sensors.ultrasonic.UltrasonicSensors;
import br.com.pos.unicamp.vrep.utils.RemoteAPI;

import static br.com.pos.unicamp.vrep.utils.LoopUtils.sleep;

public class Pioneer {

    private ImageDetector imageDetector;

    private UltrasonicSensors ultrasonicSensors;

    private Motors motors;

    private boolean legacyState = true;

    private boolean legacyIsSomethingDetected = false;

    public Pioneer() {
        imageDetector = new ImageDetector();
        ultrasonicSensors = new UltrasonicSensors();
        motors = new Motors();
    }

    public void start() {
        while (RemoteAPI.isConnected()) {
            startThread(imageDetector);
            startThread(ultrasonicSensors);

           // logPlantDetection();
            legacyMovementAlgorithm();

            sleep(500);
        }
    }

    private void legacyMovementAlgorithm() {
        final UltrasonicSensor sensor7 = ultrasonicSensors.getSensor(7);
        final UltrasonicSensor sensor8 = ultrasonicSensors.getSensor(8);
        final UltrasonicSensor sensor9 = ultrasonicSensors.getSensor(9);

        if (ultrasonicSensors.doesNotHaveAnythingAhead()) {

            if (ultrasonicSensors.hasSomethingToTheRight()) {
                if (sensor8.isDetectedConsideringMinDistance() && sensor9.isDetectedConsideringMinDistance()) {
                    motors.setLeftMotorVelocity(0.4F);
                    motors.setRightMotorVelocity(0.2F);
                    sleep(1000);
                } else {
                    motors.setLeftMotorVelocity(0.4F);
                    motors.setRightMotorVelocity(0.4F);
                    sleep(1000);
                }
            }

            if (!sensor7.isDetected() && !sensor7.isValidMinDistance()) {
                motors.setLeftMotorVelocity(0.5F);
                motors.setRightMotorVelocity(0F);
                sleep(1500);

                motors.setLeftMotorVelocity(0.5F);
                motors.setRightMotorVelocity(0.5F);
                sleep(1500);
            }

            if (sensor7.isDetectedConsideringMinDistance() && sensor8.isDetectedConsideringMinDistance() && legacyIsSomethingDetected) {
                motors.setLeftMotorVelocity(0.5F);
                motors.setRightMotorVelocity(-0.1F);
                sleep(1500);

                motors.setLeftMotorVelocity(0.5F);
                motors.setRightMotorVelocity(0.5F);
                sleep(2000);
            }

            if (legacyState) {
                motors.setLeftMotorVelocity(0.5F);
                motors.setRightMotorVelocity(0.5F);
                sleep(1000);

                legacyState = false;
            }
        } else {
            if (sensor7.isDetected() && !sensor7.isValidMinDistance()) {
                motors.setLeftMotorVelocity(-0.1F);
                motors.setRightMotorVelocity(0.3F);
                sleep(500);

                legacyIsSomethingDetected = true;
                legacyState = false;
            }
        }
    }

    private void logPlantDetection() {
        if (imageDetector.isHealthyPlantDetected()) {
            System.out.println("Healthy Plant Detected.");
        }

        if (imageDetector.isWeakPlantDetected()) {
            System.out.println("Weak Plant Detected.");
        }
    }

    private void startThread(final Runnable runnable) {
        new Thread(runnable).start();
    }
}
