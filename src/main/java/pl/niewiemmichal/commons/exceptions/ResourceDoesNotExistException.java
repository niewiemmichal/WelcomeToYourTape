package pl.niewiemmichal.commons.exceptions;

public class ResourceDoesNotExistException extends RuntimeException {

    private final String resource;
    private final String field;
    private final String value;

    public ResourceDoesNotExistException(String resource, String field, String value) {
        super(resource + " with " + field + "=" + value + " does not exist");
        this.resource = resource;
        this.field = field;
        this.value = value;
    }

    public String getResource() {
        return resource;
    }

    public String getField() {
        return field;
    }

    public String getValue() {
        return value;
    }
}
