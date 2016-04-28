package kpm.web.exceptions

/**
 * User: Martin
 * Date: 04-09-14
 */
public class UnknownQuestException extends Exception{
    public UnknownQuestException() {}

    public UnknownQuestException(String message)
    {
        super(message);
    }
}
