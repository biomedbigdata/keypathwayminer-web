package kpm.web.exceptions

/**
 * Created by Martin on 07-10-2014.
 */
public class InvalidRequestException extends Exception {
    public InvalidRequestException() {}

    public InvalidRequestException(String message) {
        super(message);
    }
}