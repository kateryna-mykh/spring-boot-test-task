package testtask.expandapis.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record ProductDto(
        @NotBlank
        @JsonFormat(pattern = "dd-MM-yyyy")
        String entryDate,
        @NotBlank
        String itemCode,
        String itemName,
        @Min(0)
        Long itemQuantity,
        @NotEmpty
        String status) {
}
