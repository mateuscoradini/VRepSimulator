package br.com.pos.unicamp.vrep.sensors.ultrasonic;

import br.com.pos.unicamp.vrep.common.Coordinate;
import br.com.pos.unicamp.vrep.exceptions.InvalidResponseException;
import br.com.pos.unicamp.vrep.utils.RemoteAPI;
import coppelia.BoolW;
import coppelia.FloatWA;
import coppelia.IntW;
import coppelia.remoteApi;

public class UltrasonicSensor {

    private static final Float MAX_DETECTION_DISTANCE = 0.5F;

    private static final Float MIN_DETECTION_DISTANCE = 0.3F;

    private final Integer sensorNumber;

    private IntW handle;

    private String sensorName;

    private BoolW detectionState;

    private FloatWA detectedPoint;

    private FloatWA detectedSurfaceNormalVector;

    private IntW detectedObjectHandle;

    public UltrasonicSensor(final int sensorNumber) {
        this.sensorNumber = sensorNumber;
        this.sensorName = "Pioneer_p3dx_ultrasonicSensor" + sensorNumber;

        initialize();
    }

    private void initialize() {
        final IntW handleSensor = new IntW(0);

        getObjectHandleFromRemoteAPI(handleSensor);
        firstRefresh(handleSensor);

        this.handle = handleSensor;
    }

    private void getObjectHandleFromRemoteAPI(final IntW handleSensor) {
        final Integer response = RemoteAPI.getInstance().simxGetObjectHandle(RemoteAPI.getClientId(),
                                                                             sensorName,
                                                                             handleSensor,
                                                                             remoteApi.simx_opmode_blocking);
        if (response != remoteApi.simx_return_ok) {
            throw new InvalidResponseException("The sensor " + sensorName + " could not get the object handle.");
        }
    }

    void refresh() {
        refreshSensorDataWithRemoteAPI(handle,
                                       remoteApi.simx_opmode_buffer);
    }

    private void firstRefresh(final IntW handleSensor) {
        refreshSensorDataWithRemoteAPI(handleSensor,
                                       remoteApi.simx_opmode_streaming);
        simxGetObjectPosition(handleSensor,
                              remoteApi.simx_opmode_streaming);
    }

    public Coordinate getGroundTruthCoordinates() {
        return simxGetObjectPosition(handle,
                                     remoteApi.simx_opmode_buffer);
    }

    private Integer refreshSensorDataWithRemoteAPI(final IntW handle,
                                                   final int mode) {
        final BoolW detectionState = new BoolW(true);
        final FloatWA detectedPoint = new FloatWA(0);
        final IntW detectedObjectHandle = new IntW(0);
        final FloatWA detectedSurfaceNormalVector = new FloatWA(0);

        final Integer response = RemoteAPI.getInstance().simxReadProximitySensor(RemoteAPI.getClientId(),
                                                                                 handle.getValue(),
                                                                                 detectionState,
                                                                                 detectedPoint,
                                                                                 detectedObjectHandle,
                                                                                 detectedSurfaceNormalVector,
                                                                                 mode);

        if (response == remoteApi.simx_return_ok) {
            this.detectionState = detectionState;
            this.detectedPoint = detectedPoint;
            this.detectedSurfaceNormalVector = detectedSurfaceNormalVector;
            this.detectedObjectHandle = detectedObjectHandle;

            return response;
        } else if (response == remoteApi.simx_return_novalue_flag) {
            return response;
        }

        throw new InvalidResponseException("The sensor " + sensorName + " could not be refreshed. Status: " + response);
    }

    private Coordinate simxGetObjectPosition(final IntW handle,
                                             final int mode) {
        final FloatWA position = new FloatWA(0);

        final Integer response = RemoteAPI.getInstance().simxGetObjectPosition(RemoteAPI.getClientId(),
                                                                               handle.getValue(),
                                                                               -1,
                                                                               position,
                                                                               mode);

        if (response == remoteApi.simx_return_ok) {
            return new Coordinate(position.getArray());
        } else if (response == remoteApi.simx_return_novalue_flag) {
            return Coordinate.empty();
        }

        throw new InvalidResponseException("Pioneer could not return its current location. Status: " + response);
    }

    public Boolean isDetected() {
        return detectionState != null && detectionState.getValue();
    }

    public Coordinate detectedCoordinate() {
        if (detectedPoint != null) {
            return new Coordinate(detectedPoint.getArray());
        }

        return Coordinate.empty();
    }

    public int getSensorNumber() {
        return sensorNumber;
    }

    public String getSensorName() {
        return sensorName;
    }
}
