package com.github.prgrms.products;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.prgrms.errors.NotFoundException;
import com.github.prgrms.utils.ApiUtils.ApiResult;

import static com.github.prgrms.utils.ApiUtils.success;
import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("api/products")
public class ProductRestController {

  private final ProductService productService;

  public ProductRestController(ProductService productService) {
    this.productService = productService;
  }

  @GetMapping(path = "/{id}")
  public ApiResult<ProductDto> findById(@PathVariable Long id) {
    return success(productService.findById(id)
            .map(ProductDto::new)
            .orElseThrow(() -> new NotFoundException("Could not found product for " + id)));
  }

  @GetMapping
  public ApiResult<List<ProductDto>> findAll() {
    return success(productService.findAll().stream()
      .map(ProductDto::new)
      .collect(toList()));
  }

}