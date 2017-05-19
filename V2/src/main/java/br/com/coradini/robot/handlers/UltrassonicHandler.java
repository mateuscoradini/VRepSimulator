package br.com.coradini.robot.handlers;

import coppelia.BoolW;
import coppelia.FloatWA;
import coppelia.IntW;

public class UltrassonicHandler extends HandleObject {
	
	private BoolW detectionState = new BoolW(false);
	private FloatWA	detectedPoint = new FloatWA(0);
	private IntW	detectedObjectHandle = new IntW(0);
	private FloatWA	detectedSurfaceNormalVector = new FloatWA(0);
	
	public BoolW getDetectionState() {
		return detectionState;
	}
	public void setDetectionState(BoolW detectionState) {
		this.detectionState = detectionState;
	}
	public FloatWA getDetectedPoint() {
		return detectedPoint;
	}
	public void setDetectedPoint(FloatWA detectedPoint) {
		this.detectedPoint = detectedPoint;
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

}
