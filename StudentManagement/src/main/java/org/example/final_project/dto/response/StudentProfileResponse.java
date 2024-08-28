package org.example.final_project.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * create StudentProfileResponse dto
 */
@Getter
@Setter
@Builder
public class StudentProfileResponse implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;

  private String fullName;
  private String studentId;
  private String department;
  private String email;
  private String phone;
  private String address;
  private String profilePicture;
}
