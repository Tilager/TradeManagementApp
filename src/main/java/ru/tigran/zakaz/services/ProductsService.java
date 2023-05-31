package ru.tigran.zakaz.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.tigran.zakaz.models.ProductModel;
import ru.tigran.zakaz.repositories.ProductsRepository;

import java.util.List;

@Service
public class ProductsService {
    private final ProductsRepository productRepository;

    @Autowired
    public ProductsService(ProductsRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductModel> getAllProducts() {
        return productRepository.findAll();
    }

    public void saveProduct(ProductModel product) {
        productRepository.save(product);
    }

    public List<ProductModel> getAllProductsWithoutWarehouse() {
        return productRepository.findAllByWarehouseIsNull();
    }

    public ProductModel getById(Integer id) { return productRepository.findById(id).orElse(null); }

    public void removeById(Integer id) { productRepository.deleteById(id); }

    public void removeByListObjects(List<ProductModel> productModelList) {
        productRepository.deleteAll(productModelList);
    }
}
