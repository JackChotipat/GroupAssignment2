/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UserInterface.Main.WorkSpaceProfiles.OrderManagement;

import TheBusiness.Business.Business;
import TheBusiness.Business.Simulation;
import TheBusiness.ProductManagement.Product;
import TheBusiness.Supplier.Supplier;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import TheBusiness.Business.ProductPerformanceReport;


/**
 * Task 4: Run Simulation Panel
 * Modified to display Margin Rate (per TA feedback)
 * 
 * @author 123
 * @date 2025-11-11
 */
public class RunSimulationPanel extends JPanel {
    private Business business;
    private Simulation simulation;
    private JPanel cardSequencePanel;
    
    // UI Components
    private JTable productTable;
    private DefaultTableModel tableModel;
    private JButton runSimulationButton;
    private JButton applyChangesButton;
    private JButton resetButton;
    private JButton backButton;
    
    // Results display
    private JLabel beforeRevenueLabel;
    private JLabel afterRevenueLabel;
    private JLabel revenueChangeLabel;
    private JLabel beforeProfitLabel;
    private JLabel afterProfitLabel;
    private JLabel profitChangeLabel;
    // 🔧 NEW: Margin Rate labels
    private JLabel beforeMarginRateLabel;
    private JLabel afterMarginRateLabel;
    private JLabel marginRateChangeLabel;
    private JLabel recommendationLabel;
    private ProductPerformanceReport performanceReport;
    // Store adjusted prices
    private HashMap<Product, Integer> adjustedPrices;
    
    public RunSimulationPanel(Business b, JPanel cardPanel) {
        super();
        this.performanceReport = new ProductPerformanceReport(business);
        this.business = b;
        this.cardSequencePanel = cardPanel;
        this.simulation = new Simulation(business);
        this.adjustedPrices = new HashMap<>();
        
        initComponents();
        loadProductData();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(0, 153, 153));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title Panel
        JPanel titlePanel = createTitlePanel();
        add(titlePanel, BorderLayout.NORTH);
        
        // Center Panel (Product Table)
        JPanel centerPanel = createCenterPanel();
        add(centerPanel, BorderLayout.CENTER);
        
        // Results Panel
        JPanel resultsPanel = createResultsPanel();
        add(resultsPanel, BorderLayout.EAST);
        
