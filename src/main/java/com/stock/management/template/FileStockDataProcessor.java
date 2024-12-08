package com.stock.management.template;

import com.stock.management.models.FinanceStock;
import com.stock.management.models.Stock;
import com.stock.management.models.TechStock;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileStockDataProcessor extends StockDataTemplate {

    private final String filePath = "stocks.csv";
    @Override
    protected void connectToSource() {
        System.out.println("Opening stock data file...");
    }

    @Override
    protected List<Stock> fetchStockDetails() {
        System.out.println("Reading stock details from file...");

        List<Stock> stocks = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Skip header row
                if (line.startsWith("name,price,quantity")) {
                    continue;
                }
                String[] values = line.split(",");
                String name = values[0];
                double price = Double.parseDouble(values[1]);
                int quantity = Integer.parseInt(values[2]);
                String type = values[3];
                Stock stock;
                if(type.equals("tech")){
                    stock = new TechStock(name, price, quantity);
                }
                else{
                    stock = new FinanceStock(name, price, quantity);
                }
                stocks.add(stock);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to process CSV file: " + e.getMessage());
        }
        return stocks;

    }

}
