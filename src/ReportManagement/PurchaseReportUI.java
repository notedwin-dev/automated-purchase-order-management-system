/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ReportManagement;

/**
 *
 * @author nixon
 */
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Date;
import javax.swing.table.TableRowSorter;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import javax.swing.table.TableModel;
import java.io.File;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.font.PDFont;


public class PurchaseReportUI extends javax.swing.JFrame {

    private DefaultTableModel tModel;
    private PurchaseReport_Management report_manage;

    public PurchaseReportUI() {
        initComponents();

        tModel = new DefaultTableModel(new Object[]{
            "Payment ID", "PO ID", "Item Code", "Item Name", "Company", "Supplier ID", "Quantity", "Unit Price", "Total Amount", "Payment Date"}, 0) {
                
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // make all cells non-editable
            }
        };

        PurchaseReportTable.setModel(tModel);
        report_manage = new PurchaseReport_Management();
        
        // - - - - - ADD ACTION LISTENER AND CALL METHOD TO PASS SELECTED DATE - - - - - //
        filterDate.getDateEditor().addPropertyChangeListener(evt -> {
            if ("date".equals(evt.getPropertyName())) {
                Date selectedDate = filterDate.getDate(); // java.util.Date
                filterTableByWeek(selectedDate);
            }
        });
        
        // - - - - - DISABLE EDITABLE FUNCTIONS - - - - - //
        JTextField editor = (JTextField) filterDate.getDateEditor().getUiComponent();
            editor.setEditable(false);
            editor.setFocusable(false);
            editor.setCursor(null); // Optional

            editor.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    e.consume();
                }
                @Override
                public void mouseReleased(MouseEvent e) {
                    e.consume();
                }
            });
            
        refreshTable();
        setColumnWidth();
        
//        print_Button.addActionListener(e -> exportTableToPDF());

    }

    
    // ========== APPLY CUSTOM RENDERER ========== //
    private void applyCustomRenderer() {
        PurchaseReportTable.getColumnModel().getColumn(2).setCellRenderer(new MultiLineRenderer()); // Item Code
        PurchaseReportTable.getColumnModel().getColumn(3).setCellRenderer(new MultiLineRenderer()); // Item Name
        PurchaseReportTable.getColumnModel().getColumn(4).setCellRenderer(new MultiLineRenderer()); // Company
        PurchaseReportTable.getColumnModel().getColumn(5).setCellRenderer(new MultiLineRenderer()); // Supplier ID
        PurchaseReportTable.getColumnModel().getColumn(6).setCellRenderer(new MultiLineRenderer()); // Quantity
        PurchaseReportTable.getColumnModel().getColumn(7).setCellRenderer(new MultiLineRenderer()); // Unit Price
    }

    public class MultiLineRenderer extends JTextArea implements TableCellRenderer {
        public MultiLineRenderer() {
            setLineWrap(true);
            setWrapStyleWord(true);
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            setText(value == null ? "" : value.toString().trim());
            setFont(table.getFont());

            // Adjust row height based on content
            int lines = (value == null ? 1 : value.toString().split("\n").length);
            int lineHeight = getFontMetrics(getFont()).getHeight();
            int requiredHeight = lineHeight * lines;

            if (table.getRowHeight(row) < requiredHeight) {
                table.setRowHeight(row, requiredHeight);
            }

            // Maintain selection color behavior
            setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());

            return this;
        }
    }

    
     // ========== DISPLAY DATA ON TABLE ========== //
    private void refreshTable() {
        tModel.setRowCount(0); // Clear table
        
        List<Object[]> data = report_manage.getTableData();
        System.out.println("Data size: " + data.size());
        
        for (Object[] row : report_manage.getTableData()) {
            System.out.println(Arrays.toString(row));
            tModel.addRow(row);
        }
        
        applyCustomRenderer();
    }
    
    
    // ========== SET COLUMN WIDTH ========== //
    private void setColumnWidth() {
        int colCount = PurchaseReportTable.getColumnCount();
        if (colCount >= 10) {
            PurchaseReportTable.getColumnModel().getColumn(0).setPreferredWidth(60);  // Payment ID
            PurchaseReportTable.getColumnModel().getColumn(1).setPreferredWidth(50);  // PO ID
            PurchaseReportTable.getColumnModel().getColumn(2).setPreferredWidth(60); // Item Code
            PurchaseReportTable.getColumnModel().getColumn(3).setPreferredWidth(100);  // Item Name
            PurchaseReportTable.getColumnModel().getColumn(4).setPreferredWidth(140); // Company
            PurchaseReportTable.getColumnModel().getColumn(5).setPreferredWidth(90);  // Payment Date
            PurchaseReportTable.getColumnModel().getColumn(6).setPreferredWidth(60);  // Supplier ID
            PurchaseReportTable.getColumnModel().getColumn(7).setPreferredWidth(60);  // Quantity
            PurchaseReportTable.getColumnModel().getColumn(8).setPreferredWidth(60);  // Unit Price
            PurchaseReportTable.getColumnModel().getColumn(9).setPreferredWidth(80);  // Total Amount
        } else {
            System.out.println("❌ Column count mismatch. Expected 10, got " + colCount);
        }
    }

    
    // ========== METHOD FOR DISPLAYING WEEKLY DATA FROM DATECHOOSER ========== //
    private void filterTableByWeek(Date selectedDate) {
        if (selectedDate == null) return;

        // Convert selectedDate to LocalDate
        LocalDate selectedLocalDate = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        // Get start and end of the week (Monday to Sunday)
        LocalDate weekStart = selectedLocalDate.with(DayOfWeek.MONDAY);
        LocalDate weekEnd = selectedLocalDate.with(DayOfWeek.SUNDAY);

        // Format to match your table format (adjust if needed)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        TableRowSorter<TableModel> sorter = new TableRowSorter<>(PurchaseReportTable.getModel());
        sorter.setRowFilter(new RowFilter<TableModel, Integer>() {
            @Override
            public boolean include(Entry<? extends TableModel, ? extends Integer> entry) {
                int dateColumnIndex = PurchaseReportTable.getColumnModel().getColumnIndex("Payment Date");
                String dateValue = entry.getStringValue(dateColumnIndex);

                try {
                    LocalDate rowDate = LocalDate.parse(dateValue, formatter);
                    return (!rowDate.isBefore(weekStart) && !rowDate.isAfter(weekEnd));
                } catch (DateTimeParseException e) {
                    return false;
                }
            }
        });

        PurchaseReportTable.setRowSorter(sorter);
        applyCustomRenderer();
        }

    // ========== EXPORT PDF METHOD ========== //
