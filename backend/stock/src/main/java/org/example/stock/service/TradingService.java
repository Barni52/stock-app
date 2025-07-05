package org.example.stock.service;

import jakarta.transaction.Transactional;
import org.example.stock.exception.*;
import org.example.stock.model.StockOrder;
import org.example.stock.model.OwnedStock;
import org.example.stock.model.Stock;
import org.example.stock.model.StockUser;
import org.example.stock.repository.OrderRepository;
import org.example.stock.repository.OwnedStockRepository;
import org.example.stock.repository.StockRepository;
import org.example.stock.repository.StockUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class TradingService {

    private final StockRepository stockRepository;
    private final StockUserRepository stockUserRepository;
    private final OrderRepository orderRepository;
    private final OwnedStockRepository ownedStockRepository;

    public TradingService(StockRepository stockRepository, StockUserRepository stockUserRepository, OrderRepository orderRepository, OwnedStockRepository ownedStockRepository) {
        this.stockRepository = stockRepository;
        this.stockUserRepository = stockUserRepository;
        this.orderRepository = orderRepository;
        this.ownedStockRepository = ownedStockRepository;
    }


    public StockOrder makeSellOrder(
            String username,
            String ticker,
            Double quantity,
            Double hitPrice
    ) throws IncorrectInputException, UserNotFoundException, StockNotFoundException, InsufficientStockException {

        if (quantity == null || quantity <= 0 || hitPrice == null || hitPrice <= 0) {
            throw new IncorrectInputException("Wrong price or quantity");
        }

        StockUser stockUser;
        Stock stock;
        Optional<StockUser> stockUserOptional = stockUserRepository.findByUsername(username);
        if(stockUserOptional.isPresent()){
            stockUser = stockUserOptional.get();
        } else{
            throw new UserNotFoundException("No user with this username");
        }

        Optional<Stock> stockOptional = stockRepository.findById(ticker);
        if(stockOptional.isPresent()){
            stock = stockOptional.get();
        } else{
            throw new StockNotFoundException("No user with this username");
        }


        List<OwnedStock> ownedStocks = stockUser.getOwnedStocks();

        OwnedStock sellStock = null;

        for(OwnedStock ownedStock : ownedStocks){
            if(ownedStock.getStock().getTicker().equals(ticker)){
                sellStock = ownedStock;
            }
        }

        if(sellStock == null || sellStock.getQuantity() < quantity){
            throw new InsufficientStockException("Not enough stock");
        }



        StockOrder stockOrder = new StockOrder();

        stockOrder.setHitPrice(hitPrice);
        stockOrder.setQuantity(quantity);
        stockOrder.setStock(stock);
        stockOrder.setType("sell");
        stockOrder.setStockUser(stockUser);

        orderRepository.save(stockOrder);

        sellStock.setQuantity(sellStock.getQuantity() - quantity);
        ownedStockRepository.save(sellStock);

        if (sellStock.getQuantity() == 0) {
            stockUser.getOwnedStocks().remove(sellStock);
            ownedStockRepository.delete(sellStock);
        }
        stockUserRepository.save(stockUser);

        return stockOrder;
    }

    public StockOrder makeBuyOrder(
            String username,
            String ticker,
            Double quantity,
            Double hitPrice
    ) throws IncorrectInputException, UserNotFoundException, StockNotFoundException, InsufficientFundException {

        if (quantity == null || quantity <= 0 || hitPrice == null || hitPrice <= 0) {
            throw new IncorrectInputException("Wrong price or quantity");
        }

        StockUser stockUser;
        Stock stock;
        Optional<StockUser> stockUserOptional = stockUserRepository.findByUsername(username);
        if(stockUserOptional.isPresent()){
            stockUser = stockUserOptional.get();
        } else{
            throw new UserNotFoundException("No user with this username");
        }

        Optional<Stock> stockOptional = stockRepository.findById(ticker);
        if(stockOptional.isPresent()){
            stock = stockOptional.get();
        } else{
            throw new StockNotFoundException("No user with this username");
        }


        if(stockUser.getBalance() < hitPrice * quantity){
            throw new InsufficientFundException("Not enough money");
        }



        StockOrder stockOrder = new StockOrder();

        stockOrder.setHitPrice(hitPrice);
        stockOrder.setQuantity(quantity);
        stockOrder.setStock(stock);
        stockOrder.setType("buy");
        stockOrder.setStockUser(stockUser);

        orderRepository.save(stockOrder);

        stockUser.setBalance(stockUser.getBalance() - (quantity * hitPrice));

        stockUserRepository.save(stockUser);

        return stockOrder;
    }
}
