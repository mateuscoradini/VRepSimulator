package br.com.pos.unicamp.vrep.actuators;

public class Motors {

    private final Motor leftMotor;

    private final Motor rightMotor;

    public Motors() {
        leftMotor = new Motor("Pioneer_p3dx_leftMotor");
        rightMotor = new Motor("Pioneer_p3dx_rightMotor");
    }

    public void setVelocity(final float motorLeftVelocity,
                            final float motorRightVelocity) {
        leftMotor.setTargetVelocity(motorLeftVelocity);
        rightMotor.setTargetVelocity(motorRightVelocity);
    }

    public Motor getLeftMotor() {
        return leftMotor;
    }

    public Motor getRightMotor() {
        return rightMotor;
    }
}
