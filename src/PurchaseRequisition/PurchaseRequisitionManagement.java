/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PurchaseRequisition;

import InventoryManagement.Inventory;
import TextFile_Handler.TextFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author User
 */
public class PurchaseRequisitionManagement {
    private static final String ItemFile = "src/itemManagement/Items.txt"; 
    private static final String PRfile = "src/PurchaseRequisition/PR.txt" ;
    private static final Map<String, String> quantityMap = new HashMap<>();
    private final List<PurchaseRequisition> prlist;

    public PurchaseRequisitionManagement() {
        this.prlist = new ArrayList<>();
    }
    
    public List<PurchaseRequisition> getPRList() {
        return prlist;
    }
    
    public void getPRfromtxtfile() {
        this.prlist.clear(); 
        List<String> lines = TextFile.readFile(PRfile);

        for (String line : lines) {
            if (line.trim().startsWith("PR ID") || line.trim().isEmpty()) {
                continue;
            }

            String[] parts = line.split(",(?=(?:[^{}]*\\{[^{}]*\\})*[^{}]*$)"); 

            if (parts.length == 8) {
                String prID = parts[0].trim();
                String date = parts[1].trim();
                String prCreatedByName = parts[2].trim();
                String prCreatedByID = parts[3].trim();

                String[] itemCodes = parts[4].replace("{", "").replace("}", "").split(",");
                String[] quantities = parts[5].replace("{", "").replace("}", "").split(",");

                String expectedDate = parts[6].trim();
                String status = parts[7].trim();

                for (int i = 0; i < itemCodes.length && i < quantities.length; i++) {
                    String itemCode = itemCodes[i].trim();
                    try {
                        int quantity = Integer.parseInt(quantities[i].trim());

                        PurchaseRequisition pr = new PurchaseRequisition(
                            prID, date, prCreatedByName, prCreatedByID,
                            itemCode, quantity, expectedDate, status
                        );
                        prlist.add(pr);
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid quantity: " + quantities[i].trim() + " in line: " + line);
                    }
                }
            }
        }
    }

    public PurchaseRequisition findprid(String prid) {
        for (PurchaseRequisition pr : prlist) {
            if (pr.getPrid().equals(prid)) {
                return pr;
            }
        }
        return null;
    }
    
    public static String generatePRID() {
        List<String> lines = TextFile.readFile(PRfile);
        if (lines.isEmpty()) {
            return "PR001";
        }

        String lastLine = lines.get(lines.size() - 1);
        String[] parts = lastLine.split(",", -1);

        if (parts.length > 0 && parts[0].startsWith("PR")) {
            String lastID = parts[0];
            try {
                int num = Integer.parseInt(lastID.substring(3));
                return String.format("PR%03d", num + 1);
            } catch (NumberFormatException e) {
                // fallback if format is wrong
                return "PR001";
            }
        }
        return "PR001";
    }
    
    public static String getCurrentDate() {
        return java.time.LocalDate.now().toString();
    }
    
    
    public List<String> getItemCodeFromItem(){
        List<String> itemCode = new ArrayList<>();
        for(String line : TextFile.readFile(ItemFile)){
            String[] data = line.split(",\\s*");
            if(data.length >= 2){
                itemCode.add(data[1]);
            }
        }
        return itemCode;
    }
    
    public void savePR(List<PurchaseRequisition> prList) {
        if (prList == null || prList.isEmpty()) {
            System.out.println("No purchase requisitions to save.");
            return;
        }
        List<String> itemCodes = new ArrayList<>();
        List<String> quantities = new ArrayList<>();
        for (PurchaseRequisition pr : prList) {
            itemCodes.add(pr.getItemcode());
            quantities.add(String.valueOf(pr.getQuantity()));
        }
        String itemCodeStr = "{" + String.join(", ", itemCodes) + "}";
        String quantityStr = "{" + String.join(", ", quantities) + "}";
        PurchaseRequisition first = prList.get(0);
        String line = String.format("%s, %s, %s, %s, %s, %s, %s, %s",
                first.getPrid(),
                first.getDate(),
                first.getPrCreatedByName(),
                first.getPrCreatedByID(),
                itemCodeStr,
                quantityStr,
                first.getExdate(),
                first.getStatus()
        );
        TextFile.appendTo(PRfile, line);
    }
    
