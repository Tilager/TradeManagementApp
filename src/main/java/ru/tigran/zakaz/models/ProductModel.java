package ru.tigran.zakaz.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "products")
@Data
public class ProductModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String description;

    private String provider;

    @OneToOne(mappedBy = "product", fetch = FetchType.EAGER)
    private WarehouseModel warehouse;
}
