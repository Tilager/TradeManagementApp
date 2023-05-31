package ru.tigran.zakaz.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tigran.zakaz.models.ProductModel;
import ru.tigran.zakaz.models.SaleModel;

@Repository
public interface SalesRepository extends JpaRepository <SaleModel, Integer> {
}
