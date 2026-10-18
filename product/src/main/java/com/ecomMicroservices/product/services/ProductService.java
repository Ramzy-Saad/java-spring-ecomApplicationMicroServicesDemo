package com.ecomMicroservices.product.services;
import com.ecomMicroservices.product.dto.ProductRequest;
import com.ecomMicroservices.product.dto.ProductResponse;
import com.ecomMicroservices.product.models.Product;
import com.ecomMicroservices.product.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = new Product();
        mappingProductRequestToProduct(product, productRequest);
        Product savedProduct = productRepository.save(product);
        return mappingProductToToProductResponse(savedProduct);
    }



    public Optional<ProductResponse> updateProduct(Long id, ProductRequest productRequest) {
        return productRepository.findById(id).map(
                existingProduct -> {
                    mappingProductRequestToProduct(existingProduct,productRequest);
                    Product savedProduct =  productRepository.save(existingProduct);
                    return mappingProductToToProductResponse(savedProduct);
                });
    }



    public List<ProductResponse> getAllProducts() {
        return productRepository.findByActiveTrue()
                .stream()
                .map(this::mappingProductToToProductResponse)
                .collect(Collectors.toList());
    }

    public boolean deleteProduct(Long id) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setActive(false);
                    productRepository.save(product);
                    return true;
                })
                .orElse(false);
    }


    private ProductResponse mappingProductToToProductResponse(Product savedProduct) {
        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(savedProduct.getId());
        productResponse.setName(savedProduct.getName());
        productResponse.setDesc(savedProduct.getDesc());
        productResponse.setPrice(savedProduct.getPrice());
        productResponse.setStock(savedProduct.getStock());
        productResponse.setCategory(savedProduct.getCategory());
        productResponse.setImageUrl(savedProduct.getImageUrl());
        productResponse.setActive(savedProduct.getActive());
        return productResponse;
    }


    private void mappingProductRequestToProduct(Product product, ProductRequest productRequest) {
        product.setName(productRequest.getName());
        product.setDesc(productRequest.getDesc());
        product.setPrice(productRequest.getPrice());
        product.setStock(productRequest.getStock());
        product.setCategory(productRequest.getCategory());
        product.setImageUrl(productRequest.getImageUrl());
    }

    public List<ProductResponse> searchProduct(String keyword) {
        return productRepository.searchproducts(keyword).stream()
                .map(this::mappingProductToToProductResponse)
                .collect(Collectors.toList());
    }
}
