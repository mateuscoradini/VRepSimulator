package br.com.coradini.robot.comport;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.coradini.robot.constants.Constants;
import br.com.coradini.robot.handlers.RobotHandler;
import br.com.coradini.robot.handlers.RobotWheelHandler;
import br.com.coradini.robot.pojo.Position;
import coppelia.FloatW;
import coppelia.FloatWA;

public class PositionLocator {

	private RobotHandler robot;
	
	private float limit = (float) (2 * Math.PI);

	private Position robotLastPosition;
	private Position initialPose;
	private Position positionGroundTruth;
	private Timestamp lastTimestamp;
	
	boolean reset = true;

	public PositionLocator(RobotHandler robot) {
		this.robot = robot;
		initializePositionGroundTruth();
		lastTimestamp = new Timestamp(System.currentTimeMillis());

	}

	public void initializePositionGroundTruth() {
		/* Get the robot current absolute position */

		FloatWA floatWAPosition = robot.getVREPHandler().getObjectPostion(robot);
		FloatWA floatWAOrientation = robot.getVREPHandler().getObjectOrientation(robot);

		initialPose = new Position();
		initialPose.setX(floatWAPosition.getArray()[0]);
		initialPose.setY(floatWAPosition.getArray()[1]);
		initialPose.setOrientation(floatWAOrientation.getArray()[2]);
		if(reset){
			robotLastPosition = initialPose;
			reset = false;
		}

		FloatW jointLeftMotor = robot.getVREPHandler().getJointPosition(robot.getLeftMotorWheel().getHandleSensorID());
		FloatW jointRightMotor = robot.getVREPHandler().getJointPosition(robot.getRighMotorWheel().getHandleSensorID());

		System.out.println("Robot Joint Position Left Wheel: " + jointLeftMotor.getValue());
		System.out.println("Robot Joint Position Right Wheel: " + jointRightMotor.getValue());
		
		System.out.println("GroundThruth {"+initialPose.getX()+" X, "+initialPose.getY()+" Y, "+initialPose.getOrientation()+" O");
		
		
		robotLastPosition = initialPose;		
	}

	public void createDraw(){
		  
	}

	public void odometryPositionUpdater() throws ParseException {		
		
		RobotWheelHandler leftWheel = robot.getLeftWheel();
		RobotWheelHandler rightWheel = robot.getRightWheel();
		
		
		Timestamp actualTimestamp = new Timestamp(System.currentTimeMillis());
		
		float vL = leftWheel.calculateSpeed();
		float vR = rightWheel.calculateSpeed();
		
		System.out.println("Velocidade Roda Esquerda: "+vL);
		System.out.println("Velocidade Roda Direita:"+vR);
		
		
		float timeDelta = actualTimestamp.getTime() - lastTimestamp.getTime();
		float deltaTheta = (float) ((vR - vL) * (timeDelta / Constants.WHEELS_RAD));
		float deltaSpace = (vR + vL) * (timeDelta / 2);
		
		float x = (float) (robotLastPosition.getX()
				+ (deltaSpace * Math.cos(addDelta(robotLastPosition.getOrientation(), deltaTheta / 2))));
		float y =  (float) (robotLastPosition.getX()
				+ (deltaSpace * Math.sin(addDelta(robotLastPosition.getOrientation(), deltaTheta / 2))));
		float orientation = addDelta(robotLastPosition.getOrientation(), deltaTheta);
		
		robotLastPosition.setX(x);
				robotLastPosition.setY(y);
		
						robotLastPosition.setOrientation(orientation);
		lastTimestamp = new Timestamp(System.currentTimeMillis());
		
		System.out.println("Odometry {"+robotLastPosition.getX()+" X, "+robotLastPosition.getY()+" Y, "+robotLastPosition.getOrientation()+" O");		

	}
	
	private float addDelta(float start, float delta){
		
		float ans = (start + delta) % limit;
		if(ans > Math.PI){
			return ans - limit;
		}		
		return ans;
		
	}

}
