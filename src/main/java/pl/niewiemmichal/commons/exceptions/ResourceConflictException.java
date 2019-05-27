package pl.niewiemmichal.commons.exceptions;

import javax.ejb.ApplicationException;

@ApplicationException
public class ResourceConflictException extends RuntimeException {

    public ResourceConflictException(String message) {
        super(message);
    }

}
