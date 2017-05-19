package br.com.coradini.robot.utils;

import br.com.coradini.robot.constants.Constants;

public final class WheelUtils {

	
	public static double vLToDrive(double vLinear, double vAngular) {
		return (((2 * vLinear) - (Constants.ROBOT_PIONEER_WHEEL_DISTANCE * vAngular)) / 2 * Constants.WHEELS_RAD);
	}

	public static double vRToDrive(double vLinear, double vAngular) {

		return (((2 * vLinear) + (Constants.ROBOT_PIONEER_WHEEL_DISTANCE * vAngular)) / 2 * Constants.WHEELS_RAD);
	}
}
