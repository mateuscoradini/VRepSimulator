package br.com.pos.unicamp.vrep.detectors;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class GreenDetector {

    private String imagePath;

    private boolean greenDetected;

    public GreenDetector(final String path) {
        imagePath = path;

        processGreenDetection();
    }

    private void processGreenDetection() {
        final Mat greenThreshold = greenThreshold();

        greenDetected = nonZeroElements(greenThreshold) > 0;
    }

    public boolean isGreenDetected() {
        return greenDetected;
    }

    private int nonZeroElements(final Mat mat) {
        return Core.countNonZero(mat);
    }

    private Mat applyBlur(final Mat hsvImage) {
        final Mat dst = new Mat();

        Imgproc.medianBlur(hsvImage,
                           dst,
                           5);

        return dst;
    }

    private Mat greenThreshold() {
        final Mat hsvImage = loadHSVImage();
        final Mat greenElements = new Mat();
        final Scalar lower = new Scalar(50,
                                        100,
                                        100);
        final Scalar upper = new Scalar(100,
                                        255,
                                        255); // 70 (more strict)

        applyBlur(hsvImage);

        Core.inRange(hsvImage,
                     lower,
                     upper,
                     greenElements);

        return greenElements;
    }

    private Mat loadHSVImage() {
        final Mat source = Imgcodecs.imread(imagePath);
        final Mat destination = new Mat();

        Imgproc.cvtColor(source,
                         destination,
                         Imgproc.COLOR_BGR2HSV);

        return destination;
    }
}
