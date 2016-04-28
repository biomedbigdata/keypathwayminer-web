package kpm.web.exceptions

/**
 * Created by: Martin
 * Date: 08-09-14
 */
public class UnknownRunParametersException  extends Exception{
    public UnknownRunParametersException() {}

    public UnknownRunParametersException(String message)
    {
        super(message);
    }
}
