package com.myecom.myecomapp.service;

import com.myecom.myecomapp.model.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    public Product saveProduct(Product product);

    public List<Product> getAllProducts();

    public Boolean deleteProduct(int productId);

    public Product getProductById(int productId);

    public Product updateProduct(Product product, MultipartFile file);

    public List<Product> getAllActiveProducts();

    public List<Product> getProductsByCategory(int categoryId);
}
