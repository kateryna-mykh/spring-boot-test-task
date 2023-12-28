package testtask.expandapis.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;

public record NewProductsDto(
      String table,
      @NotEmpty
      @Size(min = 1, message = "there should be minimum 1 record")
      List<ProductDto> records) {
}
