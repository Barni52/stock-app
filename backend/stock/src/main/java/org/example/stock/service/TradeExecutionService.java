package org.example.stock.service;

import org.example.stock.model.OwnedStock;
import org.example.stock.model.Stock;
import org.example.stock.model.StockOrder;
import org.example.stock.model.StockUser;
import org.example.stock.repository.OrderRepository;
import org.example.stock.repository.OwnedStockRepository;
import org.example.stock.repository.StockRepository;
import org.example.stock.repository.StockUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TradeExecutionService {

    private final StockUserRepository stockUserRepository;
    private final OrderRepository orderRepository;
    private final OwnedStockRepository ownedStockRepository;

    public TradeExecutionService(StockUserRepository stockUserRepository, OrderRepository orderRepository, OwnedStockRepository ownedStockRepository) {
        this.stockUserRepository = stockUserRepository;
        this.orderRepository = orderRepository;
        this.ownedStockRepository = ownedStockRepository;
    }

    @Transactional
    public void executeTrades(){
        List<StockOrder> stockOrders = orderRepository.findAll();

        for(StockOrder order : stockOrders){
            checkIfFilled(order);
        }
    }

    private void checkIfFilled(StockOrder order){
        if(order.getType().equals("sell")){
            if(order.getStock().getCurrentPrice() >= order.getHitPrice()){
                makeSell(order);
            }

        } else if (order.getType().equals("buy")) {
            if(order.getStock().getCurrentPrice() <= order.getHitPrice()){
                makeBuy(order);
            }
        }
    }

    private void makeSell(StockOrder order){
        StockUser stockUser = order.getStockUser();
        Stock stock = order.getStock();

        stockUser.setBalance(stockUser.getBalance() + (order.getHitPrice() * order.getQuantity()));

        stockUserRepository.save(stockUser);

        orderRepository.delete(order);
    }

    private void makeBuy(StockOrder order){
        StockUser stockUser = order.getStockUser();
        Stock buyStock = order.getStock();

        for(OwnedStock ownedStock : stockUser.getOwnedStocks()){
            if(ownedStock.getStock().getTicker().equals(buyStock.getTicker())){
                ownedStock.setQuantity(ownedStock.getQuantity() + order.getQuantity());
                ownedStockRepository.save(ownedStock);
                orderRepository.delete(order);
                return;
            }
        }

        OwnedStock ownedStock = new OwnedStock();

        ownedStock.setStockUser(stockUser);
        ownedStock.setStock(buyStock);
        ownedStock.setQuantity(order.getQuantity());

        ownedStockRepository.save(ownedStock);
        orderRepository.delete(order);
    }
}
