package org.example.final_project.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class StudentScoreRequest {
    private Long enrollmentId;
    private BigDecimal scoreTheory;
    private BigDecimal scorePractical;
}
