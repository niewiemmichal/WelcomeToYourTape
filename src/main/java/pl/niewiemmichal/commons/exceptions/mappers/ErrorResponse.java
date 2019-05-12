package pl.niewiemmichal.commons.exceptions.mappers;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ErrorResponse {

    private final String message;
    private final String type;
    private List<ErrorResponse> details;

    public ErrorResponse(String message, String type) {
        this.message = message;
        this.type = type;
        this.details = new ArrayList<>();
    }

    public void addDetail(ErrorResponse errorResponse) {
        details.add(errorResponse);
    }

}
