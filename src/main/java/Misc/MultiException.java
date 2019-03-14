package Misc;

import java.util.ArrayList;
import java.util.List;


/**
 * Custom exception that holds multiple exceptions
 */
public class MultiException extends Exception {
    public List<Exception> exceptionList = new ArrayList<>();
}
