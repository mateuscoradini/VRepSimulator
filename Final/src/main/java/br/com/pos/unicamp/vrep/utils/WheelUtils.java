package br.com.pos.unicamp.vrep.utils;


public final class WheelUtils {
	
	public static final double WHEELS_RAD = 0.0975;
	public static final double ROBOT_PIONEER_WHEEL_DISTANCE = 0.381;
	

	
	public static double vLToDrive(double vLinear, double vAngular) {
		return (((2 * vLinear) - (ROBOT_PIONEER_WHEEL_DISTANCE * vAngular)) / 2 * WHEELS_RAD);
	}

	public static double vRToDrive(double vLinear, double vAngular) {

		return (((2 * vLinear) + (ROBOT_PIONEER_WHEEL_DISTANCE * vAngular)) / 2 * WHEELS_RAD);
	}
}