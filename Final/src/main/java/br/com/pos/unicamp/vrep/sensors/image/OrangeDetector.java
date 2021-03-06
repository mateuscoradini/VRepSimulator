package br.com.pos.unicamp.vrep.sensors.image;

import org.opencv.core.Mat;
import org.opencv.core.Scalar;

public class OrangeDetector extends ColorDetector {

    public OrangeDetector(final Mat image) {
        super(image);
    }

    public OrangeDetector(final String path) {
        super(path);
    }

    @Override
    public Scalar upperColor() {
        return new Scalar(25,
                          255,
                          255);
    }

    @Override
    public Scalar lowerColor() {
        return new Scalar(5,
                          150,
                          150);
    }
}
