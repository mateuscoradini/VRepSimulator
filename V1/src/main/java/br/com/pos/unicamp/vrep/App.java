package br.com.pos.unicamp.vrep;

import br.com.pos.unicamp.vrep.robots.Pioneer;
import br.com.pos.unicamp.vrep.utils.RemoteAPI;
import org.opencv.core.Core;

public class App {

    static {
        loadOpenCVLibrary();
    }

    public static void main(String[] args) {
        RemoteAPI.session(() -> {
            startPioneer();
        });

//        try {
//            final PioneerRobot robot = new PioneerRobot();
//
//            robot.actuation();
//        } catch (VRepClientException e) {
//            System.out.println(e.getMsg());
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    private static void startPioneer() {
        new Pioneer().start();
    }

    private static void loadOpenCVLibrary() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
}
