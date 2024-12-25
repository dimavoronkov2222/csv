package com.example;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
class CsvProcessorTest {
    @Test
    void mapToProduct_validLine_returnsProduct() {
        String line = "Product1;Shop1;100.0;5";
        CsvProcessor.Product product = CsvProcessor.mapToProduct(line);
        assertNotNull(product);
        assertEquals("Product1", product.getName());
        assertEquals("Shop1", product.getShop());
        assertEquals(100.0, product.getPrice());
        assertEquals(5, product.getQuantity());
    }
    @Test
    void aggregateProducts_correctAggregation() {
        List<CsvProcessor.Product> products = List.of(
                new CsvProcessor.Product("Product1", "Shop1", 100.0, 5),
                new CsvProcessor.Product("Product1", "Shop1", 120.0, 3),
                new CsvProcessor.Product("Product2", "Shop1", 80.0, 10)
        );
        List<CsvProcessor.Product> aggregated = CsvProcessor.aggregateProducts(products);
        assertEquals(2, aggregated.size());
        CsvProcessor.Product aggregatedProduct1 = aggregated.get(0);
        CsvProcessor.Product aggregatedProduct2 = aggregated.get(1);
        assertEquals("Product1", aggregatedProduct1.getName());
        assertEquals("Shop1", aggregatedProduct1.getShop());
        assertEquals(110.0, aggregatedProduct1.getPrice(), 0.01);
        assertEquals(8, aggregatedProduct1.getQuantity());
        assertEquals("Product2", aggregatedProduct2.getName());
        assertEquals("Shop1", aggregatedProduct2.getShop());
        assertEquals(80.0, aggregatedProduct2.getPrice(), 0.01);
        assertEquals(10, aggregatedProduct2.getQuantity());
    }
}