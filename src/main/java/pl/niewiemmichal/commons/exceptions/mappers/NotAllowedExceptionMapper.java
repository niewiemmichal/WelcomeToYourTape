package pl.niewiemmichal.commons.exceptions.mappers;

import javax.ws.rs.NotAllowedException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NotAllowedExceptionMapper implements ExceptionMapper<NotAllowedException> {
    @Override
    public Response toResponse(NotAllowedException exception) {
        return ExceptionResponseFactory.getFor(exception);
    }
}
