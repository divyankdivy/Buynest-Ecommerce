package com.myecom.myecomapp.service;

import com.myecom.myecomapp.model.Category;

import java.util.List;

public interface CategoryService {

    public Category saveCategory(Category category);

    public Boolean isCategoryExist(String categoryName);

    public List<Category> getAllCategories();

    public Boolean deleteCategory(int id);

    public Category getCategoryById(int id);

    public List<Category> getAllActiveCategories();

}
