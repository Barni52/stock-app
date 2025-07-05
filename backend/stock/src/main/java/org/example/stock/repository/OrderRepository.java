package org.example.stock.repository;

import org.example.stock.model.StockOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<StockOrder, Long> {
}
