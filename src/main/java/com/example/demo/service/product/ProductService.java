package com.example.demo.service.product;

import com.example.demo.dto.ImageDto;
import com.example.demo.dto.productDto;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Category;
import com.example.demo.model.Image;
import com.example.demo.model.Product;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ImageRepository;
import com.example.demo.repository.ProductRepo;
import com.example.demo.request.AddProductRequest;
import com.example.demo.request.ProductUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductService implements  IProductService{

    private final ProductRepo productRepo;
    private final CategoryRepository categoryRepo;
    private final ModelMapper modelMapper;
    private final ImageRepository imageRepository;
//    public ProductService(ProductRepo productRepo, CategoryRepository categoryRepo){
//        this.productRepo=productRepo;
//        this.categoryRepo=categoryRepo;
//
//    }

    @Override
    public Product addProduct(AddProductRequest request) {
        //check if category exist in DB
        Category category= Optional.ofNullable(categoryRepo.findByName(request.getCategory().getName()))
                .orElseGet(()->{
                    Category newCategory=new Category();
                    newCategory.setName(request.getCategory().getName());
                    return categoryRepo.save(newCategory);
                });
        request.setCategory(category);
        Product product=createProduct(request, category);
        return productRepo.save(product);
    }
    private Product createProduct(AddProductRequest request, Category category){
       return new Product(
               request.getName(),
               request.getBrand(),
               request.getPrice(),
               request.getInventory(),
               request.getDescription(),
               category
       );
    }

    @Override
    public Product getProductById(Long id) {
        return productRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Product Not Found"));
    }

    @Override
    public void deleteProductById(Long id) {
        productRepo.findById(id).ifPresentOrElse(productRepo::delete,
                ()->{throw new ResourceNotFoundException("Product Not Found");});
    }

    @Override
    public Product updateProduct(ProductUpdateRequest product, Long id) {
       return productRepo.findById(id)
               .map(existingProduct->updateExistingProduct(existingProduct, product))
               .map(productRepo::save)
               .orElseThrow(()->new ResourceNotFoundException("Product Not Found"));
    }
    private Product updateExistingProduct(Product existingProduct, ProductUpdateRequest request){
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setDescription(request.getDescription());
        Category category=categoryRepo.findByName(request.getCategory().getName());
        existingProduct.setCategory(category);
        return existingProduct;
    }
    @Override
    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String categoryName) {
        //Category is an object, and we need to find by category name.
        return productRepo.findByCategoryName(categoryName);
       // return productRepo.findByCategoryName(categoryName);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepo.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepo.findByCategoryNameAndBrand(category, brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepo.findByName(name);
    }

    @Override
    public List<Product> getProductByBrandAndName(String brand, String name) {
        return productRepo.findByBrandAndName(brand, name);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepo.countByBrandAndName(brand, name);
    }
    @Override
    public productDto convertToDto(Product product){
        productDto productDto=modelMapper.map(product, productDto.class);
        List<Image> images=imageRepository.findByProductId(product.getId());
        List<ImageDto> imageDtos=images.stream()
                .map(image-> modelMapper.map(image, ImageDto.class))
                .toList();
        productDto.setImages(imageDtos);
        return productDto;
    }
    @Override
    public List<productDto> getConvertedProducttoProductDto(List<Product> products){
        return products.stream().map(product->convertToDto(product)).toList();
    }
}
