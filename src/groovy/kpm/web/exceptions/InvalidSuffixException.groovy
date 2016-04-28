package kpm.web.exceptions

/**
 * Created by: Martin
 * Date: 04-08-14
 */
class InvalidSuffixException extends Exception{
    public InvalidSuffixException() {}

    public InvalidSuffixException(String message)
    {
        super(message);
    }
}
