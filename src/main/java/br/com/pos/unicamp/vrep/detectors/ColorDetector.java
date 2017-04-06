package br.com.pos.unicamp.vrep.detectors;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

abstract class ColorDetector {

    private Mat image;

    private boolean colorDetected;
    private int detectionLevel;

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

    public int getDetectionLevel() {
        return detectionLevel;
    }

    protected boolean anyElement(final Mat mat) {
        this.detectionLevel = detectionLevel(mat);
        return detectionLevel > 400;
    }

    protected static void showResult(final Mat img) {
        Imgproc.resize(img,
                       img,
                       new Size(img.width(),
                                img.height()));
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".jpg",
                           img,
                           matOfByte);
        final byte[] byteArray = matOfByte.toArray();
        try {
            InputStream in = new ByteArrayInputStream(byteArray);
            final BufferedImage bufImage = ImageIO.read(in);

            JFrame frame = new JFrame();
            frame.getContentPane().add(new JLabel(new ImageIcon(bufImage)));
            frame.pack();
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected int detectionLevel(final Mat mat) {
        return Core.countNonZero(mat);
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
                         Imgproc.COLOR_BGR2HSV_FULL);

        return image;
    }
}
