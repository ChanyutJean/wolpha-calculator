package jean.wolpha;

import java.rmi.UnexpectedException;

public class WolphaParenthesisException extends Exception {
    public WolphaParenthesisException(String errorMessage) {
        super(errorMessage);
    }
}
