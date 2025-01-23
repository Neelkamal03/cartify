package com.example.demo.service.product;

import com.example.demo.dto.productDto;
import com.example.demo.model.Product;
import com.example.demo.request.AddProductRequest;
import com.example.demo.request.ProductUpdateRequest;

import java.util.List;

public interface IProductService {
    Product addProduct(AddProductRequest product);
    Product getProductById(Long id);
    void deleteProductById(Long id);
    Product updateProduct(ProductUpdateRequest product, Long id);
    List<Product> getAllProducts();
    List<Product> getProductsByCategory(String category);
    List<Product> getProductsByBrand(String brand);
    List<Product> getProductsByCategoryAndBrand(String category, String brand);
    List<Product> getProductsByName(String name);
    List<Product> getProductByBrandAndName(String brand, String name);
    Long countProductsByBrandAndName(String brand, String name);

    productDto convertToDto(Product product);

    List<productDto> getConvertedProducttoProductDto(List<Product> products);
}
