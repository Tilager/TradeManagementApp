package ru.tigran.zakaz.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "sales")
@Data
public class SaleModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int count;
    private double price;

    private Date date;

    @ManyToOne
    @JoinColumn(name = "warehouse_id", referencedColumnName = "id")
    private WarehouseModel warehouse;
}
