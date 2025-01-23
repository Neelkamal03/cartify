package com.example.demo.controller;

import com.example.demo.dto.productDto;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Product;
import com.example.demo.request.AddProductRequest;
import com.example.demo.request.ProductUpdateRequest;
import com.example.demo.response.ApiResponse;
import com.example.demo.service.product.IProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@AllArgsConstructor
@RestController
@RequestMapping("${api.prefix}/products")
public class ProductController {
    private final IProductService productService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllProducts() {
        try {
            //To convert Product to ProductDto we need a dependency model mapper.
            List<Product> products = productService.getAllProducts();
            List<productDto> convertedProduct = productService.getConvertedProducttoProductDto(products);
            return ResponseEntity.ok(new ApiResponse("success", convertedProduct));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error", INTERNAL_SERVER_ERROR));
        }
    }

    //Format for writing the path
    //base/path/{pathVariable}
    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long productId) {
        try {
            Product product = productService.getProductById(productId);
            productDto productDto = productService.convertToDto(product);
            return ResponseEntity.ok(new ApiResponse("success", productDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/add/product")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest request) {
        try {
            Product theProduct = productService.addProduct(request);
            return ResponseEntity.ok(new ApiResponse("Add product success", theProduct));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/update/{productId}")
    public ResponseEntity<ApiResponse> updateProduct(@RequestBody ProductUpdateRequest request, @PathVariable Long productId) {
        try {
            Product product = productService.updateProduct(request, productId);
            return ResponseEntity.ok(new ApiResponse("Updated product success!", product));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId) {
        try {
            productService.deleteProductById(productId);
            return ResponseEntity.ok(new ApiResponse("Product Deleted", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/brand/{brand}/name/{name}")
    public ResponseEntity<ApiResponse> getProductsByBrandAndName(@PathVariable String brand, @PathVariable String name) {
        try {
            List<Product> products = productService.getProductByBrandAndName(brand, name);

            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No product found for this", null));
            }
            List<productDto> convertedProduct = productService.getConvertedProducttoProductDto(products);
            return ResponseEntity.ok(new ApiResponse("success", convertedProduct));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/by-category-and-brand/products")
    public ResponseEntity<ApiResponse> getProductsByCategoryAndBrand(@RequestParam String category, @RequestParam String brand) {
        try {
            List<Product> products = productService.getProductsByCategoryAndBrand(category, brand);

            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No product found for this", null));
            }
            List<productDto> convertedProduct = productService.getConvertedProducttoProductDto(products);
            return ResponseEntity.ok(new ApiResponse("success", convertedProduct));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ApiResponse> getProductsByName(@PathVariable String name) {
        try {
            List<Product> products = productService.getProductsByName(name);

            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No product found for this", null));
            }
            List<productDto> convertedProduct = productService.getConvertedProducttoProductDto(products);
            return ResponseEntity.ok(new ApiResponse("success", convertedProduct));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/by-brand/products")
    public ResponseEntity<ApiResponse> findProductsByBrand(@RequestParam String brand) {
        try {
            List<Product> products = productService.getProductsByBrand(brand);

            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No product found for this", null));
            }
            List<productDto> convertedProduct = productService.getConvertedProducttoProductDto(products);
            return ResponseEntity.ok(new ApiResponse("success", convertedProduct));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse> findProductsByCategory(@PathVariable String category) {
        try {
            List<Product> products = productService.getProductsByCategory(category);
            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No product found for this", null));
            }
            List<productDto> convertedProduct = productService.getConvertedProducttoProductDto(products);
            return ResponseEntity.ok(new ApiResponse("success", convertedProduct));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("count/by-brand-and-name/products")
    public ResponseEntity<ApiResponse> countProductByBrandAndName(@RequestParam String brand, @RequestParam String name) {
        try {
            var count = productService.countProductsByBrandAndName(brand, name);
            return ResponseEntity.ok(new ApiResponse("Product count!", count));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
