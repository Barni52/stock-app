package org.example.stock.model;

import jakarta.persistence.*;

@Entity
public class StockOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Stock stock;
    @ManyToOne
    private StockUser stockUser;

    private Double quantity;

    private Double hitPrice;

    private String type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public StockUser getStockUser() {
        return stockUser;
    }

    public void setStockUser(StockUser stockUser) {
        this.stockUser = stockUser;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getHitPrice() {
        return hitPrice;
    }

    public void setHitPrice(Double hitPrice) {
        this.hitPrice = hitPrice;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
