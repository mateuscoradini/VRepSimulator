package br.com.pos.unicamp.vrep.sensors.image;

import coppelia.CharWA;
import coppelia.IntWA;
import org.opencv.core.Mat;

import static org.opencv.core.CvType.CV_8UC3;

public class ImageConverter {

    private final IntWA resolution;
    private final CharWA image;

    public ImageConverter(final IntWA resolution,
                          final CharWA image) {
        this.resolution = resolution;
        this.image = image;
    }

    public Mat image() {
        final char[] array = getImageArray();
        final int rows = getRows();
        final int cols = getCols();

        final Mat mat = new Mat(rows,
                                cols,
                                CV_8UC3);

        for (int i = 0; i < rows; i++) {
            final int row = rows - i;
            final int firstRowPixel = i * cols * 3;

            for (int col = 0; col < cols; col++) {
                final int pixel = firstRowPixel + col * 3;
                final char r = array[pixel];
                final char g = array[pixel + 1];
                final char b = array[pixel + 2];

                mat.put(row,
                        col,
                        r,
                        g,
                        b);
            }
        }

        return mat;
    }

    private char[] getImageArray() {
        return image.getArray();
    }

    private int getRows() {
        return resolution.getArray()[1];
    }

    private int getCols() {
        return resolution.getArray()[0];
    }
}
