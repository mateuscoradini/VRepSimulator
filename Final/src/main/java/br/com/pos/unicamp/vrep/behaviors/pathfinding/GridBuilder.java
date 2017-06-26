package br.com.pos.unicamp.vrep.behaviors.pathfinding;

import br.com.pos.unicamp.vrep.common.Coordinate;
import br.com.pos.unicamp.vrep.robots.Pioneer;

import static br.com.pos.unicamp.vrep.behaviors.pathfinding.GridUtils.coordinateToPosition;
import static br.com.pos.unicamp.vrep.utils.LoopUtils.sleep;

public class GridBuilder {

    private final Grid grid;

    public GridBuilder() {
        grid = new Grid();
    }

    public GridBuilder withWalls() {
        baseWalls();
        internalWalls();

        return this;
    }

    private void internalWalls() {
        for (int i = 0; i < numCols(); i++) {
            if (i != numRows() - 5 && i != numRows() - 6 && i != 18 && i != 19) {
                addNode(wall(new Position(numRows() - 19,
                                          i)));
            }
        }

        for (int i = numRows(); i > numRows() - 28; i--) {
            addNode(wall(new Position(i,
                                      29)));
        }
        for (int i = 29; i < 29 + 9; i++) {
            addNode(wall(new Position(25,
                                      i)));
        }
        for (int i = 25; i < 25 + 8; i++) {
            addNode(wall(new Position(i,
                                      38)));
        }

        for (int i = 34; i <= 49; i++) {
            addNode(wall(new Position(17,
                                      i)));
            addNode(wall(new Position(18,
                                      i)));
        }
        for (int i = 3; i <= 16; i++) {
            if (i != 12 && i != 13) {
                addNode(wall(new Position(i,
                                          34)));
                addNode(wall(new Position(i,
                                          35)));
            }
        }
        
        
        addNode(padding(new Position(32, 16)));
        addNode(padding(new Position(32, 17)));
        addNode(padding(new Position(32, 16)));
        
        
        addNode(padding(new Position(34, 14)));
        addNode(padding(new Position(34, 15)));
        addNode(padding(new Position(34, 16)));
        addNode(padding(new Position(34, 17)));
        
        
        addNode(padding(new Position(37, 8)));
        addNode(padding(new Position(37, 9)));
        addNode(padding(new Position(37, 10)));
        addNode(padding(new Position(37, 11)));
        
        addNode(padding(new Position(36, 14)));
        addNode(padding(new Position(37, 15)));
        addNode(padding(new Position(37, 10)));
        addNode(padding(new Position(37, 11)));
        
        addNode(padding(new Position(34, 41)));
        addNode(padding(new Position(34, 42)));
        addNode(padding(new Position(34, 43)));
        addNode(padding(new Position(34, 44)));
        addNode(padding(new Position(34, 44)));
        addNode(padding(new Position(34, 45)));
        addNode(padding(new Position(34, 45)));
        addNode(padding(new Position(34, 45)));
        addNode(padding(new Position(34, 45)));
        addNode(padding(new Position(34, 45)));
        addNode(padding(new Position(34, 43)));
        addNode(padding(new Position(34, 41)));
        addNode(padding(new Position(34, 40)));
        addNode(padding(new Position(34, 40)));
        addNode(padding(new Position(34, 39)));
        addNode(padding(new Position(34, 43)));
        addNode(padding(new Position(34, 44)));
        
        addNode(padding(new Position(35, 14)));
        addNode(padding(new Position(35, 15)));
        addNode(padding(new Position(35, 16)));
        addNode(padding(new Position(35, 17)));
        addNode(padding(new Position(36, 16)));
        addNode(padding(new Position(37, 14)));
        addNode(padding(new Position(37, 13)));
        addNode(padding(new Position(37, 12)));
        addNode(padding(new Position(37, 16)));
        
        addNode(padding(new Position(19, 34)));
        addNode(padding(new Position(19, 33)));
        addNode(padding(new Position(19, 32)));
        
        addNode(padding(new Position(24, 39)));
        addNode(padding(new Position(25, 39)));
        addNode(padding(new Position(26, 39)));
        addNode(padding(new Position(27, 39)));
        addNode(padding(new Position(28, 39)));
        addNode(padding(new Position(19, 32)));
        
        addNode(padding(new Position(32, 40)));
        addNode(padding(new Position(32, 41)));
        addNode(padding(new Position(32, 42)));
        addNode(padding(new Position(32, 43)));
        addNode(padding(new Position(32, 44)));
        addNode(padding(new Position(30, 47)));
        addNode(padding(new Position(31, 47)));
        
//        addNode(plant(new Position(12, 28)));
//        addNode(plant(new Position(12, 29)));
        
        
        
        //Plant Correction
        addNode(plant(new Position(20, 30)));
        addNode(plant(new Position(19, 29)));
        addNode(plant(new Position(20, 29)));
        
        addNode(plant(new Position(18, 21)));
        addNode(plant(new Position(18, 22)));
        
        
        
        
        addNode(padding(new Position(21, 45)));
        addNode(padding(new Position(20, 45)));
        
        addNode(padding(new Position(19, 31)));
        addNode(padding(new Position(19, 28)));
        addNode(padding(new Position(24, 26)));
        addNode(padding(new Position(24, 27)));
        
        addNode(padding(new Position(24, 26)));
        addNode(padding(new Position(24, 27)));
        addNode(padding(new Position(17, 21)));
        addNode(padding(new Position(17, 22)));
        
        addNode(padding(new Position(12, 28)));
        addNode(padding(new Position(12, 29)));
        
        addNode(padding(new Position(35, 39)));
        addNode(padding(new Position(35, 40)));
        addNode(padding(new Position(35, 41)));
        addNode(padding(new Position(35, 42)));
        addNode(padding(new Position(35, 43)));
        addNode(padding(new Position(35, 44)));
        addNode(padding(new Position(35, 45)));
        
        
        addNode(padding(new Position(18, 20)));
        addNode(padding(new Position(19, 20)));
        addNode(padding(new Position(17, 20)));
        
        
        
        
        addNode(padding(new Position(36, 15)));
        addNode(padding(new Position(36, 17)));
        addNode(padding(new Position(19, 28)));
        
        addNode(padding(new Position(37, 17)));
        
        
        addNode(padding(new Position(34, 20)));
        
        addNode(padding(new Position(33, 19)));

        addNode(padding(new Position(24, 38)));
        addNode(padding(new Position(24,
                                     37)));
        addNode(padding(new Position(24,
                                     36)));
        addNode(padding(new Position(24,
                                     35)));
        addNode(padding(new Position(24,
                                     34)));
        addNode(padding(new Position(24,
                                     33)));
        addNode(padding(new Position(24,
                                     32)));
        addNode(padding(new Position(24,
                                     31)));
        addNode(padding(new Position(24,
                                     30)));
        addNode(padding(new Position(24,
                                     29)));
    }

