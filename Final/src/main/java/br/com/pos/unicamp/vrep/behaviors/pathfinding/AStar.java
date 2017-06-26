package br.com.pos.unicamp.vrep.behaviors.pathfinding;

import java.util.ArrayList;
import java.util.List;

public class AStar {

    private final Grid grid;
    private Node source;
    private Node destination;

    public AStar(final Grid grid) {
        this.grid = grid;
    }

    public List<Node> find() {
        this.source = getSource();
        this.destination = makeDestination();

        if (isNotValid(source,
                       destination)) {
            return new ArrayList<>();
        }

        final List<Node> closedList = new ArrayList<>();
        final List<Node> openedList = new ArrayList<>();

        openedList.add(source);

        while (!openedList.isEmpty()) {
            final Node currentNode = openedList.get(0);

            closedList.add(currentNode);
            openedList.remove(currentNode);

            Node n;
            double cost;

            n = grid.getCell(new Position(currentNode.getPosition().getX() - 1,
                                          currentNode.getPosition().getY()));
            cost = 1;
            if (processNeighbor(closedList,
                                openedList,
                                currentNode,
                                n,
                                cost)) {
                return calculatePath(n);
            }

            n = grid.getCell(new Position(currentNode.getPosition().getX() + 1,
                                          currentNode.getPosition().getY()));
            cost = 1;
            if (processNeighbor(closedList,
                                openedList,
                                currentNode,
                                n,
                                cost)) {
                return calculatePath(n);
            }

            n = grid.getCell(new Position(currentNode.getPosition().getX(),
                                          currentNode.getPosition().getY() + 1));
            cost = 1;
            if (processNeighbor(closedList,
                                openedList,
                                currentNode,
                                n,
                                cost)) {
                return calculatePath(n);
            }

            n = grid.getCell(new Position(currentNode.getPosition().getX(),
                                          currentNode.getPosition().getY() - 1));
            cost = 1;
            if (processNeighbor(closedList,
                                openedList,
                                currentNode,
                                n,
                                cost)) {
                return calculatePath(n);
            }
        }

        return new ArrayList<>();
    }

    private boolean processNeighbor(final List<Node> closedList,
                                    final List<Node> openedList,
                                    final Node currentNode,
                                    final Node node,
                                    final double cost) {
        if (isValid(node)) {
            if (isDestination(node)) {
                node.setParent(currentNode);
                return true;
            }

            if (!closedList.contains(node) && isUnBlocked(node)) {
                double gNew = currentNode.getG() + cost;
                double hNew = calculateHValue(node);
                double fNew = gNew + hNew;

                if (node.getF() == Double.MAX_VALUE || node.getF() > fNew) {
                    openedList.remove(node);
                    openedList.add(node);

                    node.setF(fNew);
                    node.setG(gNew);
                    node.setH(hNew);
                    node.setParent(currentNode);
                }
            }
        }
        return false;
    }

    private boolean isNotValid(final Node source,
                               final Node destination) {
        return !isValid(source) || !isValid(destination) || source.equals(destination);
    }

    private List<Node> calculatePath(Node destination) {
        final ArrayList<Node> path = new ArrayList<>();

        path.add(destination);

        while (destination.hasParent()) {
            Node parent = destination.getParent();
            path.add(parent);

            destination = parent;
        }

        return path;
    }

    private double calculateHValue(final Node node) {
        final Position destPosition = destination.getPosition();
        final int xDest = destPosition.getX();
        final int yDest = destPosition.getY();
        final int x = node.getPosition().getX();
        final int y = node.getPosition().getY();

        return Math.sqrt(((x - xDest) * (x - xDest) + (y - yDest) * (y - yDest)));
    }

    private boolean isDestination(final Node node) {
        return node.getType().equals(Node.Type.GOAL);
    }

    private boolean isUnBlocked(final Node node) {
        switch (node.getType()) {
            case FREE:
                return true;
            case WALL:
                return false;
            case OBSTACLE:
                return false;
            case PADDING:
                return false;
            case PLANT:
                return false;
            case ROBOT:
                return false;
            case GOAL:
                return true;
            default:
                return false;
        }
    }

    private boolean isValid(final Node node) {
        final Position position = node.getPosition();
        final int x = position.getX();
        final int y = position.getY();

        return x >= 0 && x < grid.getRows() && y >= 0 && y < grid.getCols();
    }

    private Node getSource() {
        return grid.getRobot();
    }

    private Node makeDestination() {
        return grid
                .getNodes()
                .stream()
                .filter(n -> n.getType() == Node.Type.GOAL)
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }
}
