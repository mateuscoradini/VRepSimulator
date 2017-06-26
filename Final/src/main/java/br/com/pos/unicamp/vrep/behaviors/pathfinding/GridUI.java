package br.com.pos.unicamp.vrep.behaviors.pathfinding;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

public class GridUI extends JPanel {

    private final Grid grid;

    public GridUI(final Grid grid) {
        this.grid = grid;
    }

    public void refresh() {
        removeAll();

        setLayout(new GridLayout(getRows(),
                                 getCols()));

        for (int x = 0; x < getCols(); x++) {
            for (int y = 0; y < getRows(); y++) {
                final Position position = new Position(x,
                                                       y);

                if (position.equals(grid.getRobot().getPosition())) {
                    add(makeCell(grid.getRobot()));
                } else {
                    add(makeCell(grid.getCell(position)));
                }
            }
        }

        updateUI();
    }

    private JPanel makeCell(final Node node) {
        return new JPanel() {{
            setBackground(getColor(node));
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    System.out.println(node);
                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }
            });
        }};
    }

    private Color getColor(final Node node) {
        switch (node.getType()) {
            case FREE:
                return new Color(251,
                                 243,
                                 234);
            case WALL:
                return new Color(12,
                                 7,
                                 2);
            case OBSTACLE:
                return new Color(95,
                                 56,
                                 17);
            case PADDING:
                return new Color(239,
                                 203,
                                 167);
            case PLANT:
                return new Color(20,
                                 112,
                                 66);
            case ROBOT:
                return new Color(134,
                                 24,
                                 24);
            case GOAL:
                return new Color(230,
                                 230,
                                 0);
            case PATH:
                return new Color(230,
                                 230,
                                 0);
            case SUB_GOAL:
                return new Color(230,
                                 230,
                                 0);
            default:
                return new Color(251,
                                 243,
                                 234);
        }
    }

    private int getCols() {
        return grid.getCols();
    }

    private int getRows() {
        return grid.getRows();
    }
}

