package br.com.pos.unicamp.vrep.sensors.image;

import org.junit.Before;
import org.junit.Test;
import org.opencv.core.Core;

import static org.junit.Assert.*;

public class GreenDetectorTest {

    @Before
    public void setup() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    @Test
    public void testIsGreenDetectedWhenTheImageHasSomethingGreen() throws Exception {
        final String image = resourcePath("green.png");
        final ColorDetector detector = new GreenDetector(image);

        final boolean result = detector.isDetected();

        assertTrue(result);
    }

    @Test
    public void testIsOrangeDetectedWhenTheImageHasSomethingOrange() throws Exception {
        final String image = resourcePath("orange.png");
        final ColorDetector detector = new GreenDetector(image);

        final boolean result = detector.isDetected();

        assertFalse(result);
    }

    @Test
    public void testIsGreenDetectedWhenTheImageDoesNotHaveAnythingGreen() throws Exception {
        final String image = resourcePath("non-green.png");
        final ColorDetector detector = new GreenDetector(image);

        final boolean result = detector.isDetected();

        assertFalse(result);
    }

    private String resourcePath(final String imageFileName) {
        final String basePath = System.getProperty("user.dir");

        return basePath + "/resources/" + imageFileName;
    }
}
