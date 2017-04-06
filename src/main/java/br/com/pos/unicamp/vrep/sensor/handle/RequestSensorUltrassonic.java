package br.com.pos.unicamp.vrep.sensor.handle;

import coppelia.BoolW;
import coppelia.FloatWA;
import coppelia.IntW;

public class RequestSensorUltrassonic {
    
    
    private BoolW state = new BoolW(true);
    private FloatWA coord = new FloatWA(0);
    private IntW detectedObjectHandle = new IntW(0);
    
    private String sensorName;
    
    
    private IntW handle = new IntW(0);
    
    private FloatWA detectedSurfaceNormalVector = new FloatWA(0);
    private int sensorNumber;
    
    
    public BoolW getState() {
        return state;
    }
    
    public void setState(BoolW state) {
        this.state = state;
    }
    
    public FloatWA getCoord() {
        return coord;
    }
    
    public void setCoord(FloatWA coord) {
        this.coord = coord;
    }
    
    public IntW getDetectedObjectHandle() {
        return detectedObjectHandle;
    }
    
    public void setDetectedObjectHandle(IntW detectedObjectHandle) {
        this.detectedObjectHandle = detectedObjectHandle;
    }
    
    public FloatWA getDetectedSurfaceNormalVector() {
        return detectedSurfaceNormalVector;
    }
    
    public void setDetectedSurfaceNormalVector(FloatWA detectedSurfaceNormalVector) {
        this.detectedSurfaceNormalVector = detectedSurfaceNormalVector;
    }

    
    public int getSensorNumber() {
        return sensorNumber;
    }

    
    public void setSensorNumber(int sensorNumber) {
        this.sensorNumber = sensorNumber;
    }

    
    public IntW getHandle() {
        return handle;
    }

    
    public void setHandle(IntW handle) {
        this.handle = handle;
    }

    
    public String getSensorName() {
        return sensorName;
    }

    
    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

}
