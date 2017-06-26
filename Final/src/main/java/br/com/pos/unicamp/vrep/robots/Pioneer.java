package br.com.pos.unicamp.vrep.robots;

import java.util.List;

import br.com.pos.unicamp.vrep.actuators.Motors;
import br.com.pos.unicamp.vrep.behaviors.BehaviorHandler;
import br.com.pos.unicamp.vrep.behaviors.GoToGoal;
import br.com.pos.unicamp.vrep.behaviors.pathfinding.Node;
import br.com.pos.unicamp.vrep.common.Coordinate;
import br.com.pos.unicamp.vrep.common.Pose;
import br.com.pos.unicamp.vrep.exceptions.InvalidResponseException;
import br.com.pos.unicamp.vrep.remote.HTTPPoseUpdater;
import br.com.pos.unicamp.vrep.sensors.image.ImageDetector;
import br.com.pos.unicamp.vrep.sensors.ultrasonic.UltrasonicSensors;
import br.com.pos.unicamp.vrep.utils.RemoteAPI;
import coppelia.FloatWA;
import coppelia.IntW;
import coppelia.remoteApi;

import static br.com.pos.unicamp.vrep.utils.LoopUtils.loop;
import static br.com.pos.unicamp.vrep.utils.LoopUtils.sleep;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Pioneer {

	public static final float wheelRadius = 0.0975F;

	public static final float wheelbase = 0.381F;

	private final IntW handle;

	private ImageDetector imageDetector = new ImageDetector();

	private UltrasonicSensors ultrasonicSensors = new UltrasonicSensors();

	private Pose oldPose = null;

	private Pose currentPose = null;

	private Motors motors = new Motors();

	private final HTTPPoseUpdater poseUpdater = new HTTPPoseUpdater();

	public Pioneer() {
		handle = getObjectHandleFromRemoteAPI();

		firstRefresh();
	}

	private void firstRefresh() {
		simxGetObjectPosition(handle, remoteApi.simx_opmode_streaming);
		simxGetObjectOrientation(handle, remoteApi.simx_opmode_streaming);
	}

	public Pose evaluatedPosition() {
		return poseUpdater.get();
	}

	public Pose getCurrentPose() {
		return currentPose;
	}

	public Pose getOldPose() {
		return oldPose;
	}

	public void evaluatePosition() {
		/*
		 * TODO: Refactor this ugly method. It's a good idea to separate it in a
		 * VelocityEvaluation class, and perhaps you can move the
		 * 'keepPoseUpdated' method too.
		 */
		motors.getLeftMotor().evaluateVelocity();
		motors.getRightMotor().evaluateVelocity();

		final float oldTheta = oldPose.getTheta();
		final float leftMotorDistance = wheelRadius * motors.getLeftMotor().angleVariation();
		final float rightMotorDistance = wheelRadius * motors.getRightMotor().angleVariation();

		final float theta = oldTheta + ((rightMotorDistance - leftMotorDistance) / wheelbase);

		final float distance = (leftMotorDistance + rightMotorDistance) / 2;

		final float x = oldPose.getCoordinate().getX() + (distance * (float) cos(oldTheta));
		final float y = oldPose.getCoordinate().getY() + (distance * (float) sin(oldTheta));

		oldPose = currentPose;
		currentPose = new Pose(new Coordinate(x, y, 0F), theta);
	}

	boolean first = true;

	public Coordinate getGroundTruthCoordinates() {		
		return simxGetObjectPosition(handle, remoteApi.simx_opmode_buffer);
	}

	public Pose getGroundTruth() {
		final float[] floats = simxGetObjectOrientation(handle, remoteApi.simx_opmode_buffer);
		float theta = Float.parseFloat(String.format("%+.4f", floats[2]).replaceAll(",", "."));
		//float theta = floats[2];
		return new Pose(getGroundTruthCoordinates(), theta);

	}

	private float[] simxGetObjectOrientation(final IntW handle, final int mode) {
		final FloatWA eulerAngles = new FloatWA(0);

		final Integer response = RemoteAPI.getInstance().simxGetObjectOrientation(RemoteAPI.getClientId(),
				handle.getValue(), -1, eulerAngles, mode);

		if (response == remoteApi.simx_return_ok) {
			return eulerAngles.getArray();
		} else if (response == remoteApi.simx_return_novalue_flag) {
			return new float[] { 0, 0, 0 };
		}

		throw new InvalidResponseException("Pioneer could not return its current orientation. Status: " + response);
	}

	private Coordinate simxGetObjectPosition(final IntW handle, final int mode) {
		final FloatWA position = new FloatWA(0);

		final Integer response = RemoteAPI.getInstance().simxGetObjectPosition(RemoteAPI.getClientId(),
				handle.getValue(), -1, position, mode);

		if (response == remoteApi.simx_return_ok) {
			return new Coordinate(position.getArray());
		} else if (response == remoteApi.simx_return_novalue_flag) {
			return Coordinate.empty();
		}

		throw new InvalidResponseException("Pioneer could not return its current position. Status: " + response);
	}

	private IntW getObjectHandleFromRemoteAPI() {
		final IntW handleSensor = new IntW(-1);
		final Integer response = RemoteAPI.getInstance().simxGetObjectHandle(RemoteAPI.getClientId(), "Pioneer_p3dx",
				handleSensor, remoteApi.simx_opmode_blocking);
		if (response != remoteApi.simx_return_ok) {
			throw new InvalidResponseException("Pioneer could not be initialized.");
		}

		return handleSensor;
	}

	public void start() {
		final BehaviorHandler behaviorHandler = new BehaviorHandler(this);

		startThread(ultrasonicSensors);

		// You can remove the comment below to start the thread that detects the
		// plant's health.
		// (it's commented to increase the performance... shhh)
		// startThread(imageDetector);

		// This line is not being used
		// keepPoseUpdated();

		while (RemoteAPI.isConnected()) {
			getMotors().setVelocity(2F, 2F);

			behaviorHandler.evaluate();

			// Ohhh! And if you want to log the plant's health, don't forget to
			// remove the line below too!
			// logPlantDetection();

			sleep(500);
		}
	}

	public void start(final List<Node> nodes) {
		startThread(ultrasonicSensors);

		final GoToGoal goToGoal = new GoToGoal(this);

		while (RemoteAPI.isConnected()) {

			goToGoal.keepGoing(nodes);
			sleep(500);
			if (goToGoal.isFinished()) {
				break;
			}
		}
	}

	private void keepPoseUpdated() {
		new Thread(() -> {
			loop(this::evaluatePosition, 100);
		}).start();
	}

	private void logPlantDetection() {
		if (imageDetector.isHealthyPlantDetected()) {
			System.out.println("Wow! Pioneer is seeing some healthy plants ahead!");
		}

		if (imageDetector.isWeakPlantDetected()) {
			System.out.println("Ohhh! Pioneer is seeing some weak plants ahead...");
		}
	}

	private void startThread(final Runnable runnable) {
		new Thread(runnable).start();
	}

	public UltrasonicSensors getUltrasonicSensors() {
		return ultrasonicSensors;
	}

	public Motors getMotors() {
		return motors;
	}
}
