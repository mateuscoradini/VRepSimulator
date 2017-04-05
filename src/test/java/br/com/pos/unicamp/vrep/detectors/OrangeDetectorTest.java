package br.com.pos.unicamp.vrep.detectors;

import org.junit.Before;
import org.junit.Test;
import org.opencv.core.Core;

import static org.junit.Assert.*;

public class OrangeDetectorTest {

    @Before
    public void setup() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    @Test
    public void testIsOrangeDetectedWhenTheImageHasSomethingOrange() throws Exception {
        final String image = resourcePath("orange.png");
        final ColorDetector detector = new OrangeDetector(image);

        final boolean result = detector.isDetected();

        assertTrue(result);
    }

    @Test
    public void testIsOrangeDetectedWhenTheImageHasSomethingGreen() throws Exception {
        final String image = resourcePath("green.png");
        final ColorDetector detector = new OrangeDetector(image);

        final boolean result = detector.isDetected();

        assertFalse(result);
    }

    @Test
    public void testIsOrangeDetectedWhenTheImageDoesNotHaveAnythingOrange() throws Exception {
        final String image = resourcePath("non-orange.png");
        final ColorDetector detector = new OrangeDetector(image);

        final boolean result = detector.isDetected();

        assertFalse(result);
    }

    private String resourcePath(final String imageFileName) {
        final String basePath = System.getProperty("user.dir");

        return basePath + "/resources/" + imageFileName;
    }
}
