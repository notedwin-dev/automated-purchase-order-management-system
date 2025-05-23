/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package itemmanagement;

import itemmanagement.ItemManagement;
import javax.swing.table.*;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


/**
 *
 * @author nixon
 */
public class MainPanel extends javax.swing.JFrame {

    private ItemManagement itemOps; // ----- This is a field ----- //
    private Map<String, String> supplierMap = new HashMap<>();


    public MainPanel() {
        initComponents();
         // - - - - - RESIZE ICON ADD - - - - - //
        ImageIcon addIcon = new ImageIcon(getClass().getResource("/resources/icons/Add.png"));
        Image scaled_add = addIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        ImageIcon resizedAdd = new ImageIcon(scaled_add);
        add_Button.setIcon(resizedAdd);
         // - - - - - RESIZE ICON DELETE - - - - - //
        ImageIcon deleteIcon = new ImageIcon(getClass().getResource("/resources/icons/Delete.png"));
        Image scaled_delete = deleteIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        ImageIcon resizedDelete = new ImageIcon(scaled_delete);
        delete_Button.setIcon(resizedDelete);
         // - - - - - RESIZE ICON UPDATE - - - - - //
        ImageIcon updateIcon = new ImageIcon(getClass().getResource("/resources/icons/Update.png"));
        Image scaled_update = updateIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        ImageIcon resizedUpdate = new ImageIcon(scaled_update);
        update_Button.setIcon(resizedUpdate);
         // - - - - - RESIZE ICON REFRESH - - - - - //
        ImageIcon refreshIcon = new ImageIcon(getClass().getResource("/resources/icons/Refresh.png"));
        Image scaled_refresh = refreshIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        ImageIcon resizedRefresh = new ImageIcon(scaled_refresh);
        refresh_Button.setIcon(resizedRefresh);
         // - - - - - RESIZE ICON CLEAN - - - - - //
        ImageIcon cleanIcon = new ImageIcon(getClass().getResource("/resources/icons/Clean.png"));
        Image scaled_clean = cleanIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        ImageIcon resizedClean = new ImageIcon(scaled_clean);
        clean_Button.setIcon(resizedClean);
         // - - - - - RESIZE ICON LOGO - - - - - //
        ImageIcon logoIcon = new ImageIcon(getClass().getResource("/resources/icons/Logo.png"));
        Image scaled_logo = logoIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        ImageIcon resizedLogo = new ImageIcon(scaled_logo);
        Logo_lbl.setIcon(resizedLogo);
        

        // - - - - - HIDES THE TABS - - - - - //
        JTabbedPane.setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI() {
            @Override
            protected int calculateTabAreaHeight(int tabPlacement, int horizRunCount, int maxTabHeight) {
                return 0;
            } 
            @Override
            protected void paintTab(Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex, Rectangle iconRect, Rectangle textRect) {
                //----- Do nothing: skip painting tabs -----//
            }
            @Override
            protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
                //----- Remove the tab panel border -----//
            }
        });
        
        // - - - - - FULL DISABLES INTERACTIONS - - - - - //
        viewItemTable.setRowSelectionAllowed(false);
        viewItemTable.setCellSelectionEnabled(false);
        viewItemTable.setFocusable(false);
        viewItemTable.setEnabled(false); 

        
         // - - - - - DISABLE CLICKING TABS - - - - - //
        JTabbedPane.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                evt.consume(); // prevent clicking on tabs
            }
        });
        
        // - - - - -  SET MODEL - - - - - //
        itemTable.setModel(ItemManagement.sharedModel);
        viewItemTable.setModel(ItemManagement.sharedModel);
 

        //----- Initialize ItemManagement with necessary components -----//
        itemOps = new ItemManagement(
            itemTable,
            itemName_textbox,
            itemCode_textbox,
            supplierID_comboBox,
            supplierName_textbox,
            unitPrice_textbox,
            retailPrice_textbox
        );
      
        //----- Double click the row to edit then put to textbox / comboBox -----//
        itemOps.doubleClickRow();       
        
        // - - - - - LOAD DATA INTO TABLE (for itemTable)- - - - - //
        itemOps.refresh();     
        
        // - - - - - DISABLE ROW SORTING - - - - - //
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(itemTable.getModel());
            itemTable.setRowSorter(sorter);
            for (int i = 0; i < itemTable.getColumnCount(); i++) {
                sorter.setSortable(i, false);
            }        
            
        loadSupplierData();
                
        // - - - - - Auto fill textbox when supplierID is selected - - - - - //
        supplierID_comboBox.addActionListener(e -> { //----- [ e -> {...} ] is a lambda expression -----//
            String selectedID = (String) supplierID_comboBox.getSelectedItem();
            if (selectedID != null && supplierMap.containsKey(selectedID)) {
                supplierName_textbox.setText(supplierMap.get(selectedID)); 
            } else {
                supplierName_textbox.setText(""); 
            }
        });

        
        //----- Non-editable comboBox to prevent changing data for supplierName -----//
        supplierName_textbox.setEditable(false);

        
        //----- Clear the comboBox when run the program -----//
        supplierID_comboBox.setSelectedIndex(-1);
        
    }
   

    // - - - - - Read supplier.txt - - - - - //
    private void loadSupplierData() {
        try (BufferedReader br = new BufferedReader(new FileReader("src/SupplierManagement/Suppliers.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    supplierMap.put(parts[0], parts[1]); // ID → Name
                    supplierID_comboBox.addItem(parts[0]); // Only IDs go in comboBox
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        Logo_lbl = new javax.swing.JLabel();
        itemEntry_Button = new javax.swing.JButton();
        viewItemList_Button = new javax.swing.JButton();
        viewItemList_Button1 = new javax.swing.JButton();
        JTabbedPane = new javax.swing.JTabbedPane();
        ItemEntry = new javax.swing.JPanel();
        itemName_lbl = new javax.swing.JLabel();
        retailPrice_lbl = new javax.swing.JLabel();
        supplierID_lbl = new javax.swing.JLabel();
        retailPrice_textbox = new javax.swing.JTextField();
        add_Button = new javax.swing.JButton();
        unitPrice_lbl = new javax.swing.JLabel();
        update_Button = new javax.swing.JButton();
        delete_Button = new javax.swing.JButton();
        itemCode_textbox = new javax.swing.JTextField();
        supplierName_lbl = new javax.swing.JLabel();
        refresh_Button = new javax.swing.JButton();
        supplierID_comboBox = new javax.swing.JComboBox<>();
        clean_Button = new javax.swing.JButton();
        unitPrice_textbox = new javax.swing.JTextField();
        itemTableScrollPane = new javax.swing.JScrollPane();
        itemTable = new javax.swing.JTable();
        itemName_textbox = new javax.swing.JTextField();
        itemCode_lbl = new javax.swing.JLabel();
        supplierName_textbox = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        itemTableScrollPane1 = new javax.swing.JScrollPane();
        viewItemTable = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 204));

        itemEntry_Button.setBackground(new java.awt.Color(255, 255, 204));
        itemEntry_Button.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        itemEntry_Button.setForeground(new java.awt.Color(0, 0, 0));
        itemEntry_Button.setText("Item Entry");
        itemEntry_Button.setBorder(null);
        itemEntry_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemEntry_ButtonActionPerformed(evt);
            }
        });

        viewItemList_Button.setBackground(new java.awt.Color(255, 255, 204));
        viewItemList_Button.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        viewItemList_Button.setForeground(new java.awt.Color(0, 0, 0));
        viewItemList_Button.setText("View Item List");
        viewItemList_Button.setBorder(null);
        viewItemList_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewItemList_ButtonActionPerformed(evt);
            }
        });

        viewItemList_Button1.setBackground(new java.awt.Color(255, 153, 153));
        viewItemList_Button1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        viewItemList_Button1.setForeground(new java.awt.Color(0, 0, 0));
        viewItemList_Button1.setText("Back");
        viewItemList_Button1.setBorder(null);
        viewItemList_Button1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewItemList_Button1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Logo_lbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(itemEntry_Button, javax.swing.GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(viewItemList_Button, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                    .addComponent(viewItemList_Button1, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(Logo_lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(132, 132, 132)
                .addComponent(itemEntry_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(viewItemList_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(viewItemList_Button1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        ItemEntry.setBackground(new java.awt.Color(255, 255, 255));

        itemName_lbl.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        itemName_lbl.setForeground(new java.awt.Color(0, 0, 0));
        itemName_lbl.setText("Item Name");

        retailPrice_lbl.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        retailPrice_lbl.setForeground(new java.awt.Color(0, 0, 0));
        retailPrice_lbl.setText("Retail Price");

        supplierID_lbl.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        supplierID_lbl.setForeground(new java.awt.Color(0, 0, 0));
        supplierID_lbl.setText("Supplier ID");

        add_Button.setBackground(new java.awt.Color(120, 211, 77));
        add_Button.setBorder(null);
        add_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                add_ButtonActionPerformed(evt);
            }
        });

        unitPrice_lbl.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        unitPrice_lbl.setForeground(new java.awt.Color(0, 0, 0));
        unitPrice_lbl.setText("Unit Price");

        update_Button.setBackground(new java.awt.Color(76, 134, 168));
        update_Button.setBorder(null);
        update_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                update_ButtonActionPerformed(evt);
            }
        });

        delete_Button.setBackground(new java.awt.Color(251, 82, 35));
        delete_Button.setBorder(null);
        delete_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delete_ButtonActionPerformed(evt);
            }
        });

        supplierName_lbl.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        supplierName_lbl.setForeground(new java.awt.Color(0, 0, 0));
        supplierName_lbl.setText("Supplier Name");

        refresh_Button.setBackground(new java.awt.Color(216, 212, 213));
        refresh_Button.setBorder(null);
        refresh_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refresh_ButtonActionPerformed(evt);
            }
        });

        supplierID_comboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supplierID_comboBoxActionPerformed(evt);
            }
        });

        clean_Button.setBackground(new java.awt.Color(240, 225, 0));
        clean_Button.setBorder(null);
        clean_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clean_ButtonActionPerformed(evt);
            }
        });

        itemTableScrollPane.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                itemTableScrollPaneMouseClicked(evt);
            }
        });

        itemTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No.", "Item Name", "Item Code", "Supplier ID", "Supplier Name", "Category", "Unit Price", "Retail Price", "Delivery Status"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        itemTable.getTableHeader().setReorderingAllowed(false);
        itemTableScrollPane.setViewportView(itemTable);
        if (itemTable.getColumnModel().getColumnCount() > 0) {
            itemTable.getColumnModel().getColumn(0).setMaxWidth(35);
        }

        itemName_textbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemName_textboxActionPerformed(evt);
            }
        });

        itemCode_lbl.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        itemCode_lbl.setForeground(new java.awt.Color(0, 0, 0));
        itemCode_lbl.setText("Item Code");

        javax.swing.GroupLayout ItemEntryLayout = new javax.swing.GroupLayout(ItemEntry);
        ItemEntry.setLayout(ItemEntryLayout);
        ItemEntryLayout.setHorizontalGroup(
            ItemEntryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ItemEntryLayout.createSequentialGroup()
                .addGroup(ItemEntryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ItemEntryLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(ItemEntryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(supplierName_lbl)
                            .addComponent(supplierID_lbl)
                            .addComponent(itemCode_lbl)
                            .addComponent(itemName_lbl))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(ItemEntryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(itemCode_textbox, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(supplierID_comboBox, javax.swing.GroupLayout.Alignment.TRAILING, 0, 113, Short.MAX_VALUE)
                            .addComponent(supplierName_textbox, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(itemName_textbox)))
                    .addGroup(ItemEntryLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(ItemEntryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(ItemEntryLayout.createSequentialGroup()
                                .addComponent(add_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(update_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(delete_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(refresh_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(clean_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(ItemEntryLayout.createSequentialGroup()
                                .addGroup(ItemEntryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(unitPrice_lbl)
                                    .addComponent(retailPrice_lbl))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(ItemEntryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(unitPrice_textbox)
                                    .addComponent(retailPrice_textbox, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE))))))
                .addGap(817, 817, 817))
            .addGroup(ItemEntryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(ItemEntryLayout.createSequentialGroup()
                    .addGap(266, 266, 266)
                    .addComponent(itemTableScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 777, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(33, Short.MAX_VALUE)))
        );
        ItemEntryLayout.setVerticalGroup(
            ItemEntryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ItemEntryLayout.createSequentialGroup()
                .addContainerGap(211, Short.MAX_VALUE)
                .addGroup(ItemEntryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(itemName_textbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(itemName_lbl))
                .addGap(24, 24, 24)
                .addGroup(ItemEntryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(itemCode_textbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(itemCode_lbl))
                .addGap(18, 18, 18)
                .addGroup(ItemEntryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(supplierID_lbl)
                    .addComponent(supplierID_comboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(ItemEntryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(supplierName_lbl)
                    .addComponent(supplierName_textbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(ItemEntryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(unitPrice_textbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(unitPrice_lbl))
                .addGap(18, 18, 18)
                .addGroup(ItemEntryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(retailPrice_textbox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(retailPrice_lbl))
                .addGap(83, 83, 83)
                .addGroup(ItemEntryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ItemEntryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(add_Button, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(refresh_Button, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(delete_Button, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(update_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(clean_Button, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(148, 148, 148))
            .addGroup(ItemEntryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(ItemEntryLayout.createSequentialGroup()
                    .addGap(39, 39, 39)
                    .addComponent(itemTableScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(60, Short.MAX_VALUE)))
        );

        JTabbedPane.addTab("Item Entry", ItemEntry);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        itemTableScrollPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                itemTableScrollPane1MouseClicked(evt);
            }
        });
        viewItemTable = new javax.swing.JTable() {
            public boolean isCellEditable(int row, int column) {
                return false; // makes it uneditable even if model allows it
            }
        };

        viewItemTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No.", "Item Name", "Item Code", "Supplier ID", "Supplier Name", "Category", "Unit Price", "Retail Price", "Delivery Status"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        viewItemTable.getTableHeader().setReorderingAllowed(false);
        itemTableScrollPane1.setViewportView(viewItemTable);
        if (viewItemTable.getColumnModel().getColumnCount() > 0) {
            viewItemTable.getColumnModel().getColumn(0).setMaxWidth(35);
        }

        jLabel2.setFont(new java.awt.Font("Arial", 1, 48)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Item List");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(69, Short.MAX_VALUE)
                .addComponent(itemTableScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 934, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(73, 73, 73))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(431, 431, 431)
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addComponent(itemTableScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 548, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(71, 71, 71))
        );

        JTabbedPane.addTab("View Item List", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(JTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 1076, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(JTabbedPane)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void itemTableScrollPane1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_itemTableScrollPane1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_itemTableScrollPane1MouseClicked

    private void itemName_textboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemName_textboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_itemName_textboxActionPerformed

    private void itemTableScrollPaneMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_itemTableScrollPaneMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_itemTableScrollPaneMouseClicked

     // - - - - - - - - - - CLEAN FUNCTION - - - - - - - - - - //
    private void clean_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clean_ButtonActionPerformed
        itemOps.clean();
    }//GEN-LAST:event_clean_ButtonActionPerformed

    private void supplierID_comboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supplierID_comboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_supplierID_comboBoxActionPerformed

     // - - - - - - - - - - REFRESH FUNCTION - - - - - - - - - - //
    private void refresh_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refresh_ButtonActionPerformed
        itemOps.refresh();
    }//GEN-LAST:event_refresh_ButtonActionPerformed

     // - - - - - - - - - - DELETE FUNCTION - - - - - - - - - - //
    private void delete_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delete_ButtonActionPerformed
        itemOps.delete();
    }//GEN-LAST:event_delete_ButtonActionPerformed

     // - - - - - - - - - - UPDATE FUNCTION - - - - - - - - - - //
    private void update_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_update_ButtonActionPerformed
        itemOps.update();
    }//GEN-LAST:event_update_ButtonActionPerformed

     // - - - - - - - - - - ADD FUNCTION - - - - - - - - - - //
    private void add_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_add_ButtonActionPerformed
        itemOps.add();
    }//GEN-LAST:event_add_ButtonActionPerformed

    private void viewItemList_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewItemList_ButtonActionPerformed
        // - - - - - I created this just to test whether it's working or not (can delete once linked) - - - - - //
        JFrame frame = new JFrame("Item List Viewer");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1080, 700);
        frame.add(new ViewItemList());
        frame.setVisible(true);
    }//GEN-LAST:event_viewItemList_ButtonActionPerformed

    private void itemEntry_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemEntry_ButtonActionPerformed
        JTabbedPane.setSelectedIndex(0);
    }//GEN-LAST:event_itemEntry_ButtonActionPerformed

    private void viewItemList_Button1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewItemList_Button1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_viewItemList_Button1ActionPerformed





// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -  ENDS HERE  - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -  //
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
            java.util.logging.Logger.getLogger(MainPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainPanel().setVisible(true);
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ItemEntry;
    private javax.swing.JTabbedPane JTabbedPane;
    private javax.swing.JLabel Logo_lbl;
    private javax.swing.JButton add_Button;
    private javax.swing.JButton clean_Button;
    private javax.swing.JButton delete_Button;
    private javax.swing.JLabel itemCode_lbl;
    private javax.swing.JTextField itemCode_textbox;
    private javax.swing.JButton itemEntry_Button;
    private javax.swing.JLabel itemName_lbl;
    private javax.swing.JTextField itemName_textbox;
    private javax.swing.JTable itemTable;
    private javax.swing.JScrollPane itemTableScrollPane;
    private javax.swing.JScrollPane itemTableScrollPane1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JButton refresh_Button;
    private javax.swing.JLabel retailPrice_lbl;
    private javax.swing.JTextField retailPrice_textbox;
    private javax.swing.JComboBox<String> supplierID_comboBox;
    private javax.swing.JLabel supplierID_lbl;
    private javax.swing.JLabel supplierName_lbl;
    private javax.swing.JTextField supplierName_textbox;
    private javax.swing.JLabel unitPrice_lbl;
    private javax.swing.JTextField unitPrice_textbox;
    private javax.swing.JButton update_Button;
    private javax.swing.JButton viewItemList_Button;
    private javax.swing.JButton viewItemList_Button1;
    private javax.swing.JTable viewItemTable;
    // End of variables declaration//GEN-END:variables
}
