package ru.tigran.zakaz.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tigran.zakaz.models.ExpensesModel;

@Repository
public interface ExpensesRepository extends JpaRepository <ExpensesModel, Integer> {
}
