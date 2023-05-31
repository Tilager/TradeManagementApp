package ru.tigran.zakaz.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "warehouse")
@Data
public class WarehouseModel{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private double purchasingPrice;
    private double retailPrice;
    private int count;

    @Column(length = 20*1024*1024)
    private byte[] image;

    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", referencedColumnName = "id", unique = true)
    private ProductModel product;

    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL)
    private List<SaleModel> sales;


}
