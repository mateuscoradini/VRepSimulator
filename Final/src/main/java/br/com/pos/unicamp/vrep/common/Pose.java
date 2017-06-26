package br.com.pos.unicamp.vrep.common;

import static br.com.pos.unicamp.vrep.utils.NumberUtils.toTruncatedFloat;

public class Pose {

    private Coordinate coordinate;

    private float theta;

    public Pose(final Coordinate coordinate,
                final float theta) {
        this.coordinate = coordinate;
        this.theta = theta;
    }

    public static Pose initial() {
        final Coordinate initial = new Coordinate(-4.850440F,
                                                  -0.299997F,
                                                  +0.138679F);
        return new Pose(initial,  0);
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public float getTheta() {
        return theta;
    }

    private float convertToRad(final float value) {
        if (value > 0) {
            return toTruncatedFloat(value);
        }

        return toTruncatedFloat(value + (float) (2 * Math.PI));
    }
}
