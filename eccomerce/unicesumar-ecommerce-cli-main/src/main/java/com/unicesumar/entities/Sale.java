package com.unicesumar.entities;

import java.util.List;
import java.util.UUID;

public class Sale {
    private String uuid;
    private User user;
    private List<Product> products;
    private String paymentMethod;
    private double total;

    public Sale(User user, List<Product> products, String paymentMethod) {
        this.user = user;
        this.products = products;
        this.paymentMethod = paymentMethod;
        this.total = calculateTotal();
    }

    private double calculateTotal() {
        return products.stream().mapToDouble(Product::getPrice).sum();
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public User getUser() {
        return user;
    }

    public List<Product> getProducts() {
        return products;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public double getTotal() {
        return total;
    }
}
