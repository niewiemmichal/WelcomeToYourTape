package pl.niewiemmichal.commons.exceptions;

public class ResourceConflictException extends RuntimeException {

    private String resource;
    private String valueName;
    private String firstValue;
    private String secondValue;

    public ResourceConflictException(String resource, String valueName, String firstValue, String secondValue) {
        super("Got " + resource + " with conflicting " + valueName + ". Value: " + firstValue + //
                " is different from " + secondValue);
        this.resource = resource;
        this.valueName = valueName;
        this.firstValue = firstValue;
        this.secondValue = secondValue;
    }

    public String getResource() {
        return resource;
    }

    public String getValueName() {
        return valueName;
    }

    public String getFirstValue() {
        return firstValue;
    }

    public String getSecondValue() {
        return secondValue;
    }
}
