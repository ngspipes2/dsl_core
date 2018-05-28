package pt.isel.ngspipes.dsl_core.descriptors.exceptions;

public class DSLCoreException extends Exception {

    public DSLCoreException() {
        super();
    }

    public DSLCoreException(String message) {
        super(message);
    }

    public DSLCoreException(String message, Throwable cause) {
        super(message, cause);
    }

    public DSLCoreException(Throwable cause) {
        super(cause);
    }

}
