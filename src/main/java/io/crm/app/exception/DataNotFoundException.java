package io.crm.app.exception;

import java.util.Collection;

public class DataNotFoundException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = -2291286884310210032L;

    public DataNotFoundException() {
        super();
    }
    public DataNotFoundException(String message){
        super(message);
    }
    public DataNotFoundException(String message, Throwable e){
        super(message, e);
    }

    @SuppressWarnings("rawtypes")
    public static void throwExceptionIfEmpty(Object id, Object toEvaluate, String subject) {
        if (toEvaluate == null) {
            throw new DataNotFoundException("Unable to find " + subject + " with given id: " + id);
        } else if (toEvaluate instanceof Collection && ((Collection) toEvaluate).isEmpty()) {
            // if it's a collection make sure it's not empty
            throw new DataNotFoundException("Unable to find " + subject + " with given id: " + id);
        }
    }
}