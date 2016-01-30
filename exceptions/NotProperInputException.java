package exceptions;

/**
 * @author  <a href="mailto:vaino.niemi@cs.tamk.fi">Vaino Niemi</a>
 * @version 2015.1127
 * @since   1.8
 */
public class NotProperInputException extends RuntimeException {

    /**
     * Creates a new exception with message.
     *
     * @param msg the message
     */
    public NotProperInputException(String msg) {

        super(msg);
    }
}
