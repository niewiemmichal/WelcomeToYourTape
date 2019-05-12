package pl.niewiemmichal.commons.exceptions;

import javax.ejb.ApplicationException;

@ApplicationException
public class ResourceConflictException extends RuntimeException {

    private String resource;
    private String field;
    private String firstValue;
    private String secondValue;

    public ResourceConflictException(String resource, String field, String firstValue, String secondValue) {
        super("Got " + resource + " with conflicting " + field + ". Value: " + firstValue + //
                " is different from " + secondValue);
        this.resource = resource;
        this.field = field;
        this.firstValue = firstValue;
        this.secondValue = secondValue;
    }

    public String getResource() {
        return resource;
    }

    public String getValueName() {
        return field;
    }

    public String getFirstValue() {
        return firstValue;
    }

    public String getSecondValue() {
        return secondValue;
    }
}
