package br.com.pos.unicamp.vrep;

import br.com.pos.unicamp.vrep.exceptions.VRepClientException;
import org.opencv.core.Core;

public class App {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {
        try {
            final PioneerRobot robot = new PioneerRobot();

            robot.actuation();
        } catch (VRepClientException e) {
            System.out.println(e.getMsg());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
