package testtask.expandapis.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import testtask.expandapis.dto.NewProductsDto;
import testtask.expandapis.dto.ProductResponseDto;
import testtask.expandapis.mapper.ProductMapper;
import testtask.expandapis.model.Product;
import testtask.expandapis.ropository.ProductRepository;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper mapper;

    @Override
    public List<ProductResponseDto> saveAll(NewProductsDto productsDto) {
        List<Product> mappedProducts = productsDto.records().stream()
                .map(mapper::toModel)
                .toList();
        return productRepository.saveAll(mappedProducts).stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public List<ProductResponseDto> findAll(Pageable pageable) {
        return productRepository.findAll().stream()
                .map(mapper::toDto)
                .toList();
    }
}