    public GridBuilder withObstacles() {
        whiteBookcase();
        bigCloset();
        smallCloset();
        mediumCloset();
        armchairA();
        armchairB();
        doorA();
        doorB();
        doorC();
        chairA();
        chairB();
        chairC();
        chairD();
        chairE();
        chairF();
        smallTableA();
        smallTableB();
        mediumTable();
        bigTableA();
        bigTableB();

        return this;
    }

    private void bigTableB() {
        for (int i = 13; i > 7; i--) {
            for (int j = 47; j > 38; j--) {
                addNode(obstacle(new Position(i,
                                              j)));
            }
        }
    }

    private void chairF() {
        addNode(obstacle(new Position(7,
                                      40)));
        addNode(obstacle(new Position(7,
                                      41)));
        addNode(obstacle(new Position(7,
                                      42)));
    }

    private void chairE() {
        addNode(obstacle(new Position(7,
                                      46)));
        addNode(obstacle(new Position(6,
                                      46)));
        addNode(obstacle(new Position(6,
                                      45)));
        addNode(obstacle(new Position(6,
                                      44)));
        addNode(obstacle(new Position(7,
                                      44)));
        addNode(obstacle(new Position(7,
                                      45)));
    }

    private void chairD() {
        addNode(obstacle(new Position(14,
                                      44)));
        addNode(obstacle(new Position(14,
                                      43)));
        addNode(obstacle(new Position(15,
                                      43)));
        addNode(obstacle(new Position(15,
                                      44)));
    }

    private void chairC() {
        addNode(obstacle(new Position(45,
                                      35)));
        addNode(obstacle(new Position(45,
                                      34)));
        addNode(obstacle(new Position(44,
                                      34)));
        addNode(obstacle(new Position(44,
                                      35)));
    }

