package ru.tigran.zakaz.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.tigran.zakaz.models.SaleModel;
import ru.tigran.zakaz.repositories.SalesRepository;

import java.util.List;

@Service
public class SalesService {
    private final SalesRepository salesRepository;

    @Autowired
    public SalesService(SalesRepository salesRepository) {
        this.salesRepository = salesRepository;
    }

    public List<SaleModel> getAllSales() {
        return salesRepository.findAll();
    }

    public void saveSale(SaleModel sale) {
        salesRepository.save(sale);
    }

    public SaleModel getById(int id) { return salesRepository.findById(id).orElse(null); }

    public void removeById(int id) { salesRepository.deleteById(id); }
}
