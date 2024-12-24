package com.example.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CsvProduct {
    private String name;
    private String shop;
    private double price;
    private int quantity;
    public String toCsvRow() {
        return String.join(";", name, shop, String.valueOf(price), String.valueOf(quantity));
    }
}
