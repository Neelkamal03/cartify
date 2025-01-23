package com.example.demo.service.category;

import com.example.demo.exception.AlreadyExistsExecption;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Category;
import com.example.demo.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
    private final CategoryRepository categoryRepo;

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepo.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Resource Not Found"));
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepo.findByName(name);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepo.findAll();
    }

    @Override
    public Category addCategory(Category category) {
        return Optional.of(category).filter(c->!categoryRepo.existsByName(c.getName()))
                .map(categoryRepo::save)
                .orElseThrow(()->new AlreadyExistsExecption(category.getName()+"already Exists"));
    }

    @Override
    public Category updateCategory(Category category, Long id) {
        //Optional is conatiner object it may or may not contain null value.
        //It is used to prevent Nullpointerexception
        //If a value is present, the Optional contains it.
        //If a value is absent (null), the Optional is empty.
        // If category Optional is empty.
        // map() is skipped—the lambda never runs.
        //orElseThrow() throws an exception
        return Optional.ofNullable(getCategoryById(id))
                .map(oldCategory->{
                    oldCategory.setName(category.getName());
                    return categoryRepo.save(oldCategory);
                }).orElseThrow(()->new ResourceNotFoundException("Resource Not Found"));
    }

    @Override
    public void deleteCategoryById(Long id) {
        categoryRepo.findById(id)
                .ifPresentOrElse(categoryRepo::delete, ()->{
                    throw new ResourceNotFoundException("Resource Not Found");
                });
    }
}
