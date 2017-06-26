package br.com.pos.unicamp.vrep.sensors.ultrasonic;

import java.util.ArrayList;
import java.util.List;

import br.com.pos.unicamp.vrep.exceptions.InvalidObjectIdException;

import static br.com.pos.unicamp.vrep.utils.LoopUtils.loop;

/*
 *  #### UltrasonicSensors #####
 *
 *       12    13
 *    11   =  =   14
 *  10  =        =  15
 *  9  =          =  16
 *  8  =          =  1
 *   7  =        =  2
 *    6    =  =    3
 *        5    4
 */
public class UltrasonicSensors implements Runnable {

    private List<UltrasonicSensor> sensors;

    public UltrasonicSensors() {
        sensors = makeSensors();
    }

    private List<UltrasonicSensor> makeSensors() {
        final List<UltrasonicSensor> sensors = new ArrayList<>();

        for (int sensorNumber = 1; sensorNumber <= 16; sensorNumber++) {
            sensors.add(new UltrasonicSensor(sensorNumber));
        }

        return sensors;
    }

    public List<UltrasonicSensor> getSensors() {
        return sensors;
    }

    public UltrasonicSensor getSensor(final int sensorNumber) {
        return sensors
                .stream()
                .filter(sensor -> sensor.getSensorNumber() == sensorNumber)
                .findFirst()
                .orElseThrow(() -> new InvalidObjectIdException("There's no sensor for the number " + sensorNumber));
    }

    @Override
    public void run() {
        loop(() -> {
                 sensors.forEach(UltrasonicSensor::refresh);
             },
             100);
    }
}
