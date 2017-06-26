package br.com.pos.unicamp.vrep.behaviors.pathfinding;

import br.com.pos.unicamp.vrep.common.Coordinate;

public class GridUtils {

    public static Coordinate positionToCoordinate(final Position position) {
        final float x = 5.625F - ((float) position.getY()) / 4F;
        final float y = ((float) position.getX()) / 4F - 5.375F;

        return new Coordinate(x,
                              y,
                              0);
    }

    public static Position coordinateToPosition(final Coordinate coordinate) {
        final int x = (int) ((4 * coordinate.getY()) + 22);
        final int y = (int) (23 - (4 * coordinate.getX()));

        return new Position(x,
                            y);
    }
}
