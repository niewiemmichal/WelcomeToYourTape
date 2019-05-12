package pl.niewiemmichal.commons.exceptions.mappers;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class BadRequestExceptionMapper implements ExceptionMapper<BadRequestException> {
    @Override
    public Response toResponse(BadRequestException exception) {
        return ExceptionResponseFactory.getFor(exception);
    }
}
