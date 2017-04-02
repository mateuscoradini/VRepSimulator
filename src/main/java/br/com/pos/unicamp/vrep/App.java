package br.com.pos.unicamp.vrep;

import br.com.pos.unicamp.vrep.exceptions.VRepClientException;

public class App {

    public static void main(String[] args) {
        try {
            final PioneerRobot robot = new PioneerRobot();

            robot.actuation();
        } catch (VRepClientException e) {
            System.out.println(e.getMsg());
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
