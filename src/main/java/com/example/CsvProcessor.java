package com.example;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import com.example.model.CsvProduct;
public class CsvProcessor {
    public static void main(String[] args) throws IOException {
        String file1Path = "file1.csv";
        String file2Path = "file2.csv";
        List<Product> products = new ArrayList<>();
        products.addAll(parseCsv(file1Path));
        products.addAll(parseCsv(file2Path));
        Map<String, List<Product>> productsByShop = products.stream()
                .collect(Collectors.groupingBy(Product::getShop));
        for (Map.Entry<String, List<Product>> entry : productsByShop.entrySet()) {
            String shopName = entry.getKey();
            List<Product> aggregatedProducts = aggregateProducts(entry.getValue());
            saveToCsv(shopName + "_res.csv", aggregatedProducts);
        }
    }
    private static List<Product> parseCsv(String filePath) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath))) {
            return reader.lines()
                    .skip(1)
                    .map(CsvProcessor::mapToProduct)
                    .collect(Collectors.toList());
        }
    }
    protected static Product mapToProduct(String line) {
        String[] fields = line.split(";");
        return new Product(fields[0], fields[1], Double.parseDouble(fields[2]), Integer.parseInt(fields[3]));
    }
    public static List<Product> aggregateProducts(List<Product> products) {
        return products.stream()
                .collect(Collectors.groupingBy(Product::getName))
                .entrySet().stream()
                .map(entry -> {
                    String name = entry.getKey();
                    List<Product> productList = entry.getValue();
                    int totalQuantity = productList.stream().mapToInt(Product::getQuantity).sum();
                    double averagePrice = productList.stream().mapToDouble(Product::getPrice).average().orElse(0);
                    return new Product(name, productList.get(0).getShop(), averagePrice, totalQuantity);
                })
                .collect(Collectors.toList());
    }
    private static void saveToCsv(String filePath, List<Product> products) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath))) {
            writer.write("Name;Shop;Price;Quantity\n");
            for (Product product : products) {
                writer.write(product.toCsvRow());
                writer.newLine();
            }
        }
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class Product {
        private String name;
        private String shop;
        private double price;
        private int quantity;

        public String toCsvRow() {
            return String.join(";", name, shop, String.valueOf(price), String.valueOf(quantity));
        }
    }
}