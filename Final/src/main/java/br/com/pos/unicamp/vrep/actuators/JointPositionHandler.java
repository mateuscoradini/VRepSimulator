package br.com.pos.unicamp.vrep.actuators;

import br.com.pos.unicamp.vrep.utils.RemoteAPI;
import coppelia.CharWA;
import coppelia.remoteApi;

class JointPositionHandler {

    private final String motorName;

    private final CharWA signalValue;

    JointPositionHandler(final String motorName) {
        this.motorName = motorName;
        this.signalValue = setupSignal();
    }

    private CharWA setupSignal() {
        final CharWA signalValue = new CharWA("");

        RemoteAPI.getInstance().simxGetStringSignal(RemoteAPI.getClientId(),
                                                    signalName(),
                                                    signalValue,
                                                    remoteApi.simx_opmode_streaming);
        return signalValue;
    }

    public JointPosition getJointPosition() {
        refreshSignalValue();

        final String[] split = signalValue.getString().split("\\-\\-");

        if (split.length == 2) {
            return new JointPosition(Float.parseFloat(split[0]),
                                     Float.parseFloat(split[1]));
        }

        return new JointPosition(0,
                                 0);
    }

    private void refreshSignalValue() {
        RemoteAPI.getInstance().simxGetStringSignal(RemoteAPI.getClientId(),
                                                    signalName(),
                                                    signalValue,
                                                    remoteApi.simx_opmode_buffer);
    }

    private String signalName() {
        return motorName + "--jointPosition--timestamp";
    }
}
