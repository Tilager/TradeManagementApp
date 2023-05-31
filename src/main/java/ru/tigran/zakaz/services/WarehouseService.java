package ru.tigran.zakaz.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.tigran.zakaz.models.ProductModel;
import ru.tigran.zakaz.models.WarehouseModel;
import ru.tigran.zakaz.repositories.WarehouseRepository;

import java.util.List;

@Service
public class WarehouseService {
    private final WarehouseRepository warehouseRepository;

    @Autowired
    public WarehouseService(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    public List<WarehouseModel> getAllWarehouses() {
        return warehouseRepository.findAll();
    }

    public void saveWarehouses(WarehouseModel warehouse) {
        warehouseRepository.save(warehouse);
    }

    public void removeByListObjects(List<WarehouseModel> productModelList) {
        warehouseRepository.deleteAll(productModelList);
    }

    public WarehouseModel getById(int id) { return warehouseRepository.findById(id).orElse(null); }

    public void removeById(int id) { warehouseRepository.deleteById(id); }
}
