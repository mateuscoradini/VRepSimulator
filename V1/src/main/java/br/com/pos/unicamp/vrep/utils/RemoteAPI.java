package br.com.pos.unicamp.vrep.utils;

import br.com.pos.unicamp.vrep.exceptions.RemoteAPIException;
import coppelia.remoteApi;

public class RemoteAPI {

    private static final String IP = "127.0.0.1";

    private static final Integer PORT = 25000;

    private static Integer clientId;

    private static remoteApi instance;

    private RemoteAPI() {
    }

    public static remoteApi getInstance() throws RemoteAPIException {
        if (instance == null) {
            throw new RemoteAPIException("'RemoteAPI' class was not started.");
        }

        return instance;
    }

    public static Integer getClientId() {
        return clientId;
    }

    public static Boolean isConnected() {
        return getInstance().simxGetConnectionId(getClientId()) != -1;
    }

    public static void session(final Runnable runnable) {
        start();
        //runnable.run();
        stop();
    }

    private static void start() {
        instance = makeRemoteApi();
    }

    private static void stop() {
        closeConnection(instance,
                        clientId);
    }

    private static remoteApi makeRemoteApi() {
        final remoteApi remoteApi = new remoteApi();

        closeAllOpenedConnections(remoteApi);
        clientId = openConnection(remoteApi);

        return remoteApi;
    }

    private static int openConnection(final remoteApi api) {
        final boolean waitUntilConnected = true;
        final boolean doNotReconnectOnceDisconnected = true;
        final int timeOutInMs = 2000;
        final int threadCycleInMs = 5;

        return api.simxStart(IP,
                             PORT,
                             waitUntilConnected,
                             doNotReconnectOnceDisconnected,
                             timeOutInMs,
                             threadCycleInMs);
    }

    private static void closeAllOpenedConnections(final remoteApi api) {
        closeConnection(api,
                        -1);
    }

    private static void closeConnection(final remoteApi api,
                                        final int clientID) {
        api.simxFinish(clientID);
    }
}
