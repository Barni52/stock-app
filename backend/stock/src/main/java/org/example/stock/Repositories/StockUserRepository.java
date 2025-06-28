package org.example.stock.Repositories;

import org.example.stock.models.StockUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockUserRepository extends JpaRepository<StockUser, Long> {
    StockUser findByName(String name);
}
