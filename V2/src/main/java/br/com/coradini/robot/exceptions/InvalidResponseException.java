package br.com.coradini.robot.exceptions;

public class InvalidResponseException extends RuntimeException {

    public InvalidResponseException(final String s) {
        super(s);
    }
}
