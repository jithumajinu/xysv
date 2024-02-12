package io.crm.app.exception;

public class CrmException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 6536689676581686806L;

    public CrmException() {
        super();
    }

    public CrmException(String message) {
        super(message);
    }

    public CrmException(String message, Throwable cause) {
        super(message, cause);
    }

    public CrmException(Throwable cause) {
        super(cause);
    }

    protected CrmException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
