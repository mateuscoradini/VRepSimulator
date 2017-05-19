package br.com.coradini.robot.exceptions;

public class ConnectionProxyException extends VRepClientException {

    public ConnectionProxyException(String string) {
        super.setMsg(string);
    }
}
