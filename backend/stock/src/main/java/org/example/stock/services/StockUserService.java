package org.example.stock.services;


import org.example.stock.Repositories.StockUserRepository;
import org.example.stock.models.StockUser;
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
