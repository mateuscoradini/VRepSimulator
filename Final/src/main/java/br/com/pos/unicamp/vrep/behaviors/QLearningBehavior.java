package br.com.pos.unicamp.vrep.behaviors;

import br.com.pos.unicamp.vrep.behaviors.pathfinding.Grid;
import br.com.pos.unicamp.vrep.behaviors.pathfinding.Node;
import br.com.pos.unicamp.vrep.behaviors.pathfinding.Position;
import br.com.pos.unicamp.vrep.common.Coordinate;
import br.com.pos.unicamp.vrep.common.Pose;
import br.com.pos.unicamp.vrep.robots.Pioneer;

public class QLearningBehavior implements Behavior {

	private Coordinate destination;

	private final double alpha = 0.1; // Learning rate
	private final double gamma = 0.9; // Eagerness - 0 looks in the near future,
										// 1 looks in the distant future

	private int statesCount = 0;

	private final int reward = 100;
	private final int rewardMin = 1;
	private final int penalty = -10;

	// private char[][] maze; // Maze read from file
	private int[][] R; // Reward lookup
	private double[][] Q; // Q learning

	private Pioneer pioneer;
	
	private Grid grid;

	@Override
	public void initialize(final Pioneer pioneer) {
		this.pioneer = pioneer;

		PathPlanner finalPathPlanner = new PathPlanner(pioneer, 38, 6);
		Grid grid = finalPathPlanner.getGrid();

		statesCount = grid.getCols() * grid.getRows();
		R = new int[statesCount][statesCount];
		Q = new double[statesCount][statesCount];
		init(grid);

	}

//	void calculateQ() {
//		Random rand = new Random();
//
//		for (int i = 0; i < 1000; i++) { // Train cycles
//			// Select random initial state
//			
//			
//			Node crtState = new Node(grid.getRobot().getPosition(), Node.Type.FREE);
//
//			while (!isFinalState(crtState.getType())) {
//				int[] actionsFromCurrentState = possibleActionsFromState(crtState);
//
//				// Pick a random action from the ones possible
//				int index = rand.nextInt(actionsFromCurrentState.length);
//				int nextState = actionsFromCurrentState[index];
//
//				// Q(state,action)= Q(state,action) + alpha * (R(state,action) +
//				// gamma * Max(next state, all actions) - Q(state,action))
//				double q = Q[crtState][nextState];
//				double maxQ = maxQ(nextState);
//				int r = R[crtState][nextState];
//
//				double value = q + alpha * (r + gamma * maxQ - q);
//				Q[crtState][nextState] = value;
//
//				crtState = nextState;
//			}
//		}
//	}
//	
//	private void possibleActionsFromState(Node.Type type) {
//		ArrayList<Integer> result = new ArrayList<Integer>();
//		for (int i = 0; i < statesCount; i++) {
//			if (R[state][i] != -1) {
//				result.add(i);
//			}
//		}
//
//		return result.stream().mapToInt(i -> i).toArray();
//	}

	boolean isFinalState(Node.Type type) {
		if(!type.equals(Node.Type.FREE) || !type.equals(Node.Type.GOAL)){
			return true;
		}
		return false;
	}

	public void init(Grid grid) {
		int i = 0;
		int j = 0;
		// We will navigate through the reward matrix R using k index
		for (int k = 0; k < statesCount; k++) {

			// We will navigate with i and j through the maze, so we need
			// to translate k into i and j
			i = k / grid.getCols();
			j = k - i * grid.getCols();

			// Fill in the reward matrix with -1
			for (int s = 0; s < statesCount; s++) {
				R[k][s] = -1;
			}

			// If not in final state or a wall try moving in all directions in
			// the maze
			Node n = grid.getCell(new Position(i, j));
			if (n.getType().equals(Node.Type.FREE)) {

				// Try to move left in the maze
				int goLeft = j - 1;
				if (goLeft >= 0) {
					int target = i * grid.getCols() + goLeft;
					Node nAux = grid.getCell(new Position(i, goLeft));
					if (nAux.getType().equals(Node.Type.GOAL)) {
						R[k][target] = reward;
					} else {
						if (nAux.getType().equals(Node.Type.FREE)) {
							R[k][target] = rewardMin;
						} else {
							R[k][target] = penalty;
						}
					}

				}

				// Try to move right in the maze
				int goRight = j + 1;
				if (goRight < grid.getCols()) {
					int target = i * grid.getCols() + goRight;
					Node nAux = grid.getCell(new Position(i, goRight));
					if (nAux.getType().equals(Node.Type.GOAL)) {
						R[k][target] = reward;
					} else {
						if (nAux.getType().equals(Node.Type.FREE)) {
							R[k][target] = rewardMin;
						} else {
							R[k][target] = penalty;
						}
					}
				}

				// Try to move up in the maze
				int goUp = i - 1;
				if (goUp < grid.getCols()) {
					int target = goUp * grid.getCols() + j;
					Node nAux = grid.getCell(new Position(i, goUp));
					if (nAux.getType().equals(Node.Type.GOAL)) {
						R[k][target] = reward;
					} else {
						if (nAux.getType().equals(Node.Type.FREE)) {
							R[k][target] = rewardMin;
						} else {
							R[k][target] = penalty;
						}
					}

				}

				// Try to move down in the maze
				int goDown = i + 1;
				if (goDown < grid.getCols()) {
					int target = goDown * grid.getCols() + j;
					Node nAux = grid.getCell(new Position(i, goDown));
					if (nAux.getType().equals(Node.Type.GOAL)) {
						R[k][target] = reward;
					} else {
						if (nAux.getType().equals(Node.Type.FREE)) {
							R[k][target] = rewardMin;
						} else {
							R[k][target] = penalty;
						}
					}

				}
			}
		}
	}

	private boolean isFound(final Pose groundTruth) {
		if (distanceToGoal(groundTruth) < 0.5) {

			return true;
		}
		return false;
	}

	private double distanceToGoal(Pose groundTruth) {
		float deltaX = destination.getX() - groundTruth.getCoordinate().getX();
		float deltaY = destination.getY() - groundTruth.getCoordinate().getY();

		return Math.sqrt((deltaX * deltaX) + (deltaY * deltaY));
	}

	@Override
	public void keepGoing() {


		//TODO Refactoring all classe to set new states e actions
//		SimpleAvoidObstacle simpleAvoidObstacle = new SimpleAvoidObstacle();
//		simpleAvoidObstacle.initialize(pioneer);
//		SimpleWallFollower simpleWallFollower = new SimpleWallFollower();
//		simpleWallFollower.initialize(pioneer);

//		final Double obstacleLevel = simpleAvoidObstacle.obstacleLevel();
//		final double leftVelocity;
//		final double rightVelocity;
		

	}

	@Override
	public boolean isComplete() {
		return false;
	}

}
