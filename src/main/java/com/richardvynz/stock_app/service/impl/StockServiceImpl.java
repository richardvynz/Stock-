package com.richardvynz.stock_app.service.impl;

import com.richardvynz.stock_app.dto.StockRequestDTO;
import com.richardvynz.stock_app.dto.StockResponseDTO;
import com.richardvynz.stock_app.entity.Stock;
import com.richardvynz.stock_app.exception.StockNotFoundException;
import com.richardvynz.stock_app.service.StockService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class StockServiceImpl implements StockService {
    private final List<Stock> stocks = new ArrayList<>();
    private final AtomicLong nextId = new AtomicLong(1);

    public StockServiceImpl() {
        Stock stock1 = new Stock(nextId.getAndIncrement(), "Apple Inc.", 150.0, LocalDateTime.now(), LocalDateTime.now());
        Stock stock2 = new Stock(nextId.getAndIncrement(), "Microsoft Corporation", 300.0, LocalDateTime.now(), LocalDateTime.now());
        stocks.add(stock1);
        stocks.add(stock2);
    }

    @Override
    public Flux<StockResponseDTO> getAllStocks() {
        return Flux.fromIterable(stocks).map(this::toDto);
    }

    @Override
    public Mono<StockResponseDTO> getStockById(Long id) {
        return Mono.justOrEmpty(stocks.stream()
                        .filter(stock -> stock.getId().equals(id))
                        .findFirst())
                .map(this::toDto)
                .switchIfEmpty(Mono.error(new StockNotFoundException("Stock not found")));
    }

    @Override
    public Mono<StockResponseDTO> updateStockPrice(Long id, double newPrice) {
        return Mono.fromCallable(() -> {
            for (Stock stock : stocks) {
                if (stock.getId().equals(id)) {
                    stock.setCurrentPrice(newPrice);
                    stock.setLastUpdate(LocalDateTime.now());
                    return stock;
                }
            }
            throw new StockNotFoundException("Stock not found");
        }).map(this::toDto);
    }

    @Override
    public Mono<StockResponseDTO> createStock(StockRequestDTO stockRequestDTO) {
        return Mono.fromCallable(() -> {
            Stock stock = new Stock();
            stock.setId(nextId.getAndIncrement());
            stock.setName(stockRequestDTO.getName());
            stock.setCurrentPrice(stockRequestDTO.getCurrentPrice());
            stock.setCreateDate(LocalDateTime.now());
            stock.setLastUpdate(LocalDateTime.now());
            stocks.add(stock);
            return stock;
        }).map(this::toDto);
    }

    private StockResponseDTO toDto(Stock stock) {
        StockResponseDTO dto = new StockResponseDTO();
        dto.setId(stock.getId());
        dto.setName(stock.getName());
        dto.setCurrentPrice(stock.getCurrentPrice());
        dto.setCreateDate(stock.getCreateDate());
        dto.setLastUpdate(stock.getLastUpdate());
        return dto;
    }
}
