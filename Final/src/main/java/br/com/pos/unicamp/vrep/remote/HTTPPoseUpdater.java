package br.com.pos.unicamp.vrep.remote;

import java.io.IOException;

import br.com.pos.unicamp.vrep.common.Coordinate;
import br.com.pos.unicamp.vrep.common.Pose;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class HTTPPoseUpdater {

    public Pose get() {
        Pose pose = Pose.initial();
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpGet httpget = new HttpGet("http://localhost:8090/pose");
            try (CloseableHttpResponse response = httpclient.execute(httpget)) {
                String responseString = new BasicResponseHandler().handleResponse(response);
                String[] coord = responseString.split(",");
                float x = Float.parseFloat(coord[0]);
                float y = Float.parseFloat(coord[1]);
                float theta = Float.parseFloat(coord[2]);
                Coordinate coordinate = new Coordinate(x,
                                                       y,
                                                       0);
                pose = new Pose(coordinate,
                                theta);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return pose;
    }
}
