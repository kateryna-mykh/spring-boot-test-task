package testtask.expandapis.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import testtask.expandapis.dto.NewProductsDto;
import testtask.expandapis.dto.ProductResponseDto;
import testtask.expandapis.service.ProductService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public List<ProductResponseDto> addProducts(@RequestBody @Valid NewProductsDto productsDto) {
        return productService.saveAll(productsDto);
    }

    @GetMapping("/all")
    public List<ProductResponseDto> getAll(Pageable pageable) {
        return productService.findAll(pageable);
    }
}
