/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PurchaseRequisition;

import TextFile_Handler.TextFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author user
 */
public class PR_Management {
    private final List<PROperation>prlist;
    
    public PR_Management(){
        this.prlist = new ArrayList<>();
    }
    
    public void getPRfromtxtfile() {
        List<String> lines = TextFile.readFile("src/PurchaseRequisition/PR.txt");
        for (String line : lines) {
            String[] parts = line.split(",(?=(?:[^{}]*\\{[^{}]*\\})*[^{}]*$)"); // handle {a,b} fields correctly

            if (parts.length == 8) {
                String prID = parts[0].trim();
                String date = parts[1].trim();
                String smName = parts[2].trim();
                String smID = parts[3].trim();

                String[] itemCodes = parts[4].replace("{", "").replace("}", "").split(",");
                String[] quantities = parts[5].replace("{", "").replace("}", "").split(",");

                String itemCodeText = Arrays.stream(itemCodes).map(String::trim).collect(Collectors.joining("\n"));
                String quantityText = Arrays.stream(quantities).map(String::trim).collect(Collectors.joining("\n"));

                String expectedDate = parts[6].trim();
                String status = parts[7].trim();

                PROperation pr = new PROperation(prID, date, smName, smID, itemCodeText, quantityText, expectedDate, status);
                prlist.add(pr);
            }
        }
    }

//    PR0001, 25-3-2025, Simon, SM0001, {DY0001, DY002}, {10, 5}, 10-04-2025, PENDING
    
    public PROperation findprid(String prid) {
        for (PROperation pr : prlist) {
            if (pr.getPRID().equals(prid)) {
                return pr;
            }
        }
        return null;
    }
    
    public List<PROperation> getPrlist() {
        return prlist;
    }
    
   


}
