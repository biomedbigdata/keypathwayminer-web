package kpm.web.exceptions

/**
 * Created by Martin on 10-10-2014.
 */
class InvalidRunParametersException extends Exception {
    public InvalidRunParametersException() {}

    public InvalidRunParametersException(String message) {
        super(message);
    }
}