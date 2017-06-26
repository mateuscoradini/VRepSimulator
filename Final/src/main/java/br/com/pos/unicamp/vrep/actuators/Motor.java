package br.com.pos.unicamp.vrep.actuators;

import br.com.pos.unicamp.vrep.exceptions.InvalidResponseException;
import br.com.pos.unicamp.vrep.utils.RemoteAPI;
import coppelia.IntW;
import coppelia.remoteApi;

import static br.com.pos.unicamp.vrep.utils.NumberUtils.toTruncatedFloat;

public class Motor {

    private final JointPositionHandler jointPositionHandler;
    private final IntW motor;
    private JointPosition oldJointPosition = JointPosition.empty();
    private JointPosition currentJointPosition = JointPosition.empty();

    public Motor(final String motorName) {
        motor = getMotorFromRemoteAPI(motorName);
        jointPositionHandler = new JointPositionHandler(motorName);
    }

    public void setTargetVelocity(final float velocity) {
        simxSetJointTargetVelocity(velocity);
    }

    public void evaluateVelocity() {
        final JointPosition newJointPosition = jointPositionHandler.getJointPosition();

        if (isNoise(newJointPosition)) {
            return;
        }

        oldJointPosition = currentJointPosition;
        currentJointPosition = newJointPosition;
    }

    private boolean isNoise(final JointPosition newJointPosition) {
        float variation = angleVariation(currentJointPosition,
                                         newJointPosition);
        return variation > 2.0;
    }

    public float evaluatedVelocity() {
        float angleVariation = angleVariation();
        float timeVariation = timeVariation();

        if (timeVariation == 0) {
            return 0;
        }

        return angleVariation / timeVariation;
    }

    public float angleVariation() {
        return angleVariation(oldJointPosition,
                              currentJointPosition);
    }

    private float convertToRad(final float value) {
        if (value > 0) {
            return toTruncatedFloat(value);
        }

        return toTruncatedFloat(value + (float) (2 * Math.PI));
    }

    private float angleVariation(final JointPosition oldJointPosition,
                                 final JointPosition newJointPosition) {
        float oldAngle = convertToRad(oldJointPosition.getJointPosition());
        float newAngle = convertToRad(newJointPosition.getJointPosition());

        float delta = newAngle - oldAngle;

        if (delta < 0) {
            delta = delta + (float) (2 * Math.PI);
        }

        return delta;
    }

    public float timeVariation() {
        return currentJointPosition.getTimestamp() - oldJointPosition.getTimestamp();
    }

    public JointPosition getCurrentJointPosition() {
        return currentJointPosition;
    }

    public JointPosition getOldJointPosition() {
        return oldJointPosition;
    }

    private int simxSetJointTargetVelocity(final float velocity) {
        return RemoteAPI.getInstance().simxSetJointTargetVelocity(RemoteAPI.getClientId(),
                                                                  motor.getValue(),
                                                                  velocity,
                                                                  remoteApi.simx_opmode_streaming);
    }

    private IntW getMotorFromRemoteAPI(final String motorId) {
        final IntW motor = new IntW(-1);

        int response = RemoteAPI.getInstance().simxGetObjectHandle(RemoteAPI.getClientId(),
                                                                   motorId,
                                                                   motor,
                                                                   remoteApi.simx_opmode_blocking);

        if (response != remoteApi.simx_return_ok) {
            throw new InvalidResponseException("The motor " + motorId + " could not be instantiated. Status: " + response);
        }

        return motor;
    }
}
