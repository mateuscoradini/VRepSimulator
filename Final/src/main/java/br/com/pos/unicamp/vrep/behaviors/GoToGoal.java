package br.com.pos.unicamp.vrep.behaviors;

import static br.com.pos.unicamp.vrep.behaviors.pathfinding.GridUtils.positionToCoordinate;

import java.util.List;

import br.com.pos.unicamp.vrep.behaviors.pathfinding.Node;
import br.com.pos.unicamp.vrep.common.Coordinate;
import br.com.pos.unicamp.vrep.common.Pose;
import br.com.pos.unicamp.vrep.robots.Pioneer;

public class GoToGoal {
	
	private int i = 0;

	private Coordinate destination;

	private boolean finished = false;

	private Pioneer pioneer;

	private double lastHeadingError = 0;

	private double headingErrorIntegral = 0;

	private double referenceHeading = 0;

	private boolean found = true;

	public GoToGoal(final Pioneer pioneer) {
		this.pioneer = pioneer;
	}

	private void reset(final Node destinationNode) {
		found = false;

		destination = positionToCoordinate(destinationNode.getPosition());
		referenceHeading = getHeading(pioneer.getGroundTruth());
		headingErrorIntegral = 0;
		lastHeadingError = 0;
		i = 0;
		
	}

	public void keepGoing(final List<Node> nodes) {
		final Pose groundTruth = pioneer.getGroundTruth();

		if (i == 20) {
			referenceHeading = getHeading(pioneer.getGroundTruth());
			headingErrorIntegral = 0;
			lastHeadingError = 0;
			i = 0;
		}
		
		if (isFound(groundTruth)) {
			if (!nodes.isEmpty()) {
				Node e = nodes.get(nodes.size() - 1);
				nodes.remove(e);
				reset(e);
			} else {
				setVelocity(0, 0);
				finished = true;
				return;
			}
		}

		i++;
		double u = u(groundTruth);

		double vL = 1.5 - (u * 2);
		double vR = 1.5 + (u * 2);

		setVelocity(vL, vR);
		// System.out.println("U Value: "+u);
	}

	private boolean isFound(final Pose groundTruth) {
		if (found || distanceToGoal(groundTruth) < 0.5) {
			//TODO DEBUG MODE
			System.out.println("Found");
			found = true;
		}

		return found;
	}

	// TODO Refactor this method consider negative PI in rotation
	private double u(final Pose groundTruth) {
		final double DELTA_T = 500;
		final double PID_P = 0.75;
		final double PID_I = 0.0000500;
		final double PID_D = 0.0;
		
		final double headingError = referenceHeading - groundTruth.getTheta();

		final double headingErrorDerivative = (headingError - lastHeadingError) / DELTA_T;

		headingErrorIntegral += (headingError * DELTA_T);
		lastHeadingError = headingError;

		return (PID_P * headingError) + (PID_I * headingErrorIntegral) + (PID_D * headingErrorDerivative);
	}

	private double u2(final Pose groundTruth) {
		final double DELTA_T = 500;
		final double PID_P = 0.75;
		final double PID_I = 0.000005;
		final double PID_D = 0.0;

		double angle = Math.toDegrees(groundTruth.getTheta());
		double angleRef = Math.toDegrees(referenceHeading);
		final double headingError = referenceHeading - groundTruth.getTheta();

		final double headingErrorDerivative = (headingError - lastHeadingError) / DELTA_T;

		headingErrorIntegral += (headingError * DELTA_T);
		lastHeadingError = headingError;

		return (PID_P * headingError) + (PID_I * headingErrorIntegral) + (PID_D * headingErrorDerivative);
	}

	private double distanceToGoal(final Pose groundTruth) {
		float deltaX = destination.getX() - groundTruth.getCoordinate().getX();
		float deltaY = destination.getY() - groundTruth.getCoordinate().getY();

		return Math.sqrt((deltaX * deltaX) + (deltaY * deltaY));
	}

	private double getHeading(final Pose currentPose) {
		final double deltaX = destination.getX() - currentPose.getCoordinate().getX();
		final double deltaY = destination.getY() - currentPose.getCoordinate().getY();

		double rad = Math.atan(deltaY / deltaX);

		if (deltaX < 0) {
			if (deltaY < 0) {
				rad -= Math.PI;
			} else {
				rad += Math.PI;
			}
		}
		System.out.println("RAD: "+rad);

		return rad;
	}

	private void setVelocity(final double velocityLeft, final double velocityRight) {
		pioneer.getMotors().setVelocity((float) velocityLeft, (float) velocityRight);
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}
}
