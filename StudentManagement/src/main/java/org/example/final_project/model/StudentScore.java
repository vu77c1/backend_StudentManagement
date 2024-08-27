package org.example.final_project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Builder
@Table(name = "tbl_student_scores")
@NoArgsConstructor
@AllArgsConstructor
public class StudentScore extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "score_id", nullable = false)
    private Long id;

    @Column(name = "score_theory", precision = 5, scale = 2)
    private BigDecimal scoreTheory;

    @Column(name = "score_practical", precision = 5, scale = 2)
    private BigDecimal scorePractical;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "enrollment_id", nullable = false)
    private Enrollment enrollment;

}