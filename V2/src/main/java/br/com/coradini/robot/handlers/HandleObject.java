package br.com.coradini.robot.handlers;

import coppelia.IntW;

public class HandleObject {
	
	
	private String name;
	private IntW handleSensorID;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public IntW getHandleSensorID() {
		return handleSensorID;
	}

	public void setHandleSensorID(IntW handleSensorID) {
		this.handleSensorID = handleSensorID;
	}

}