    private void smallTableB() {
        for (int i = 48; i > 40; i--) {
            for (int j = 39; j >= 36; j--) {
                addNode(obstacle(new Position(i,
                                              j)));
            }
        }
    }

    private void mediumTable() {
        for (int i = 47; i > 42; i--) {
            for (int j = 48; j > 41; j--) {
                addNode(obstacle(new Position(i,
                                              j)));
            }
        }
    }

    private void smallTableA() {
        for (int i = 34; i <= 36; i++) {
            for (int j = 8; j <= 13; j++) {
                addNode(obstacle(new Position(i,
                                              j)));
            }
        }
    }

    private void chairB() {
        addNode(obstacle(new Position(45,
                                      12)));
        addNode(obstacle(new Position(45,
                                      13)));
        addNode(obstacle(new Position(44,
                                      13)));
        addNode(obstacle(new Position(44,
                                      12)));
    }

    private void chairA() {
        addNode(obstacle(new Position(43,
                                      24)));
        addNode(obstacle(new Position(43,
                                      25)));
        addNode(obstacle(new Position(44,
                                      25)));
        addNode(obstacle(new Position(44,
                                      24)));
    }

    private void bigTableA() {
        for (int i = 48; i >= 41; i--) {
            for (int j = 14; j <= 23; j++) {
                addNode(obstacle(new Position(i,
                                              j)));
            }
        }
    }

    public GridBuilder withPlants() {
        plantA();
        plantB();
        plantC();
        plantD();
        plantE();
        plantF();
        //plantG();
        plantH();
        plantI();

        return this;
    }

    private void plantI() {
        addNode(plant(new Position(37,
                                   31)));
        addNode(plant(new Position(36,
                                   31)));
        addNode(plant(new Position(37,
                                   32)));
        addNode(plant(new Position(36,
                                   32)));
    }

    private void plantH() {
        addNode(plant(new Position(35,
                                   5)));
        addNode(plant(new Position(34,
                                   5)));
        addNode(plant(new Position(34,
                                   6)));
        addNode(plant(new Position(35,
                                   6)));
    }

//    private void plantG() {
//        addNode(plant(new Position(31,
//                                   3)));
//        addNode(plant(new Position(31,
//                                   4)));
//        addNode(plant(new Position(32,
//                                   4)));
//        addNode(plant(new Position(32,
//                                   3)));
//    }

    private void plantF() {
        addNode(padding(new Position(26,
                                     25)));
        addNode(padding(new Position(25,
                                     25)));
        addNode(plant(new Position(25,
                                   26)));
        addNode(plant(new Position(25,
                                   27)));
        addNode(padding(new Position(25,
                                     28)));
        addNode(padding(new Position(26,
                                     28)));
        addNode(plant(new Position(26,
                                   27)));
        ;
        addNode(plant(new Position(26,
                                   26)));
    }

    private void plantE() {
        addNode(plant(new Position(19,
                                   21)));
        addNode(plant(new Position(19,
                                   22)));
    }

    private void plantD() {
        addNode(plant(new Position(9,
                                   22)));
        addNode(plant(new Position(9,
                                   23)));
        addNode(plant(new Position(10,
                                   23)));
        addNode(plant(new Position(10,
                                   22)));
    }

    private void plantC() {
        addNode(plant(new Position(10,
                                   28)));
        addNode(plant(new Position(10,
                                   29)));
        addNode(plant(new Position(11,
                                   28)));
        addNode(plant(new Position(11,
                                   29)));
    }

    private void plantB() {
        addNode(plant(new Position(19,
                                   31)));
        addNode(plant(new Position(19,
                                   30)));
    }

    private void plantA() {
        addNode(plant(new Position(20,
                                   46)));
        addNode(plant(new Position(20,
                                   47)));
        addNode(plant(new Position(21,
                                   46)));
        addNode(plant(new Position(21,
                                   47)));
    }

    private void doorA() {
        addNode(padding(new Position(28,
                                     19)));
        addNode(padding(new Position(29,
                                     19)));
        addNode(padding(new Position(30,
                                     19)));
        addNode(padding(new Position(31,
                                     19)));
        addNode(padding(new Position(32,
                                     19)));
        addNode(padding(new Position(28,
                                     20)));
        addNode(padding(new Position(29,
                                     20)));
        addNode(obstacle(new Position(30,
                                      20)));
        addNode(obstacle(new Position(31,
                                      20)));
        addNode(obstacle(new Position(32,
                                      20)));
    }

