package testtask.expandapis.dto;

import jakarta.validation.constraints.NotBlank;

public record UserAuthDto(
        @NotBlank String username, 
        @NotBlank String password) {
}
