package br.com.pos.unicamp.vrep.sensors.image;

import java.rmi.UnexpectedException;
import java.util.Optional;

import br.com.pos.unicamp.vrep.utils.RemoteAPI;
import coppelia.CharWA;
import coppelia.IntW;
import coppelia.IntWA;
import coppelia.remoteApi;
import org.opencv.core.Mat;

import static br.com.pos.unicamp.vrep.utils.LoopUtils.loop;

public class ImageDetector implements Runnable {

    final int BASE_RESOLUTION = 256;
    private final remoteApi vrepApi;
    private final int clientID;
    private IntW kinect;
    private boolean enabled = false;
    private boolean healthyPlantDetected = false;
    private boolean weakPlantDetected = false;

    @Deprecated
    public ImageDetector(final int clientID,
                         final remoteApi vrepApi) {
        this.clientID = clientID;
        this.vrepApi = vrepApi;

        setup();
        turnOn();
    }

    public ImageDetector() {
        this.clientID = RemoteAPI.getClientId();
        this.vrepApi = RemoteAPI.getInstance();

        setup();
        turnOn();
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

    @Override
    public void run() {
        loop(() -> {
                 if (enabled) {
                     final Optional<Mat> matOptional = Optional.ofNullable(getImage());

                     if (matOptional.isPresent()) {
                         final Mat mat = matOptional.get();

                         // TODO - improve this prevalence rule
                         final GreenDetector greenDetector = new GreenDetector(mat);
                         final OrangeDetector orangeDetector = new OrangeDetector(mat);

                         if (greenDetector.getDetectionLevel() > orangeDetector.getDetectionLevel()) {
                             healthyPlantDetected = greenDetector.isDetected();
                             weakPlantDetected = false;
                         } else {
                             healthyPlantDetected = false;
                             weakPlantDetected = orangeDetector.isDetected();
                         }
                     }
                 }
             },
             3000);
    }

    private void sleep(final int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean isWeakPlantDetected() {
        return weakPlantDetected;
    }

    public boolean isHealthyPlantDetected() {
        return healthyPlantDetected;
    }
}
