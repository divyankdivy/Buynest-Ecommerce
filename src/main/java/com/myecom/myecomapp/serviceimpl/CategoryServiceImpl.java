package com.myecom.myecomapp.serviceimpl;

import com.myecom.myecomapp.model.Category;
import com.myecom.myecomapp.repository.CategoryRepository;
import com.myecom.myecomapp.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository catRepo;

    @Override
    public Category saveCategory(Category category) {
        return catRepo.save(category);
    }

    @Override
    public Boolean isCategoryExist(String categoryName) {
        return catRepo.existsByName(categoryName);
    }

    @Override
    public List<Category> getAllCategories() {
        return catRepo.findAll() ;
    }

    @Override
    public Boolean deleteCategory(int id) {
        Category category = catRepo.findById(id).orElse(null);
        if (!Objects.isNull(category)) {
            catRepo.delete(category);
            return true;
        }
        return false;
    }

    @Override
    public Category getCategoryById(int id) {
        return catRepo.findById(id).orElse(null);
    }

    @Override
    public List<Category> getAllActiveCategories() {
        return catRepo.findByIsActiveTrue();
    }
}
