package pl.niewiemmichal.commons.exceptions.mappers;

import pl.niewiemmichal.commons.exceptions.ResourceDoesNotExistException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ResourceDoesNotExistExceptionMapper implements ExceptionMapper<ResourceDoesNotExistException> {
    @Override
    public Response toResponse(ResourceDoesNotExistException exception) {
        return ExceptionResponseFactory.getFor(exception);
    }
}
