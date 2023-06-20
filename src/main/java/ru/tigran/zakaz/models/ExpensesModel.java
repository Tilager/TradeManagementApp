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

    private String name;
    private Double price;
    private Integer count;
    private Double cost;

    @PreUpdate
    private void beforeUpdate() {
        setCost();
    }

    public void setCount(Integer count) {
        this.count = count;
        setCost();
    }

    public void setPrice(Double price) {
        this.price = price;
        setCost();
    }

    public void setCost() {
        if (price != null && count != null)
            this.cost = this.price * this.count;
    }
}
