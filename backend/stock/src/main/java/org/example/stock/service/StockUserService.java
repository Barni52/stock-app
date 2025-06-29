package org.example.stock.service;


import org.example.stock.repository.StockUserRepository;
import org.example.stock.model.StockUser;
import org.springframework.stereotype.Service;

@Service
public class StockUserService {

    private StockUserRepository stockUserRepository;

    public void addUser(String name, double money){
        StockUser stockUser = new StockUser();
        stockUser.setUsername(name);
        stockUser.setBalance(money);

        stockUserRepository.save(stockUser);
    }
}
