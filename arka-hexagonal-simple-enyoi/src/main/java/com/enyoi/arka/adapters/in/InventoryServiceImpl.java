package com.enyoi.arka.adapters.in;

import com.enyoi.arka.domain.entities.Product;
import com.enyoi.arka.domain.entities.ProductCategory;
import com.enyoi.arka.domain.exception.ProductNotFoundException;
import com.enyoi.arka.domain.ports.in.InventoryService;
import com.enyoi.arka.domain.ports.out.NotificationService;
import com.enyoi.arka.domain.ports.out.ProductRepository;
import com.enyoi.arka.domain.valueobjects.Money;
import com.enyoi.arka.domain.valueobjects.ProductId;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class InventoryServiceImpl implements InventoryService {
    private final ProductRepository productRepository;
    private final NotificationService notificationService;
    private static final int STOCK_THRESHOLD = 10;

    public InventoryServiceImpl(ProductRepository productRepository, NotificationService notificationService) {
        this.productRepository = Objects.requireNonNull(productRepository);
        this.notificationService = Objects.requireNonNull(notificationService);
    }

    @Override
    public Product registerProduct(String name, String description, Money price, int stock, String category) {
        Product product = Product.builder()
                .id(ProductId.of(UUID.randomUUID().toString()))
                .name(name)
                .description(description)
                .price(price)
                .stock(stock)
                .category(ProductCategory.valueOf(category))
                .build();

        return productRepository.save(product);
    }

    @Override
    public Product getProductById(ProductId id) {
        return productRepository.findById(id).orElseThrow(
                () -> new ProductNotFoundException(id.value())
        );
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product updateStock(ProductId id, int newStock) {
        Product product = getProductById(id);
        Product updatedProduct = Product.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(newStock)
                .category(product.getCategory())
                .build();
        return productRepository.save(updatedProduct);
    }

    @Override
    public void reduceStock(ProductId id, int quantity) {
        Product product = getProductById(id);
        product.reduceStock(quantity);
        productRepository.save(product);
    }

    @Override
    public List<Product> getLowStockProducts() {
        return productRepository.findLowStockProducts(STOCK_THRESHOLD);
    }

    @Override
    public void generateRestockReport() {
        List<Product> products = getLowStockProducts();
        for (Product product : products) {
            notificationService.notifyLowStockAlert(product.getName(), product.getStock());
        }
    }
}
