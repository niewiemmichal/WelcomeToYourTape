package pl.niewiemmichal.commons.exceptions.mappers;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.ExceptionMapper;

import pl.niewiemmichal.commons.exceptions.ResourceConflictException;

@Provider
public class ResourceConflictExceptionMapper implements ExceptionMapper<ResourceConflictException>
{
    @Override
    public Response toResponse(ResourceConflictException exception) {
        return ExceptionResponseFactory.getFor(exception);
    }
}
