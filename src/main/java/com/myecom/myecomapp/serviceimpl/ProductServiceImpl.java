package com.myecom.myecomapp.serviceimpl;

import com.myecom.myecomapp.model.Product;
import com.myecom.myecomapp.repository.ProductRepository;
import com.myecom.myecomapp.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepo;

    @Override
    public Product saveProduct(Product product) {
        return productRepo.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    @Override
    public Boolean deleteProduct(int productId) {

        Product product = productRepo.findById(productId).orElse(null);

        if (product != null) {
            productRepo.deleteById(productId);
            return true;
        }
        return false;
    }

    @Override
    public Product getProductById(int productId) {

        return productRepo.findById(productId).orElse(null);
    }

    @Override
    public Product updateProduct(Product product, MultipartFile image) {

        try {
            // Fetch existing product from DB
            Product dbProduct = getProductById(product.getProductId());

            // Keep the old image name if a new image isn't uploaded
            String imageName = image.isEmpty() ? dbProduct.getImageName() : image.getOriginalFilename();

            // Update fields
            dbProduct.setTitle(product.getTitle());
            dbProduct.setDescription(product.getDescription());
            dbProduct.setPrice(product.getPrice());
            dbProduct.setCategory(product.getCategory());
            dbProduct.setStock(product.getStock());
            dbProduct.setIsActive(product.getIsActive());
            dbProduct.setImageName(imageName);

            // Save the updated product
            Product updatedProduct = productRepo.save(dbProduct);

            // If image is uploaded, save it to the static folder
            if (!image.isEmpty()) {
                File saveFile = new ClassPathResource("static/img").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "product_img");

                // Create folder if not exists
                if (!Files.exists(path)) {
                    Files.createDirectories(path);
                }

                // Save (replace) the image
                Files.copy(image.getInputStream(), path.resolve(imageName), StandardCopyOption.REPLACE_EXISTING);
            }

            return updatedProduct;
        } catch (Exception e) {
            System.out.println("Error while updating product: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Product> getAllActiveProducts() {
        return productRepo.findByIsActiveTrue();
    }

    @Override
    public List<Product> getProductsByCategory(int categoryId) {
        return productRepo.findByCategoryCategoryIdAndIsActiveTrue(categoryId);
    }
}
