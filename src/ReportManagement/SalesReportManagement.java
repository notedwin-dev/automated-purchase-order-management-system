/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ReportManagement;

import DailySalesManagement.Sales;
import InventoryManagement.TextFile;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author user
 */
public class SalesReportManagement {
    private List<Sales> salesList = new ArrayList<>();
    private static final String filePath = "src/DailySalesManagement/sales.txt";
    
    public void loadSalesFromFile(String yearMonth) {
        salesList.clear();
        List<String> lines = TextFile.readFile(filePath);

        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length >= 6) {
                String date = parts[0];
                if (date.startsWith(yearMonth)) { 
                    String itemCode = parts[1];
                    String itemName = parts[2];
                    int quantitySold = Integer.parseInt(parts[3]);
                    double retailPrice = Double.parseDouble(parts[4]);
                    double totalAmount = Double.parseDouble(parts[5]);

                    salesList.add(new Sales(date, itemCode, itemName, quantitySold, retailPrice, totalAmount));
                }
            }
        }
    }
    
    public int getTotalTransactions() {
        return salesList.size();
    }

    public int getTotalQuantitySold() {
        int total = 0;
        for (Sales s : salesList) {
            total += s.getQuantitySold();
        }
        return total;
    }

    public double getTotalSalesAmount() {
        double total = 0;
        for (Sales s : salesList) {
            total += s.getTotalAmount();
        }
        return total;
    }

    
    public List<String> getItemWiseSummary() {
        List<String> summary = new ArrayList<>();
        List<String> processed = new ArrayList<>();

        for (Sales s : salesList) {
            String key = s.getItemCode() + "|" + s.getItemName();
            if (!processed.contains(key)) {
                int qtySum = 0;
                double totalSum = 0;
                for (Sales x : salesList) {
                    if (x.getItemCode().equals(s.getItemCode())) {
                        qtySum += x.getQuantitySold();
                        totalSum += x.getTotalAmount();
                    }
                }
                summary.add(s.getItemCode() + "|" + s.getItemName() + "|" + qtySum + "|" + String.format("%.2f", totalSum));
                processed.add(key);
            }
        }
        return summary;
    }

}
