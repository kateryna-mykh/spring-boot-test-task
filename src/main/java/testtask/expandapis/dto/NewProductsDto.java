package testtask.expandapis.dto;

import java.util.List;

public record NewProductsDto(
      String table,
      List<ProductDto> records) {
}
