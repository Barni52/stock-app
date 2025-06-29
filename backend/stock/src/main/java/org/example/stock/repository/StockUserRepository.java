package org.example.stock.repository;

import org.example.stock.model.StockUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockUserRepository extends JpaRepository<StockUser, Long> {
    Optional<StockUser> findByUsername(String name);
}
