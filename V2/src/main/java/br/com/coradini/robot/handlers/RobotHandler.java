package br.com.coradini.robot.handlers;

import java.util.ArrayList;
import java.util.List;

import br.com.coradini.robot.pojo.Position;
import br.com.coradini.robot.utils.WheelUtils;

public class RobotHandler extends HandleObject {

	private static final String ROBOT_NAME = "Pioneer_p3dx";
	private static final String ROBOT_ULTRASSENSORS_NAME = "Pioneer_p3dx_ultrasonicSensor";
	private static final String ROBOT_LEFT_MOTOR_NAME = "Pioneer_p3dx_leftMotor";
	private static final String ROBOT_RIGHT_MOTOR_NAME = "Pioneer_p3dx_rightMotor";

	private static final String ROBOT_LEFT_WHEEL_NAME = "Pioneer_p3dx_leftWheel";
	private static final String ROBOT_RIGHT_WHEEL_NAME = "Pioneer_p3dx_rightWheel";

	private Position robotPostion;
	private Position groundTruthPosition;

	private RobotWheelHandler leftMotorWheel;
	private RobotWheelHandler righMotorWheel;

	private RobotWheelHandler leftWheel;
	private RobotWheelHandler rightWheel;

	private List<UltrassonicHandler> listUltrassonicSensors;

	private VREPHandler vREPHandler;

	public VREPHandler getVREPHandler() {
		return vREPHandler;
	}

	public void setvREPHandler(VREPHandler vREPHandler) {
		this.vREPHandler = vREPHandler;
	}

	public RobotHandler(VREPHandler vREPHandlerSimulation) {

		listUltrassonicSensors = new ArrayList<UltrassonicHandler>();
		robotPostion = new Position();
		groundTruthPosition = new Position();
		setName(ROBOT_NAME);
		initializeAllRobotHandles(vREPHandlerSimulation);

	}

	public void initializeAllRobotHandles(VREPHandler VREP_API) {
		this.vREPHandler = VREP_API;
		initializeRobotHandle(VREP_API);
		initializeUltrassonicsHandles(VREP_API);
		initializeWheelsMotorsHandles(VREP_API);
		initializeWheelsHandles(VREP_API);
	}

	private void initializeRobotHandle(VREPHandler VREP_API) {
		HandleObject hObject = VREP_API.getHandle(getName());
		setHandleSensorID(hObject.getHandleSensorID());

	}

	private void initializeWheelsHandles(VREPHandler VREP_API) {
		leftWheel = new RobotWheelHandler(VREP_API);
		leftWheel.setName(ROBOT_LEFT_WHEEL_NAME);
		HandleObject hObjectL = VREP_API.getHandle(leftWheel.getName());
		leftWheel.setHandleSensorID(hObjectL.getHandleSensorID());

		rightWheel = new RobotWheelHandler(VREP_API);
		rightWheel.setName(ROBOT_RIGHT_WHEEL_NAME);
		HandleObject hObjectR = VREP_API.getHandle(rightWheel.getName());
		rightWheel.setHandleSensorID(hObjectR.getHandleSensorID());

	}

	private void initializeWheelsMotorsHandles(VREPHandler VREP_API) {
		leftMotorWheel = new RobotWheelHandler(VREP_API);
		leftMotorWheel.setName(ROBOT_LEFT_MOTOR_NAME);
		HandleObject hObjectL = VREP_API.getHandle(leftMotorWheel.getName());
		leftMotorWheel.setHandleSensorID(hObjectL.getHandleSensorID());

		righMotorWheel = new RobotWheelHandler(VREP_API);
		righMotorWheel.setName(ROBOT_RIGHT_MOTOR_NAME);
		HandleObject hObjectR = VREP_API.getHandle(righMotorWheel.getName());
		righMotorWheel.setHandleSensorID(hObjectR.getHandleSensorID());

	}

	private void initializeUltrassonicsHandles(VREPHandler VREP_API) {
		for (int i = 0; i < 16; i++) {
			UltrassonicHandler handler = new UltrassonicHandler();
			handler.setName(ROBOT_ULTRASSENSORS_NAME + (i + 1));
			HandleObject hObject = VREP_API.getHandle(handler.getName());
			handler.setHandleSensorID(hObject.getHandleSensorID());
			listUltrassonicSensors.add(handler);
		}
	}

	public UltrassonicHandler getSensorProximity(HandleObject obj) {
		UltrassonicHandler ultrassonicHandler = vREPHandler.readProximitySensor(obj);
		return ultrassonicHandler;

	}

	public void drive(double vLinear, double vAngular) {
		float velocityLeft = (float) WheelUtils.vLToDrive(vLinear, vAngular);
		vREPHandler.setJointTargetVelocity(leftMotorWheel, velocityLeft);

		float velocityRight = (float) WheelUtils.vRToDrive(vLinear, vAngular);
		vREPHandler.setJointTargetVelocity(righMotorWheel, velocityRight);
	}

	public Position getRobotPostion() {
		return robotPostion;
	}

	public void setRobotPostion(Position robotPostion) {
		this.robotPostion = robotPostion;
	}

	public Position getGroundTruthPosition() {
		return groundTruthPosition;
	}

	public void setGroundTruthPosition(Position groundTruthPosition) {
		this.groundTruthPosition = groundTruthPosition;
	}

	public RobotWheelHandler getLeftWheel() {
		return leftWheel;
	}

	public void setLeftWheel(RobotWheelHandler leftWheel) {
		this.leftWheel = leftWheel;
	}

	public List<UltrassonicHandler> getListUltrassonicSensors() {
		return listUltrassonicSensors;
	}

	public void setListUltrassonicSensors(List<UltrassonicHandler> listUltrassonicSensors) {
		this.listUltrassonicSensors = listUltrassonicSensors;
	}

	public RobotWheelHandler getLeftMotorWheel() {
		return leftMotorWheel;
	}

	public void setLeftMotorWheel(RobotWheelHandler leftMotorWheel) {
		this.leftMotorWheel = leftMotorWheel;
	}

	public RobotWheelHandler getRighMotorWheel() {
		return righMotorWheel;
	}

	public void setRighMotorWheel(RobotWheelHandler righMotorWheel) {
		this.righMotorWheel = righMotorWheel;
	}

	public RobotWheelHandler getRightWheel() {
		return rightWheel;
	}

	public void setRightWheel(RobotWheelHandler rightWheel) {
		this.rightWheel = rightWheel;
	}

}
