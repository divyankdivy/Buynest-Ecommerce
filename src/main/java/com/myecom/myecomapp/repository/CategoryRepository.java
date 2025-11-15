package com.myecom.myecomapp.repository;

import com.myecom.myecomapp.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Boolean existsByName(String name);

    public List<Category> findByIsActiveTrue();
}