//    private void exportTableToPDF() {
//        PDDocument document = new PDDocument();
//        PDPage page = new PDPage(PDRectangle.A4);
//        document.addPage(page);
//
//        try {
//            PDPageContentStream contentStream = new PDPageContentStream(document, page);
//            PDFont font = PDType1Font.HELVETICA;
//
//            float margin = 50;
//            float yStart = page.getMediaBox().getHeight() - margin;
//            float yPosition = yStart;
//            float rowHeight = 20;
//            float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
//
//            TableModel model = PurchaseReportTable.getModel();
//            int cols = model.getColumnCount();
//            float colWidth = tableWidth / cols;
//
//            contentStream.setFont(font, 12);
//            contentStream.beginText();
//            contentStream.newLineAtOffset(margin, yPosition);
//            contentStream.showText("Weekly Purchase Report");
//            contentStream.endText();
//
//            yPosition -= rowHeight * 2;
//
//            // Header
//            for (int col = 0; col < cols; col++) {
//                String header = model.getColumnName(col);
//                drawText(contentStream, font, 10, margin + col * colWidth, yPosition, header);
//            }
//
//            yPosition -= rowHeight;
//
//            // Table rows
//            for (int row = 0; row < PurchaseReportTable.getRowCount(); row++) {
//                for (int col = 0; col < cols; col++) {
//                    Object value = PurchaseReportTable.getValueAt(row, col);
//                    drawText(contentStream, font, 10, margin + col * colWidth, yPosition, String.valueOf(value));
//                }
//                yPosition -= rowHeight;
//
//                if (yPosition < margin) {
//                    contentStream.close();
//                    page = new PDPage(PDRectangle.A4);
//                    document.addPage(page);
//                    contentStream = new PDPageContentStream(document, page);
//                    yPosition = yStart;
//                }
//            }
//
//            contentStream.close();
//            String fileName = "WeeklyPurchaseReport.pdf";
//            document.save(new File(fileName));
//            document.close();
//
//            JOptionPane.showMessageDialog(this, "Report exported to PDF:\n" + fileName);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            JOptionPane.showMessageDialog(this, "Failed to export PDF: " + e.getMessage());
//        }
//    }
//
//    private void drawText(PDPageContentStream stream, PDFont font, int fontSize, float x, float y, String text) throws IOException {
//        stream.beginText();
//        stream.setFont(font, fontSize);
//        stream.newLineAtOffset(x, y);
//        stream.showText(text);
//        stream.endText();
//    }




// ========================================================================================//
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        PurchaseReportTable = new javax.swing.JTable();
        print_Button = new javax.swing.JButton();
        filterDate = new com.toedter.calendar.JDateChooser();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Arial", 1, 48)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Purchase Report");

        PurchaseReportTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Payment ID", "PO ID", "Item Code", "Item Name", "Company", "Payment Date", "Supplier ID", "Quantity", "Unit Price", "Total Amount"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(PurchaseReportTable);

        print_Button.setBackground(new java.awt.Color(153, 204, 255));
        print_Button.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        print_Button.setForeground(new java.awt.Color(0, 0, 0));
        print_Button.setText("PRINT");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(340, 340, 340)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(filterDate, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(80, 80, 80)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 915, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(85, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(print_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(483, 483, 483))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(filterDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 476, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(print_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(42, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PurchaseReportUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PurchaseReportUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PurchaseReportUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PurchaseReportUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PurchaseReportUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable PurchaseReportTable;
    private com.toedter.calendar.JDateChooser filterDate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton print_Button;
    // End of variables declaration//GEN-END:variables
}