    private void doorB() {
        addNode(obstacle(new Position(30,
                                      48)));
        addNode(obstacle(new Position(31,
                                      48)));
        addNode(obstacle(new Position(32,
                                      48)));
    }

    private void doorC() {
        addNode(obstacle(new Position(11,
                                      33)));
        addNode(obstacle(new Position(11,
                                      32)));
        addNode(obstacle(new Position(11,
                                      31)));
    }

    private void armchairB() {
        for (int i = 4; i <= 8; i++) {
            for (int j = 12; j <= 16; j++) {
                addNode(obstacle(new Position(i,
                                              j)));
            }
        }
    }

    private void armchairA() {
        for (int i = 9; i <= 13; i++) {
            for (int j = 5; j <= 8; j++) {
                addNode(obstacle(new Position(i,
                                              j)));
            }
        }
    }

    private void mediumCloset() {
        for (int i = 33; i <= 38; i++) {
            addNode(obstacle(new Position(34,
                                          i)));
            addNode(obstacle(new Position(35,
                                          i)));
            addNode(obstacle(new Position(36,
                                          i)));
        }
    }

    private void smallCloset() {
        for (int i = 22; i <= 27; i++) {
            addNode(obstacle(new Position(31,
                                          i)));
            addNode(obstacle(new Position(32,
                                          i)));
        }
    }

    private void bigCloset() {
        for (int i = 30; i > 14; i--) {
            addNode(obstacle(new Position(i,
                                          2)));
            addNode(obstacle(new Position(i,
                                          3)));
            addNode(obstacle(new Position(i,
                                          4)));
        }
        addNode(obstacle(new Position(27,
                                      5)));
        addNode(obstacle(new Position(27,
                                      6)));
        addNode(obstacle(new Position(26,
                                      6)));
        addNode(obstacle(new Position(26,
                                      5)));
        addNode(obstacle(new Position(25,
                                      5)));
    }

    private void whiteBookcase() {
        for (int i = 5; i <= 15; i++) {
            addNode(obstacle(new Position(numRows() - i,
                                          2)));
            addNode(obstacle(new Position(numRows() - i,
                                          3)));
        }
    }

    private void baseWalls() {
        for (int i = 0; i < numCols(); i++) {
            addNode(wall(new Position(0,
                                      i)));
            addNode(wall(new Position(1,
                                      i)));
            addNode(wall(new Position(numRows() - 1,
                                      i)));
            addNode(wall(new Position(numRows() - 2,
                                      i)));
            addNode(wall(new Position(numRows() - 3,
                                      i)));
        }

        for (int i = 0; i < numRows(); i++) {
            addNode(wall(new Position(i,
                                      0)));
            addNode(wall(new Position(i,
                                      1)));
            addNode(wall(new Position(i,
                                      numCols() - 1)));
            addNode(wall(new Position(i,
                                      numCols() - 2)));
        }
    }

    public Grid getGrid() {
        return grid;
    }

    private int numCols() {
        return grid.getCols();
    }

    private int numRows() {
        return grid.getRows();
    }

    private Node wall(final Position position) {
        return new Node(position,
                        Node.Type.WALL);
    }

    private Node obstacle(final Position position) {
        return new Node(position,
                        Node.Type.OBSTACLE);
    }

    private Node plant(final Position position) {
        return new Node(position,
                        Node.Type.PLANT);
    }

    private Node padding(final Position position) {
        return new Node(position,
                        Node.Type.PADDING);
    }

    private void addNode(final Node e) {
        grid.getNodes().add(e);
    }

    public GridBuilder withCols(final int cols) {
        grid.setCols(cols);

        return this;
    }

    public GridBuilder withRows(final int rows) {
        grid.setRows(rows);

        return this;
    }

    public GridBuilder withRobot(final Pioneer pioneer) {
        // TODO:
        // Fix this. We should not need a delay to get a valid GroundTruth value.
        // (it's needed only at the first call.
        sleep(1000);

        //final Coordinate robotCoordinate = pioneer.getGroundTruth().getCoordinate();
        final Coordinate robotCoordinate = pioneer.evaluatedPosition().getCoordinate();
        final Position position = coordinateToPosition(robotCoordinate);

        grid.setRobot(new Node(position,
                               Node.Type.ROBOT) {{
            setF(0);
            setG(0);
            setH(0);
        }});

        return this;
    }

    public GridBuilder withGoal(final Node goal) {
        grid.getNodes().add(goal);

        return this;
    }
}
