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

public class GreenDetector extends ColorDetector {

    public GreenDetector(final Mat image) {
        super(image);
    }

    public GreenDetector(final String path) {
        super(path);
    }

    @Override
    public Scalar upperColor() {
        return new Scalar(70,
                          255,
                          255);
    }

    @Override
    public Scalar lowerColor() {
        return new Scalar(50,
                          0,
                          0);
    }
}

