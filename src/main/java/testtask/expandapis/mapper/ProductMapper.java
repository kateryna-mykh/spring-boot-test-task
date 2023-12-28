package testtask.expandapis.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import testtask.expandapis.config.MapperConfig;
import testtask.expandapis.dto.ProductDto;
import testtask.expandapis.dto.ProductResponseDto;
import testtask.expandapis.model.Product;

@Mapper(config = MapperConfig.class)
public interface ProductMapper {
    ProductResponseDto toDto(Product product);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "entryDate", dateFormat = "dd-MM-yyyy")
    Product toModel(ProductDto dto);
}