        // Button Panel
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createTitlePanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(0, 153, 153));
        
        JLabel titleLabel = new JLabel("Task 4: Run Simulation");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        panel.add(titleLabel);
        
        return panel;
    }
    
    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY, 2),
            "Product Price Adjustments",
            0, 0,
            new Font("Arial", Font.BOLD, 16)
        ));
        
        // Instructions
        JLabel instructionLabel = new JLabel(
            "<html><b>Instructions:</b> Enter new target prices in the 'New Target' column, then click 'Run Simulation'</html>"
        );
        instructionLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(instructionLabel, BorderLayout.NORTH);
        
        // Table
        String[] columns = {"Product Name", "Current Target", "New Target", "Sales Above", "Sales Below"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2; // Only "New Target" column is editable
            }
        };
        
        productTable = new JTable(tableModel);
        productTable.setFont(new Font("Arial", Font.PLAIN, 14));
        productTable.setRowHeight(30);
        productTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        productTable.getTableHeader().setBackground(new Color(0, 153, 153));
        productTable.getTableHeader().setForeground(Color.WHITE);
        
        // Set column widths
        productTable.getColumnModel().getColumn(0).setPreferredWidth(200);
        productTable.getColumnModel().getColumn(1).setPreferredWidth(120);
        productTable.getColumnModel().getColumn(2).setPreferredWidth(120);
        productTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        productTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        
        JScrollPane scrollPane = new JScrollPane(productTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    // 🔧 MODIFIED: Added Margin Rate section
    private JPanel createResultsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY, 2),
            "Simulation Results",
            0, 0,
            new Font("Arial", Font.BOLD, 16)
        ));
        panel.setPreferredSize(new Dimension(350, 0));
        
        // Revenue Section
        panel.add(createSectionLabel("REVENUE:"));
        beforeRevenueLabel = createResultLabel("Before: $0");
        afterRevenueLabel = createResultLabel("After: $0");
        revenueChangeLabel = createResultLabel("Change: $0");
        panel.add(beforeRevenueLabel);
        panel.add(afterRevenueLabel);
        panel.add(revenueChangeLabel);
        panel.add(Box.createVerticalStrut(20));
        
        // Profit Section
        panel.add(createSectionLabel("PROFIT:"));
        beforeProfitLabel = createResultLabel("Before: $0");
        afterProfitLabel = createResultLabel("After: $0");
        profitChangeLabel = createResultLabel("Change: $0");
        panel.add(beforeProfitLabel);
        panel.add(afterProfitLabel);
        panel.add(profitChangeLabel);
        panel.add(Box.createVerticalStrut(20));
        
        // 🔧 NEW: Margin Rate Section (KEY METRIC)
        panel.add(createSectionLabel("MARGIN RATE:"));
        beforeMarginRateLabel = createResultLabel("Before: 0.00%");
        afterMarginRateLabel = createResultLabel("After: 0.00%");
        marginRateChangeLabel = createResultLabel("Change: 0.00%");
        panel.add(beforeMarginRateLabel);
        panel.add(afterMarginRateLabel);
        panel.add(marginRateChangeLabel);
        panel.add(Box.createVerticalStrut(20));
        
        // Recommendation
        panel.add(createSectionLabel("RECOMMENDATION:"));
        recommendationLabel = new JLabel("Click 'Run Simulation' to see results");
        recommendationLabel.setFont(new Font("Arial", Font.BOLD, 14));
        recommendationLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        recommendationLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        panel.add(recommendationLabel);
        
        panel.add(Box.createVerticalGlue());
        
        return panel;
    }
    
    private JLabel createSectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        return label;
    }
    
    private JLabel createResultLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setBorder(BorderFactory.createEmptyBorder(3, 20, 3, 10));
        return label;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setBackground(new Color(0, 153, 153));
        
        // Back button
        backButton = new JButton("<< Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setPreferredSize(new Dimension(120, 40));
        backButton.addActionListener(e -> handleBack());
        
        // Run Simulation button
        runSimulationButton = new JButton("Run Simulation");
        runSimulationButton.setFont(new Font("Arial", Font.BOLD, 14));
        runSimulationButton.setBackground(new Color(0, 123, 255));
        runSimulationButton.setForeground(Color.WHITE);
        runSimulationButton.setPreferredSize(new Dimension(150, 40));
        runSimulationButton.setFocusPainted(false);
        runSimulationButton.addActionListener(e -> runSimulation());
        
        // Apply Changes button
        applyChangesButton = new JButton("Apply Changes");
        applyChangesButton.setFont(new Font("Arial", Font.BOLD, 14));
        applyChangesButton.setBackground(new Color(40, 167, 69));
        applyChangesButton.setForeground(Color.WHITE);
        applyChangesButton.setPreferredSize(new Dimension(150, 40));
        applyChangesButton.setFocusPainted(false);
        applyChangesButton.setEnabled(false);
        applyChangesButton.addActionListener(e -> handleApplyChanges());
        
        // Reset button
        resetButton = new JButton("Reset");
        resetButton.setFont(new Font("Arial", Font.BOLD, 14));
        resetButton.setBackground(new Color(108, 117, 125));
        resetButton.setForeground(Color.WHITE);
        resetButton.setPreferredSize(new Dimension(120, 40));
        resetButton.setFocusPainted(false);
        resetButton.addActionListener(e -> handleReset());
        
        panel.add(backButton);
        panel.add(runSimulationButton);
        panel.add(applyChangesButton);
        panel.add(resetButton);
        
        return panel;
    }
    
    /**
     * Load all products into the table
     */
    private void loadProductData() {
        tableModel.setRowCount(0);
        
        for (Supplier supplier : business.getSupplierDirectory().getSuplierList()) {
            for (Product product : supplier.getProductCatalog().getProductList()) {
                Object[] row = {
                    product.toString(),
                    product.getTargetPrice(),
                    "", // New Target (empty initially)
                    product.getNumberOfProductSalesAboveTarget(),
                    product.getNumberOfProductSalesBelowTarget()
                };
                tableModel.addRow(row);
            }
        }
    }
    
    /**
     * Run simulation with new prices
     */
    private void runSimulation() {
        performanceReport.captureBeforeState();
        try {
            adjustedPrices.clear();
            int adjustmentCount = 0;
            int currentRow = 0;
            
            // Collect all adjustments
            for (Supplier supplier : business.getSupplierDirectory().getSuplierList()) {
                for (Product product : supplier.getProductCatalog().getProductList()) {
                    Object newTargetObj = tableModel.getValueAt(currentRow, 2);
                    
                    if (newTargetObj != null && !newTargetObj.toString().trim().isEmpty()) {
                        try {
                            int newTarget = Integer.parseInt(newTargetObj.toString().trim());
                            
                            // Validate against floor and ceiling
                            if (newTarget < product.getFloorPrice() || newTarget > product.getCeilingPrice()) {
                                JOptionPane.showMessageDialog(this,
                                    product.toString() + " price must be between " +
                                    product.getFloorPrice() + " and " + product.getCeilingPrice(),
                                    "Invalid Price",
                                    JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            
                            adjustedPrices.put(product, newTarget);
                            adjustmentCount++;
                        } catch (NumberFormatException e) {
                            JOptionPane.showMessageDialog(this,
                                "Invalid number format for " + product.toString(),
                                "Input Error",
                                JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                    currentRow++;
                }
            }
            
            if (adjustmentCount == 0) {
                JOptionPane.showMessageDialog(this,
                    "Please enter at least one new target price",
                    "No Changes",
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            // Run simulation
            simulation.saveCurrentState();
            
            for (Product product : adjustedPrices.keySet()) {
                simulation.applyNewPrice(product, adjustedPrices.get(product));
            }
            
            simulation.calculateNewResults();
            
            // Update results display
            updateResultsDisplay();
            
            // Enable apply button
            applyChangesButton.setEnabled(true);
            performanceReport.captureAfterState();
            JOptionPane.showMessageDialog(this,
                "Simulation completed!\n" +
                "Adjusted " + adjustmentCount + " product(s)",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error running simulation: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    /**
     * 🔧 MODIFIED: Update the results panel with simulation results
     * Now includes Margin Rate display and recommendation based on margin rate
     */
    private void updateResultsDisplay() {
        // Revenue
        beforeRevenueLabel.setText("Before: $" + 
            String.format("%,d", simulation.getBeforeRevenue()));
        afterRevenueLabel.setText("After: $" + 
            String.format("%,d", simulation.getAfterRevenue()));
        
        int revenueChange = simulation.getRevenueChange();
        revenueChangeLabel.setText("Change: " + 
            (revenueChange >= 0 ? "+" : "") + "$" + String.format("%,d", revenueChange));
        revenueChangeLabel.setForeground(revenueChange >= 0 ? new Color(40, 167, 69) : Color.RED);
        
        // Profit
        beforeProfitLabel.setText("Before: $" + 
            String.format("%,d", simulation.getBeforeProfit()));
        afterProfitLabel.setText("After: $" + 
            String.format("%,d", simulation.getAfterProfit()));
        
        int profitChange = simulation.getProfitChange();
        profitChangeLabel.setText("Change: " + 
            (profitChange >= 0 ? "+" : "") + "$" + String.format("%,d", profitChange));
        profitChangeLabel.setForeground(profitChange >= 0 ? new Color(40, 167, 69) : Color.RED);
        
        // 🔧 NEW: Margin Rate
        beforeMarginRateLabel.setText("Before: " + 
            String.format("%.2f%%", simulation.getBeforeMarginRate()));
        afterMarginRateLabel.setText("After: " + 
            String.format("%.2f%%", simulation.getAfterMarginRate()));
        
        double marginRateChange = simulation.getMarginRateChange();
        marginRateChangeLabel.setText("Change: " + 
            (marginRateChange >= 0 ? "+" : "") + String.format("%.2f%%", marginRateChange));
        marginRateChangeLabel.setForeground(marginRateChange >= 0 ? new Color(40, 167, 69) : Color.RED);
        
        // 🔧 MODIFIED: Recommendation based on MARGIN RATE (per TA feedback)
        if (marginRateChange > 0) {
            recommendationLabel.setText("<html><font color='green'>✓ APPLY NEW PRICES<br>Margin rate increased by " + 
                String.format("%.2f%%", marginRateChange) + "</font></html>");
        } else if (marginRateChange < 0) {
            recommendationLabel.setText("<html><font color='red'>✗ KEEP ORIGINAL PRICES<br>Margin rate decreased by " + 
                String.format("%.2f%%", Math.abs(marginRateChange)) + "</font></html>");
        } else {
            recommendationLabel.setText("<html>→ NO CHANGE<br>Margin rate remains the same</html>");
        }
    }
    
    /**
     * Handle Apply Changes button click
     * Permanently applies the simulated price changes
     */
    private void handleApplyChanges() {
        int choice = JOptionPane.showConfirmDialog(this,
            "Apply these price changes permanently?\n" +
            "This cannot be undone.",
            "Confirm Changes",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (choice == JOptionPane.YES_OPTION) {
            // Save the new state as the current state
            simulation.saveCurrentState();
            
            JOptionPane.showMessageDialog(this,
                "Price changes applied successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            
            // Clear the "New Target" column
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                tableModel.setValueAt("", i, 2);
            }
            
            // Update "Current Target" column with new values
            loadProductData();
            
            // 🔧 MODIFIED: Reset display including margin rate
            beforeRevenueLabel.setText("Before: $" + 
                String.format("%,d", simulation.getBeforeRevenue()));
            afterRevenueLabel.setText("After: $" + 
                String.format("%,d", simulation.getBeforeRevenue()));
            revenueChangeLabel.setText("Change: $0");
            revenueChangeLabel.setForeground(Color.BLACK);
            
            beforeProfitLabel.setText("Before: $" + 
                String.format("%,d", simulation.getBeforeProfit()));
            afterProfitLabel.setText("After: $" + 
                String.format("%,d", simulation.getBeforeProfit()));
            profitChangeLabel.setText("Change: $0");
            profitChangeLabel.setForeground(Color.BLACK);
            
            beforeMarginRateLabel.setText("Before: " + 
                String.format("%.2f%%", simulation.getBeforeMarginRate()));
            afterMarginRateLabel.setText("After: " + 
                String.format("%.2f%%", simulation.getBeforeMarginRate()));
            marginRateChangeLabel.setText("Change: 0.00%");
            marginRateChangeLabel.setForeground(Color.BLACK);
            
            recommendationLabel.setText("Click 'Run Simulation' to see results");
            recommendationLabel.setForeground(Color.BLACK);
            
            applyChangesButton.setEnabled(false);
            adjustedPrices.clear();
        }
    }
    
    /**
     * Handle Reset button click
     * Reverts all changes to original prices
     */
    private void handleReset() {
        simulation.restore();
        
        // Clear all "New Target" inputs
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            tableModel.setValueAt("", i, 2);
        }
        
        // Reload product data to show original prices
        loadProductData();
        
        applyChangesButton.setEnabled(false);
        adjustedPrices.clear();
        resetResultsDisplay();
        
        JOptionPane.showMessageDialog(this,
            "All changes reset to original values",
            "Reset Complete",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * 🔧 MODIFIED: Reset the results display to initial state
     * Now includes margin rate reset
     */
    private void resetResultsDisplay() {
        beforeRevenueLabel.setText("Before: $0");
        afterRevenueLabel.setText("After: $0");
        revenueChangeLabel.setText("Change: $0");
        revenueChangeLabel.setForeground(Color.BLACK);
        
        beforeProfitLabel.setText("Before: $0");
        afterProfitLabel.setText("After: $0");
        profitChangeLabel.setText("Change: $0");
        profitChangeLabel.setForeground(Color.BLACK);
        
        // 🔧 NEW: Reset margin rate labels
        beforeMarginRateLabel.setText("Before: 0.00%");
        afterMarginRateLabel.setText("After: 0.00%");
        marginRateChangeLabel.setText("Change: 0.00%");
        marginRateChangeLabel.setForeground(Color.BLACK);
        
        recommendationLabel.setText("Click 'Run Simulation' to see results");
        recommendationLabel.setForeground(Color.BLACK);
    }
    
    /**
     * Handle Back button click
     * Returns to previous panel
     */
    private void handleBack() {
        try {
            // Get parent container
            Container parent = cardSequencePanel;

            // Remove this panel
            if (parent != null) {
                parent.remove(this);
                parent.revalidate();
                parent.repaint();
            }

            // Navigate to previous card
            CardLayout layout = (CardLayout) cardSequencePanel.getLayout();
            layout.previous(cardSequencePanel);

        } catch (Exception e) {
            System.err.println("Error in handleBack: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public ProductPerformanceReport getPerformanceReport() {
        return performanceReport;
    }
}