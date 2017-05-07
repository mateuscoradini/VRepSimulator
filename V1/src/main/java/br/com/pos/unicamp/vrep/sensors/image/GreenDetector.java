package br.com.pos.unicamp.vrep.sensors.image;

import org.opencv.core.Mat;
import org.opencv.core.Scalar;

public class GreenDetector extends ColorDetector {

    public GreenDetector(final Mat image) {
        super(image);
    }

    public GreenDetector(final String path) {
        super(path);
    }

    @Override
    public Scalar upperColor() {
        return new Scalar(100,
                          220,
                          250);
    }

    @Override
    public Scalar lowerColor() {
        return new Scalar(60,
                          50,
                          50);
    }
}

