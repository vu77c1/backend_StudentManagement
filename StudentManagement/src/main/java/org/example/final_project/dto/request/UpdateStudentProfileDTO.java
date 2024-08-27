package org.example.final_project.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
public class UpdateStudentProfileDTO implements Serializable {
  @Serial private static final long serialVersionUID = 1L;
  private String email;
  private String phoneNumber;
  private String address;
}
