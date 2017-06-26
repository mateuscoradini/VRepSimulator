package br.com.pos.unicamp.vrep.actuators;

public class JointPosition {

    float jointPosition;
    float timestamp;

    JointPosition(final float jointPosition,
                  final float timestamp) {
        this.jointPosition = jointPosition;
        this.timestamp = timestamp;
    }

    public static JointPosition empty() {
        return new JointPosition(0,
                                 0);
    }

    public float getJointPosition() {
        return jointPosition;
    }

    public float getTimestamp() {
        return timestamp;
    }
}
