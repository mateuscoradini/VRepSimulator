package br.com.pos.unicamp.vrep;

public class App {

	// static {
	// loadOpenCVLibrary();
	// }

	public static void main(String[] args) {

		startPioneer();

		// try {
		// final PioneerRobot robot = new PioneerRobot();
		//
		// robot.actuation();
		// } catch (VRepClientException e) {
		// System.out.println(e.getMsg());
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
	}

	private static void startPioneer() {
		PioneerRobot robot = new PioneerRobot();
		try {
			robot.initializeRobot();

			robot.actuation();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// private static void loadOpenCVLibrary() {
	// System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	// }
}
