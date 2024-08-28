package org.example.final_project.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.example.final_project.util.Platform;

import java.io.Serializable;

/**
 * SignInRequest
 */
@Getter
public class SignInRequest implements Serializable {

    @NotBlank(message = "username must be not null")
    private String username;

    @NotBlank(message = "username must be not blank")
    private String password;

//    @NotNull(message = "username must be not null")
    private Platform platform;

    private String deviceToken;

    private String version;
}