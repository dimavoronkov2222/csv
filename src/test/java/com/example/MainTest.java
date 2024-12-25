package com.example;
import com.example.model.CsvProduct;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
class MainTest {
    @Test
    void mapToProduct_validLine_returnsCsvProduct() {
        String line = "Product1;Shop1;100.0;5";
        CsvProduct product = Main.mapToProduct(line);
        Assertions.assertNotNull(product);
        Assertions.assertEquals("Product1", product.getName());
        Assertions.assertEquals("Shop1", product.getShop());
        Assertions.assertEquals(100.0, product.getPrice());
        Assertions.assertEquals(5, product.getQuantity());
    }
    @Test
    void aggregateProducts_correctAggregation() {
        List<CsvProduct> products = List.of(
                new CsvProduct("Product1", "Shop1", 100.0, 5),
                new CsvProduct("Product1", "Shop1", 120.0, 3),
                new CsvProduct("Product2", "Shop1", 80.0, 10)
        );
        List<CsvProduct> aggregated = Main.aggregateProducts(products);
        Assertions.assertEquals(2, aggregated.size());
        CsvProduct aggregatedProduct1 = aggregated.get(0);
        CsvProduct aggregatedProduct2 = aggregated.get(1);
        Assertions.assertEquals("Product1", aggregatedProduct1.getName());
        Assertions.assertEquals("Shop1", aggregatedProduct1.getShop());
        Assertions.assertEquals(110.0, aggregatedProduct1.getPrice(), 0.01);
        Assertions.assertEquals(8, aggregatedProduct1.getQuantity());
        Assertions.assertEquals("Product2", aggregatedProduct2.getName());
        Assertions.assertEquals("Shop1", aggregatedProduct2.getShop());
        Assertions.assertEquals(80.0, aggregatedProduct2.getPrice(), 0.01);
        Assertions.assertEquals(10, aggregatedProduct2.getQuantity());
    }
    @Test
    void parseCsv_validFile_returnsProducts() throws IOException {
        String testCsvPath = "test.csv";
        Files.writeString(Paths.get(testCsvPath), "Name;Shop;Price;Quantity\nProduct1;Shop1;100.0;5");
        List<CsvProduct> products = Main.parseCsv(testCsvPath);
        Assertions.assertEquals(1, products.size());
        CsvProduct product = products.get(0);
        Assertions.assertEquals("Product1", product.getName());
        Assertions.assertEquals("Shop1", product.getShop());
        Assertions.assertEquals(100.0, product.getPrice());
        Assertions.assertEquals(5, product.getQuantity());
        Files.delete(Paths.get(testCsvPath));
    }
}