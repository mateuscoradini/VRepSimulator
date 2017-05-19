package br.com.coradini.robot.monitor;

import java.io.IOException;
import java.text.ParseException;

import br.com.coradini.robot.comport.FuzzyWallFollower;
import br.com.coradini.robot.comport.PositionLocator;
import br.com.coradini.robot.handlers.RobotHandler;
import br.com.coradini.robot.handlers.UltrassonicHandler;

public class RobotComportMonitor implements Runnable {

	FuzzyWallFollower fuzzyWallFollower;
	RobotHandler robot;
	int intervalMs = 300;
	PositionLocator locator;

	public RobotComportMonitor(FuzzyWallFollower fuzzyWallFollower, RobotHandler robot,PositionLocator locator) throws IOException {
		this.fuzzyWallFollower = fuzzyWallFollower;
		this.fuzzyWallFollower.createEngineWallFollower();
		this.robot = robot;
		// Pose updater
		this.locator = locator;
		
	

		int intervalSeconds = intervalMs / 1000;
	}

	@Override
	public void run() {
		while (true) {
			update();
			try {
				Thread.sleep(intervalMs);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public void update() {
		UltrassonicHandler sideSensor_sensor7 = robot.getListUltrassonicSensors().get(6);
		UltrassonicHandler diagSensor_sensor5 = robot.getListUltrassonicSensors().get(4);
		UltrassonicHandler rightFrontSensor_sensor4 = robot.getListUltrassonicSensors().get(3);
		sideSensor_sensor7 = robot.getSensorProximity(sideSensor_sensor7);
		diagSensor_sensor5 = robot.getSensorProximity(diagSensor_sensor5);
		rightFrontSensor_sensor4 = robot.getSensorProximity(rightFrontSensor_sensor4);
		fuzzyWallFollower.getEngine().getInputVariable("side_sensor").setValue(sideSensor_sensor7.getDetectedPoint().getArray()[2]);
		fuzzyWallFollower.getEngine().getInputVariable("diag_sensor").setValue(sideSensor_sensor7.getDetectedPoint().getArray()[2]);
		fuzzyWallFollower.getEngine().getInputVariable("right_front_sensor").setValue(sideSensor_sensor7.getDetectedPoint().getArray()[2]);

		fuzzyWallFollower.getEngine().process();

		double linear = fuzzyWallFollower.getEngine().getOutputValue("whell_linear_speed");
		double angular = fuzzyWallFollower.getEngine().getOutputValue("whell_angular_speed");

		linear = linear * 50;
		angular = angular * 50;
		
		robot.drive(linear, angular);
		
		try {
			locator.initializePositionGroundTruth();
			locator.odometryPositionUpdater();
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
