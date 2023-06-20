package ru.tigran.zakaz.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.tigran.zakaz.models.ExpensesModel;
import ru.tigran.zakaz.repositories.ExpensesRepository;

import java.util.List;

@Service
public class ExpensesService {
    private final ExpensesRepository expensesRepository;

    @Autowired
    public ExpensesService(ExpensesRepository expensesRepository) {
        this.expensesRepository = expensesRepository;
    }

    public List<ExpensesModel> getAllExpenses() {
        return expensesRepository.findAll();
    }

    public void saveExpense(ExpensesModel expense) {
        expensesRepository.save(expense);
    }

    public ExpensesModel getById(int id) { return expensesRepository.findById(id).orElse(null); }

    public void removeById(int id) { expensesRepository.deleteById(id); }

    public void removeByListObjects(List<ExpensesModel> expensesModels) {
        expensesRepository.deleteAll(expensesModels);
    }
}
