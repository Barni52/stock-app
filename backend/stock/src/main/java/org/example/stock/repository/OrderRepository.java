package org.example.stock.repository;

import org.example.stock.model.OwnedStock;
import org.example.stock.model.Stock;
import org.example.stock.model.StockOrder;
import org.example.stock.model.StockUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<StockOrder, Long> {

    List<StockOrder> findStockOrderByStockUser(StockUser stockUser);

}
