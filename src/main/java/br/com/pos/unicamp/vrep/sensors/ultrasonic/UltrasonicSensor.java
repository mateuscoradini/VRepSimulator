package br.com.pos.unicamp.vrep.sensors.ultrasonic;

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

        this.handle = handleSensor;
    }

    private void getObjectHandleFromRemoteAPI(final IntW handleSensor) {
        final Integer response = RemoteAPI.getInstance().simxGetObjectHandle(RemoteAPI.getClientId(),
                                                                             sensorName,
                                                                             handleSensor,
                                                                             remoteApi.simx_opmode_oneshot_wait);
        if (response != remoteApi.simx_return_ok) {
            throw new InvalidResponseException("The sensor " + sensorName + " could not get the object handle.");
        }
    }

    void refresh() {
        final BoolW detectionState = new BoolW(true);
        final FloatWA detectedPoint = new FloatWA(0);
        final IntW detectedObjectHandle = new IntW(0);
        final FloatWA detectedSurfaceNormalVector = new FloatWA(0);

        getSensorDataFromRemoteAPI(detectionState,
                                   detectedPoint,
                                   detectedObjectHandle,
                                   detectedSurfaceNormalVector);

        this.detectionState = detectionState;
        this.detectedPoint = detectedPoint;
        this.detectedSurfaceNormalVector = detectedSurfaceNormalVector;
        this.detectedObjectHandle = detectedObjectHandle;
    }

    private void getSensorDataFromRemoteAPI(final BoolW detectionState,
                                            final FloatWA detectedPoint,
                                            final IntW detectedObjectHandle,
                                            final FloatWA detectedSurfaceNormalVector) {
        if (handle == null) {
            return;
        }

        final Integer response = RemoteAPI.getInstance().simxReadProximitySensor(RemoteAPI.getClientId(),
                                                                                 handle.getValue(),
                                                                                 detectionState,
                                                                                 detectedPoint,
                                                                                 detectedObjectHandle,
                                                                                 detectedSurfaceNormalVector,
                                                                                 remoteApi.simx_opmode_oneshot_wait);

        if (!(response == remoteApi.simx_return_ok || response == remoteApi.simx_return_novalue_flag)) {
            throw new InvalidResponseException("The sensor " + sensorName + " could not be refreshed.");
        }
    }

    public Boolean isDetectedConsideringMaxDistance() {
        return isDetected() && isValidMaxDistance();
    }

    public Boolean isDetectedConsideringMinDistance() {
        return isDetected() && isValidMinDistance();
    }

    public Boolean isValidMaxDistance() {
        return currentDistance() < MAX_DETECTION_DISTANCE;
    }

    public Boolean isValidMinDistance() {
        return currentDistance() > MIN_DETECTION_DISTANCE;
    }

    public Boolean isDetected() {
        return detectionState != null && detectionState.getValue();
    }

    private Float currentDistance() {
        if (detectedPoint == null) {
            return 999F;
        }

        return detectedPoint.getArray()[2];
    }

    public int getSensorNumber() {
        return sensorNumber;
    }

    public String getSensorName() {
        return sensorName;
    }
}
