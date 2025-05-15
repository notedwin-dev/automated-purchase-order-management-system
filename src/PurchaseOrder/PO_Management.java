/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PurchaseOrder;

import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.*;

public class PO_Management {
    private final List<Object[]> tableData = new ArrayList<>();

    // ========== Read the PR_List text file ========== //
    public PO_Management() {
        readDataFromPR_List("src/PurchaseRequisition/PR.txt");
    }

    private void readDataFromPR_List(String path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            boolean isFirstLine = true;
            int rowNo = 1;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] data = line.split(",(?=(?:[^{}]*\\{[^{}]*\\})*[^{}]*$)");
                if (data.length == 8) {
                    String prID = data[0].trim();
                    String date = data[1].trim();
                    String smName = data[2].trim();
                    String smID = data[3].trim();
                    String[] itemCodes = data[4].replace("{", "").replace("}", "").split(",");
                    String[] quantities = data[5].replace("{", "").replace("}", "").split(",");
                    String expectedDate = data[6].trim();
                    String status = data[7].trim();

                    String itemCodeText = Arrays.stream(itemCodes).map(String::trim).collect(Collectors.joining("\n"));
                    String quantityText = Arrays.stream(quantities).map(String::trim).collect(Collectors.joining("\n"));

                    tableData.add(new Object[]{
                        rowNo++, prID, date, smName, smID, itemCodeText, quantityText, expectedDate, status
                    });
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    public List<Object[]> getTableData() {
        return tableData;
    }


}
