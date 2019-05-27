package pl.niewiemmichal.commons.exceptions.mappers;
import pl.niewiemmichal.commons.exceptions.ResourceConflictException;
import pl.niewiemmichal.commons.exceptions.ResourceDoesNotExistException;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotAllowedException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

class ExceptionResponseFactory {
    static Response getFor(RuntimeException exception) {
        exception.printStackTrace();
        return Response.serverError()
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(new ErrorResponse("Unexpected error occurred. Please contact system administrator",
                        "unexpected_error"))
                .build();
    }

    static Response getFor(BadRequestException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(new ErrorResponse(exception.getMessage(),"invalid_argument"))
                .build();
    }

    static Response getFor(ResourceDoesNotExistException exception) {
        ErrorResponse errorResponse = new ErrorResponse("Resource does not exist", "not_found");
        errorResponse.addDetail(new ErrorResponse(exception.getMessage(), ""));
        return Response.status(Response.Status.NOT_FOUND)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(errorResponse)
                .build();
    }

    static Response getFor(NotAllowedException exception) {
        ErrorResponse errorResponse = new ErrorResponse("Method not allowed", "not_allowed");
        errorResponse.addDetail(new ErrorResponse(exception.getMessage(), "description"));
        return Response.status(Response.Status.METHOD_NOT_ALLOWED)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(errorResponse)
                .build();
    }

    static Response getFor(ResourceConflictException exception) {
        ErrorResponse errorResponse = new ErrorResponse("Resource conflict", "conflict");
        errorResponse.addDetail(new ErrorResponse(exception.getMessage(), "description"));
        return Response.status(Response.Status.CONFLICT)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(errorResponse)
                .build();
    }
}
