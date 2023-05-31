package ru.tigran.zakaz.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tigran.zakaz.models.ExpensesModel;
import ru.tigran.zakaz.models.ProductModel;

@Repository
public interface ExpencesRepository extends JpaRepository <ExpensesModel, Integer> {
}
