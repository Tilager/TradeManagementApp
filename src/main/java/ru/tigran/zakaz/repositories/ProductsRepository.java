package ru.tigran.zakaz.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.tigran.zakaz.models.ProductModel;

import java.util.List;

@Repository
public interface ProductsRepository extends JpaRepository <ProductModel, Integer> {
    @Query(value = "select p from ProductModel p inner join p.warehouse w where w.count > 0")
    List<ProductModel> findAllProductsInStock();

    List<ProductModel> findAllByWarehouseIsNull();
}
