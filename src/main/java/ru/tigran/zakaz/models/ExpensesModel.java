package ru.tigran.zakaz.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "expences")
@Data
public class ExpensesModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int name;
    private double price;
    private int count;
}
