package br.com.pos.unicamp.vrep.actuators;

import br.com.pos.unicamp.vrep.exceptions.InvalidResponseException;
import br.com.pos.unicamp.vrep.utils.RemoteAPI;
import coppelia.IntW;
import coppelia.remoteApi;

public class Motors {

    private final IntW leftMotor;

    private final IntW rightMotor;

    public Motors() {
        leftMotor = getMotorFromRemoteAPI("Pioneer_p3dx_leftMotor");
        rightMotor = getMotorFromRemoteAPI("Pioneer_p3dx_rightMotor");
    }

    private IntW getMotorFromRemoteAPI(final String motorId) {
        final IntW motor = new IntW(-1);

        int response = RemoteAPI.getInstance().simxGetObjectHandle(RemoteAPI.getClientId(),
                                                                   motorId,
                                                                   motor,
                                                                   remoteApi.simx_opmode_oneshot_wait);

        if (response != remoteApi.simx_return_ok) {
            throw new InvalidResponseException("The motor " + motorId + " could not be instantiated.");
        }

        return motor;
    }

    public void setLeftMotorVelocity(final float velocity) {
        setMotorVelocity(leftMotor,
                         velocity);
    }

    public void setRightMotorVelocity(final float velocity) {
        setMotorVelocity(rightMotor,
                         velocity);
    }

    private void setMotorVelocity(final IntW motor,
                                  final float velocity) {
        RemoteAPI.getInstance().simxSetJointTargetVelocity(RemoteAPI.getClientId(),
                                                           motor.getValue(),
                                                           velocity,
                                                           remoteApi.simx_opmode_streaming);
    }
}
