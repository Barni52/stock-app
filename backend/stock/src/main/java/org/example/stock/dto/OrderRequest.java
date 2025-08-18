package org.example.stock.dto;

public class OrderRequest {
    private String username;
    private String ticker;
    private Double quantity;
    private Double hitPrice;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
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
}
