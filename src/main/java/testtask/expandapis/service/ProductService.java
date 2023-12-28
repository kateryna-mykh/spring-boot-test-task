package testtask.expandapis.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import testtask.expandapis.dto.NewProductsDto;
import testtask.expandapis.dto.ProductResponseDto;

public interface ProductService {
    List<ProductResponseDto> saveAll(NewProductsDto productsDto);

    List<ProductResponseDto> findAll(Pageable pageable);
}
