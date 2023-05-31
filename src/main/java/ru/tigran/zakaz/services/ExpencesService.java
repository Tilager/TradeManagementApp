package ru.tigran.zakaz.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.tigran.zakaz.models.ExpensesModel;
import ru.tigran.zakaz.repositories.ExpencesRepository;

import java.util.List;

@Service
public class ExpencesService {
    private final ExpencesRepository expencesRepository;

    @Autowired
    public ExpencesService(ExpencesRepository expencesRepository) {
        this.expencesRepository = expencesRepository;
    }

    public List<ExpensesModel> getAllExpences() {
        return expencesRepository.findAll();
    }

    public void saveExpense(ExpensesModel expense) {
        expencesRepository.save(expense);
    }

    public ExpensesModel getById(int id) { return expencesRepository.findById(id).orElse(null); }

    public void removeById(int id) { expencesRepository.deleteById(id); }
}
