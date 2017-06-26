package br.com.pos.unicamp.vrep.behaviors;

import java.awt.Dimension;
import java.util.List;
import javax.swing.JFrame;

import br.com.pos.unicamp.vrep.behaviors.pathfinding.AStar;
import br.com.pos.unicamp.vrep.behaviors.pathfinding.Grid;
import br.com.pos.unicamp.vrep.behaviors.pathfinding.GridBuilder;
import br.com.pos.unicamp.vrep.behaviors.pathfinding.GridUI;
import br.com.pos.unicamp.vrep.behaviors.pathfinding.Node;
import br.com.pos.unicamp.vrep.behaviors.pathfinding.Position;
import br.com.pos.unicamp.vrep.common.Coordinate;
import br.com.pos.unicamp.vrep.robots.Pioneer;

import static br.com.pos.unicamp.vrep.behaviors.pathfinding.GridUtils.coordinateToPosition;
import static br.com.pos.unicamp.vrep.utils.LoopUtils.sleep;

public class PathPlanner {

    private static final int COLS = 52;

    private static final int ROWS = 52;

    private final Grid grid;

    private final GridUI gridUI;

    private final Pioneer pioneer;

    private final List<Node> path;

    public PathPlanner(final Pioneer pioneer,
                       final int goalX,
                       final int goalY) {

        final Node goal = makeGoal(goalX,
                                   goalY);

        this.pioneer = pioneer;
        this.grid = makeGrid(pioneer,
                             goal);
        this.gridUI = makeGridUI();
        this.path = findPath();

        updateRobotPosition();

        showPopUp();
    }

    private void showPopUp() {
        new JFrame() {{
            setTitle("GridUI");
            setContentPane(gridUI);
            setVisible(true);
            setSize(new Dimension(800,
                                  800));
        }};

        gridUI.refresh();
    }

    private void updateRobotPosition() {
        new Thread(() -> {
            while (true) {
                final Node robot = grid.getRobot();
                //final Coordinate coordinate = pioneer.getGroundTruth().getCoordinate();
                final Coordinate coordinate = pioneer.getGroundTruth().getCoordinate();
                final Position position = coordinateToPosition(coordinate);

                robot.setType(Node.Type.ROBOT);
                robot.setPosition(position);

                gridUI.refresh();

                sleep(500);
            }
        }).start();
    }

    public List<Node> getPath() {
        return path;
    }

    private List<Node> findPath() {
        final AStar aStar = new AStar(grid);
        final List<Node> nodes = aStar.find();

        nodes.forEach(n -> n.setType(Node.Type.SUB_GOAL));

        return nodes;
    }

    private GridUI makeGridUI() {
        final GridUI gridUI = new GridUI(grid);

        gridUI.refresh();

        return gridUI;
    }

    private Grid makeGrid(final Pioneer pioneer,
                          final Node goal) {
        return new GridBuilder()
                .withCols(COLS)
                .withRows(ROWS)
                .withWalls()
                .withObstacles()
                .withPlants()
                .withRobot(pioneer)
                .withGoal(goal)
                .getGrid();
    }

    private Node makeGoal(final int goalX,
                          final int goalY) {
        return new Node(new Position(goalX,
                                     goalY),
                        Node.Type.GOAL);
    }

	public Grid getGrid() {
		return grid;
	}

	public Pioneer getPioneer() {
		return pioneer;
	}
}
