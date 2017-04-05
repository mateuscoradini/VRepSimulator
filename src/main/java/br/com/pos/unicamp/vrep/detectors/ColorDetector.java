package br.com.pos.unicamp.vrep.detectors;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

abstract class ColorDetector {

    private Mat image;

    private boolean colorDetected;

    public ColorDetector(final Mat image) {
        this.image = image;

        processGreenDetection();
    }

    public ColorDetector(final String path) {
        this(Imgcodecs.imread(path));
    }

    private void processGreenDetection() {
        colorDetected = anyElement(threshold());
    }

    public boolean isDetected() {
        return colorDetected;
    }

    private boolean anyElement(final Mat mat) {
        return Core.countNonZero(mat) > 100;
    }

    private Mat applyBlur(final Mat hsvImage) {
        final Mat dst = new Mat();

        Imgproc.medianBlur(hsvImage,
                           dst,
                           5);

        return dst;
    }

    private Mat threshold() {
        final Mat hsvImage = loadHSVImage();
        final Mat colorElements = new Mat();
        final Scalar lower = lowerColor();
        final Scalar upper = upperColor();

        Core.inRange(hsvImage,
                     lower,
                     upper,
                     colorElements);

        applyBlur(colorElements);

        return colorElements;
    }

    abstract Scalar upperColor();

    abstract Scalar lowerColor();

    private Mat loadHSVImage() {
        Imgproc.cvtColor(image,
                         image,
                         Imgproc.COLOR_BGR2HSV);

        return image;
    }
}
