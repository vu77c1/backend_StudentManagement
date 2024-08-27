package org.example.final_project.dto.response;

import java.util.List;

public class ResponseError extends ResponseData {

    public ResponseError(int status, String message) {
        super(status, message);
    }

    public ResponseError(int status, List<String> errors) {
        super(status, "Validation errors", errors);

    }
}
