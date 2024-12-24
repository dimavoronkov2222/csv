package com.example;
import com.example.model.CsvProduct;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
public class Main {
    public static void main(String[] args) throws IOException {
        String file1Path = "file1.csv";
        String file2Path = "file2.csv";
        List<CsvProduct> products = new ArrayList<>();
        products.addAll(parseCsv(file1Path));
        products.addAll(parseCsv(file2Path));
        Map<String, List<CsvProduct>> productsByShop = products.stream()
                .collect(Collectors.groupingBy(CsvProduct::getShop));
        for (Map.Entry<String, List<CsvProduct>> entry : productsByShop.entrySet()) {
            String shopName = entry.getKey();
            List<CsvProduct> aggregatedProducts = aggregateProducts(entry.getValue());
            saveToCsv(shopName + "_res.csv", aggregatedProducts);
        }
    }
    private static List<CsvProduct> parseCsv(String filePath) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath))) {
            return reader.lines()
                    .skip(1)
                    .map(Main::mapToProduct)
                    .collect(Collectors.toList());
        }
    }
    private static CsvProduct mapToProduct(String line) {
        String[] fields = line.split(";");
        return new CsvProduct(fields[0], fields[1], Double.parseDouble(fields[2]), Integer.parseInt(fields[3]));
    }
    private static List<CsvProduct> aggregateProducts(List<CsvProduct> products) {
        return products.stream()
                .collect(Collectors.groupingBy(CsvProduct::getName))
                .entrySet().stream()
                .map(entry -> {
                    String name = entry.getKey();
                    List<CsvProduct> productList = entry.getValue();
                    int totalQuantity = productList.stream().mapToInt(CsvProduct::getQuantity).sum();
                    double averagePrice = productList.stream().mapToDouble(CsvProduct::getPrice).average().orElse(0);
                    return new CsvProduct(name, productList.get(0).getShop(), averagePrice, totalQuantity);
                })
                .collect(Collectors.toList());
    }
    private static void saveToCsv(String filePath, List<CsvProduct> products) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath))) {
            writer.write("Name;Shop;Price;Quantity\n");
            for (CsvProduct product : products) {
                writer.write(product.toCsvRow());
                writer.newLine();
            }
        }
    }
}