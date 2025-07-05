package org.example.stock.repository;

import org.example.stock.model.OwnedStock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnedStockRepository extends JpaRepository<OwnedStock, Long> {

}