    public static List<PurchaseRequisition> getAllPR() {
        List<String> lines = TextFile.readFile(PRfile);
        List<PurchaseRequisition> prList = new ArrayList<>();

        for (String line : lines) {
            PurchaseRequisition pr = parseLineToPR(line);
            if (pr != null) {
                prList.add(pr);
            }
        }
        return prList;
    }
    
    public static String getOriginalQuantity(String prId) {
        return quantityMap.getOrDefault(prId, "");
    }
    
    private static PurchaseRequisition parseLineToPR(String line) {
        List<String> parts = new ArrayList<>();
        StringBuilder currentPart = new StringBuilder();
        int braceCount = 0;

        for (char c : line.toCharArray()) {
            if (c == '{') braceCount++;
            else if (c == '}') braceCount--;

            if (c == ',' && braceCount == 0) {
                parts.add(currentPart.toString().trim());
                currentPart = new StringBuilder();
            } else {
                currentPart.append(c);
            }
        }

        parts.add(currentPart.toString().trim());

        if (parts.size() < 8) {
            System.out.println("Invalid line (missing fields): " + line);
            return null;
        }

        String PR_ID = parts.get(0);
        String date = parts.get(1);
        String PRCreatedByName = parts.get(2);
        String PRCreatedByID = parts.get(3);
        String itemCode = parts.get(4);
        String quantityField = parts.get(5);
        String expectedDeliveryDate = parts.get(6);
        String status = parts.get(7);

        // Save full quantity string (remove curly braces only for display)
        String quantityStr = quantityField.replace("{", "").replace("}", "");
        quantityMap.put(PR_ID, quantityStr);

        // Parse only the first quantity as int
        int quantity = 0;
        try {
            if (quantityStr.contains(",")) {
                quantity = Integer.parseInt(quantityStr.split(",")[0].trim());
            } else {
                quantity = Integer.parseInt(quantityStr.trim());
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid quantity in line: " + line);
        }

        return new PurchaseRequisition(PR_ID, date, PRCreatedByName, PRCreatedByID,
                                       itemCode, quantity, expectedDeliveryDate, status);
    }
    
    public void updatePR(PurchaseRequisition updatedPR, String updatedQuantitiesStr) {
        List<String> lines = TextFile.readFile(PRfile);
        List<String> updatedLines = new ArrayList<>();
        boolean found = false;

        for (String line : lines) {
            // Extract PR_ID from the line (everything before first comma)
            String prIdInLine = "";
            int firstComma = line.indexOf(',');
            if (firstComma > 0) {
                prIdInLine = line.substring(0, firstComma).trim();
            }

            if (prIdInLine.equals(updatedPR.getPrid())) {
                // Build updated line with updatedPR data
                String updatedLine = String.format("%s, %s, %s, %s, %s, %s, %s, %s",
                    updatedPR.getPrid(),
                    updatedPR.getDate(),
                    updatedPR.getPrCreatedByName(),
                    updatedPR.getPrCreatedByID(),
                    updatedPR.getItemcode(),        // should be like "{item1, item2}"
                    updatedQuantitiesStr,           // should be like "{1, 2}"
                    updatedPR.getExdate(),
                    updatedPR.getStatus()
                );
                updatedLines.add(updatedLine);
                found = true;
            } else {
                updatedLines.add(line);
            }
        }

        if (!found) {
            // Optionally add new line if PR not found
            String newLine = String.format("%s, %s, %s, %s, %s, %s, %s, %s",
                    updatedPR.getPrid(),
                    updatedPR.getDate(),
                    updatedPR.getPrCreatedByName(),
                    updatedPR.getPrCreatedByID(),
                    updatedPR.getItemcode(),
                    updatedQuantitiesStr,
                    updatedPR.getExdate(),
                    updatedPR.getStatus()
            );
            updatedLines.add(newLine);
        }

        // Overwrite the file with updated lines
        TextFile.overwriteFile(PRfile, updatedLines);
    }

}
