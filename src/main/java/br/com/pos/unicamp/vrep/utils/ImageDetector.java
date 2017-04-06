package br.com.pos.unicamp.vrep.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.rmi.UnexpectedException;
import java.util.Optional;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import br.com.pos.unicamp.vrep.detectors.GreenDetector;
import br.com.pos.unicamp.vrep.detectors.OrangeDetector;
import coppelia.CharWA;
import coppelia.IntW;
import coppelia.IntWA;
import coppelia.remoteApi;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class ImageDetector implements Runnable {

    final int BASE_RESOLUTION = 256;
    private final remoteApi vrepApi;
    private final int clientID;
    private IntW kinect;
    private boolean enabled = false;
    private boolean healthyPlantDetected = false;
    private boolean debilitatedDetected = false;

    public ImageDetector(final int clientID,
                         final remoteApi vrepApi) {
        this.clientID = clientID;
        this.vrepApi = vrepApi;

        setup();
        turnOn();
    }

    private static void showResult(final Mat img) {
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

    private void setup() {
        try {
            setupKinect();
        } catch (final UnexpectedException e) {
            System.err.println("Error during the Kinect setup.");
        }

        pingKinect();
    }

    private Mat getImage() {
        final CharWA image = newImage();
        final IntWA resolution = newResolution();
        final int response = vrepApi.simxGetVisionSensorImage(clientID,
                                                              kinect.getValue(),
                                                              resolution,
                                                              image,
                                                              0,
                                                              remoteApi.simx_opmode_buffer);

        if (response == remoteApi.simx_return_ok) {
            final ImageConverter converter = new ImageConverter(resolution,
                                                                image);
            return converter.image();
        } else {
            return null;
        }
    }

    private CharWA newImage() {
        return new CharWA(BASE_RESOLUTION * BASE_RESOLUTION * 3);
    }

    private IntWA newResolution() {
        return new IntWA(2);
    }

    private void pingKinect() {
        final CharWA image = newImage();
        final IntWA resolution = newResolution();

        vrepApi.simxGetVisionSensorImage(clientID,
                                         kinect.getValue(),
                                         resolution,
                                         image,
                                         0,
                                         remoteApi.simx_opmode_streaming);
    }

    private void setupKinect() throws UnexpectedException {
        this.kinect = new IntW(1);

        final int response = vrepApi.simxGetObjectHandle(clientID,
                                                         "kinect_rgb",
                                                         kinect,
                                                         remoteApi.simx_opmode_oneshot_wait);

        if (response == remoteApi.simx_return_ok) {
            System.out.println("Successfully connected to kinect_rgb");
        } else {
            throw new UnexpectedException("The sensor 'kinect_rgb' could not be initialized.");
        }
    }

    public void turnOn() {
        enabled = true;
    }

    public void turnOff() {
        enabled = false;
    }

    private boolean healthyPlantDetected(final Mat mat) {
        final GreenDetector detector = new GreenDetector(mat);

        return detector.isDetected();
    }

    private boolean debilitatedPlantDetected(final Mat mat) {
        final OrangeDetector detector = new OrangeDetector(mat);

        return detector.isDetected();
    }

    @Override
    public void run() {
        while (true) {
            if (enabled) {
                final Optional<Mat> matOptional = Optional.ofNullable(getImage());

                if (matOptional.isPresent()) {
                    final Mat mat = matOptional.get();

                    healthyPlantDetected = healthyPlantDetected(mat);
                    debilitatedDetected = debilitatedPlantDetected(mat);
                }

                sleep(1000);
            }
        }
    }

    private void sleep(final int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean isDebilitatedPlantDetected() {
        return debilitatedDetected;
    }

    public boolean isHealthyPlantDetected() {
        return healthyPlantDetected;
    }
}
