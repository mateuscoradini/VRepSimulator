package br.com.coradini.robot.handlers;

import br.com.coradini.robot.constants.Constants;
import br.com.coradini.robot.exceptions.InvalidResponseException;
import coppelia.BoolW;
import coppelia.FloatW;
import coppelia.FloatWA;
import coppelia.IntW;
import coppelia.remoteApi;

public class VREPHandler {

	private static remoteApi VREP_API;
	private int clientID;
	private String host;
	private int port;
	private boolean isConnected;

	public VREPHandler() {
		VREP_API = new remoteApi();
		host = Constants.VREP_REMOTE_HOST;
		port = Constants.VREP_REMOTE_PORT;
	}

	public void connect() {
		closeAllOpenedConnections();
		clientID = VREP_API.simxStart(host, port, true, true, 2000, 5);
	}

	public HandleObject getHandle(String name) {
		IntW intW = new IntW(0);
		VREP_API.simxGetObjectHandle(clientID, name, intW, VREP_API.simx_opmode_oneshot_wait);

		HandleObject handleObject = new HandleObject();
		handleObject.setName(name);
		handleObject.setHandleSensorID(intW);
		return handleObject;
	}

	public UltrassonicHandler readProximitySensor(HandleObject handleObject) {
		UltrassonicHandler ultrassonicHandler = new UltrassonicHandler();
		BoolW detectionState = new BoolW(false);
		FloatWA detectedPoint = new FloatWA(0);
		IntW detectedObjectHandle = new IntW(0);
		FloatWA detectedSurfaceNormalVector = new FloatWA(0);

		final Integer response = VREP_API.simxReadProximitySensor(clientID, handleObject.getHandleSensorID().getValue(), detectionState, detectedPoint, detectedObjectHandle, detectedSurfaceNormalVector, remoteApi.simx_opmode_oneshot_wait);
		if (!(response == remoteApi.simx_return_ok || response == remoteApi.simx_return_novalue_flag)) {
			throw new InvalidResponseException("The sensor " + handleObject.getName() + " could not be refreshed.");
		}
		ultrassonicHandler.setDetectedPoint(detectedPoint);
		ultrassonicHandler.setDetectedSurfaceNormalVector(detectedSurfaceNormalVector);
		ultrassonicHandler.setDetectionState(detectionState);
		ultrassonicHandler.setDetectedObjectHandle(detectedObjectHandle);
		return ultrassonicHandler;
	}

	public FloatW getJointPosition(IntW handleSensorID) {
		FloatW position = new FloatW(0f);
		VREP_API.simxGetJointPosition(clientID, handleSensorID.getValue(), position, remoteApi.simx_opmode_blocking);
		return position;
	}

	public FloatWA getObjectPostion(HandleObject handleObject) {
		int relativeToObjectHandle = 0;
		FloatWA position = new FloatWA(0);
		final Integer response = VREP_API.simxGetObjectPosition(clientID, handleObject.getHandleSensorID().getValue(), relativeToObjectHandle, position, remoteApi.simx_opmode_blocking);
		return position;
	}

	public FloatWA getObjectOrientation(HandleObject handleObject) {
		int relativeToObjectHandle = 0;
		FloatWA position = new FloatWA(0);
		final Integer response = VREP_API.simxGetObjectOrientation(clientID, handleObject.getHandleSensorID().getValue(), relativeToObjectHandle, position, remoteApi.simx_opmode_blocking);
		return position;
	}

	public int setJointTargetVelocity(HandleObject handleObj, float targetVelocity) {
		return VREP_API.simxSetJointTargetVelocity(clientID, handleObj.getHandleSensorID().getValue(), targetVelocity, remoteApi.simx_opmode_blocking);
	}

	/**
	 * Close all connections necessary
	 */
	private void closeAllOpenedConnections() {
		closeConnection(-1);
	}

	private void closeConnection(final int clientID) {
		VREP_API.simxFinish(clientID);
	}

	public static remoteApi getVrepApi() {
		return VREP_API;
	}

	public static void setVrepApi(remoteApi vrepApi) {
		VREP_API = vrepApi;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public boolean isConnected() {
		return isConnected;
	}

	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}

	public int getClientID() {
		return clientID;
	}

	public void setClientID(int clientID) {
		this.clientID = clientID;
	}

}
