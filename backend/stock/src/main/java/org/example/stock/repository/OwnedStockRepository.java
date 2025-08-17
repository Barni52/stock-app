package org.example.stock.repository;

import org.example.stock.model.OwnedStock;
import org.example.stock.model.StockUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OwnedStockRepository extends JpaRepository<OwnedStock, Long> {
    List<OwnedStock> findOwnedStocksByStockUser(StockUser stockUser);
}
