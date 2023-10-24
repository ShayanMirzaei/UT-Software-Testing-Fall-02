package exceptions;

import static defines.Errors.INVALID_SCORE;

public class InvalidScore extends Exception {
    public InvalidScore() {
        super(INVALID_SCORE);
    }
}
