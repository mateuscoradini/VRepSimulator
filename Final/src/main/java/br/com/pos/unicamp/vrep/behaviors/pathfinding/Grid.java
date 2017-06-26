package br.com.pos.unicamp.vrep.behaviors.pathfinding;

import java.util.HashSet;
import java.util.Set;

public class Grid {

    private Set<Node> nodes = new HashSet<>();
    private Node robot;
    private int cols;
    private int rows;

    public int getCols() {
        return cols;
    }

    public void setCols(final int cols) {
        this.cols = cols;
    }

    public int getRows() {
        return rows;
    }

    void setRows(final int rows) {
        this.rows = rows;
    }

    public Set<Node> getNodes() {
        return nodes;
    }

    public Node getCell(final Position position) {
        return nodes
                .stream()
                .filter(cell -> cell.getPosition().equals(position))
                .findFirst()
                .orElseGet(() -> {
                    final Node node = new Node(position,
                                               Node.Type.FREE);

                    nodes.add(node);

                    return node;
                });
    }

    public Node getRobot() {
        return robot;
    }

    public void setRobot(final Node robot) {
        this.robot = robot;
    }
}
