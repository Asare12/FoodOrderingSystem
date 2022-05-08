package com.davidasare.FoodOrderingSystem.services;

import com.davidasare.FoodOrderingSystem.exception.CustomException;
import com.davidasare.FoodOrderingSystem.model.Category;
import com.davidasare.FoodOrderingSystem.model.Product;
import com.davidasare.FoodOrderingSystem.payload.request.ProductRequest;
import com.davidasare.FoodOrderingSystem.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public List<ProductRequest> listProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductRequest> productRequestList = new ArrayList<>();
        for(Product product : products){
            ProductRequest request = getRequestFromProduct(product);
            productRequestList.add(request);
        }
        return productRequestList;
    }

    public static ProductRequest getRequestFromProduct(Product product){
        return new ProductRequest(product);
    }

    public void addProduct(ProductRequest productRequest, Category category) {
        Product product = getProductFromRequest(productRequest, category);
        productRepository.save(product);
    }

    public static Product getProductFromRequest(ProductRequest productRequest, Category category) {
        return new Product(productRequest, category);
    }

    public void updateProduct(Long productID, ProductRequest productRequest, Category category) {
        Product product = getProductFromRequest(productRequest, category);
        product.setId(productID);
        productRepository.save(product);
    }

    public Product getProductById(Long productId) throws CustomException {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isEmpty())
            throw new CustomException("Product id is invalid " + productId);
        return optionalProduct.get();
    }

    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }
}
