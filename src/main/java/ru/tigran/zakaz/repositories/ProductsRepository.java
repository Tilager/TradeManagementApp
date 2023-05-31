package ru.tigran.zakaz.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tigran.zakaz.models.ProductModel;

import java.util.List;

@Repository
public interface ProductsRepository extends JpaRepository <ProductModel, Integer> {
    List<ProductModel> findAllByWarehouseIsNull();
}
