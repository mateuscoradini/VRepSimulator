package br.com.coradini.robot.handlers;

import java.sql.Timestamp;
import java.text.ParseException;

import br.com.coradini.robot.constants.Constants;
import coppelia.FloatW;

public class RobotWheelHandler extends HandleObject {

	private VREPHandler VREP_API;

	private float limit = (float) (2 * Math.PI);

	private int lastPosition;
	private Timestamp lastTimestamp;
	private boolean clockWiseSpin;
	private float speed;
	private double wheelsRad;
	private float lastEncoderPosition = 0f;

	public RobotWheelHandler(VREPHandler VREP_API) {
		this.VREP_API = VREP_API;
		lastTimestamp = new Timestamp(System.currentTimeMillis());

	}

	public float calculateSpeed() throws ParseException {

		float delta = getDeltaAngle();

		Timestamp actualTimestamp = new Timestamp(System.currentTimeMillis());
		float timeDelta = actualTimestamp.getTime() - lastTimestamp.getTime();
		lastTimestamp = actualTimestamp;

		speed = (float) (Constants.WHEELS_RAD * (delta / timeDelta));

		if (clockWiseSpin) {
			return -speed;
		}

		return speed;
	}

	public int normalizeAngle(int angle) {
		int newAngle = angle;
		while (newAngle <= -180)
			newAngle += 360;
		while (newAngle > 180)
			newAngle -= 360;
		return newAngle;
	}

	public float getDeltaAngle() {
		FloatW position = VREP_API.getJointPosition(super.getHandleSensorID());

		float delta = calculateDelta(lastEncoderPosition, position.getValue(), clockWiseSpin);
		if (delta > Math.PI) {
			delta = (float) ((2 * Math.PI) - delta);
			clockWiseSpin = !clockWiseSpin;
		}

		return delta;
	}

	private float calculateDelta(float lastEncoderPosition, float actualPosition, boolean clockWiseSpin2) {
		lastEncoderPosition = convertNegativePi360(lastEncoderPosition);
		actualPosition = convertNegativePi360(actualPosition);

		float ans = Math.abs(lastEncoderPosition - actualPosition);

		if (clockWiseSpin2) {
			if (actualPosition < lastEncoderPosition) {
				return limit - ans;
			}
			return ans;
		}

		if (actualPosition < lastEncoderPosition) {
			return limit - ans;
		}
		return ans;
	}

	public float convertNegativePi360(float angle) {
		return (float) ((limit + angle) % limit);

	}

	public RobotWheelHandler() {

	}

	public int getLastPosition() {
		return lastPosition;
	}

	public void setLastPosition(int lastPosition) {
		this.lastPosition = lastPosition;
	}

	

	public boolean isClockWiseSpin() {
		return clockWiseSpin;
	}

	public void setClockWiseSpin(boolean clockWiseSpin) {
		this.clockWiseSpin = clockWiseSpin;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public double getWheelsRad() {
		return wheelsRad;
	}

	public void setWheelsRad(double wheelsRad) {
		this.wheelsRad = wheelsRad;
	}

}
