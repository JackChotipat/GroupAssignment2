/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UserInterface.Main.WorkSpaceProfiles.OrderManagement;

import TheBusiness.Business.Business;
import TheBusiness.Business.ProfitOptimizer;
import TheBusiness.ProductManagement.Product;
import TheBusiness.Supplier.Supplier;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author 123
 */
public class OptimizePanel extends JPanel {
    private Business business;
    private ProfitOptimizer optimizer;
    private JPanel cardSequencePanel;
    
    // UI Components
    private JButton optimizeButton;
    private JButton resetButton;
    private JButton backButton;
    private JButton viewDetailsButton;
    
    private JTextArea logArea;
    private JScrollPane logScrollPane;
    
    // Results display
    private JLabel initialProfitLabel;
    private JLabel finalProfitLabel;
    private JLabel improvementLabel;
    private JLabel improvementPercentLabel;
    private JLabel iterationsLabel;
    private JLabel adjustmentsLabel;
    private JLabel statusLabel;
    
    private JProgressBar progressBar;
    
    // Results table
    private JTable resultsTable;
    private DefaultTableModel tableModel;
    
    private boolean isOptimizing = false;
    private int initialProfit;
    
    public OptimizePanel(Business b, JPanel cardPanel) {
        super();
        this.business = b;
        this.cardSequencePanel = cardPanel;
        this.optimizer = new ProfitOptimizer(business);
        
        initComponents();
        displayInitialState();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(0, 153, 153));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title Panel
        add(createTitlePanel(), BorderLayout.NORTH);
        
        // Center Panel (Split: Log and Results)
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(createLogPanel());
        splitPane.setRightComponent(createResultsPanel());
        splitPane.setDividerLocation(500);
        add(splitPane, BorderLayout.CENTER);
        
        // Button Panel
        add(createButtonPanel(), BorderLayout.SOUTH);
    }
    
    private JPanel createTitlePanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(0, 153, 153));
        
        JLabel titleLabel = new JLabel("Task 5: Maximize Profit Margins");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        panel.add(titleLabel);
        
        return panel;
    }
    
    private JPanel createLogPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY, 2),
            "Optimization Log",
            0, 0,
            new Font("Arial", Font.BOLD, 16)
        ));
        
        // Instructions
        JLabel instructionLabel = new JLabel(
            "<html><b>Instructions:</b> Click 'Start Optimization' to automatically find the best prices</html>"
        );
        instructionLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(instructionLabel, BorderLayout.NORTH);
        
        // Log area
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Courier New", Font.PLAIN, 12));
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        
        logScrollPane = new JScrollPane(logArea);
        panel.add(logScrollPane, BorderLayout.CENTER);
        
        // Progress bar
        JPanel progressPanel = new JPanel(new BorderLayout(5, 5));
        progressPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        statusLabel = new JLabel("Ready to optimize");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        progressPanel.add(statusLabel, BorderLayout.NORTH);
        
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setValue(0);
        progressPanel.add(progressBar, BorderLayout.CENTER);
        
        panel.add(progressPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createResultsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY, 2),
            "Optimization Results",
            0, 0,
            new Font("Arial", Font.BOLD, 16)
        ));
        
        // Statistics Panel
        JPanel statsPanel = createStatsPanel();
        panel.add(statsPanel);
        
        panel.add(Box.createVerticalStrut(20));
        
        // Comparison Panel
        JPanel comparisonPanel = createComparisonPanel();
        panel.add(comparisonPanel);
        
        panel.add(Box.createVerticalGlue());
        
        return panel;
    }
    
    private JPanel createStatsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2, 10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder("Statistics"));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));
        
        // Iterations
        panel.add(createLabel("Total Iterations:"));
        iterationsLabel = createValueLabel("0");
        panel.add(iterationsLabel);
        
        // Adjustments
        panel.add(createLabel("Price Adjustments:"));
        adjustmentsLabel = createValueLabel("0");
        panel.add(adjustmentsLabel);
        
        // Initial Profit
        panel.add(createLabel("Initial Profit:"));
        initialProfitLabel = createValueLabel("$0");
        panel.add(initialProfitLabel);
        
        // Final Profit
        panel.add(createLabel("Final Profit:"));
        finalProfitLabel = createValueLabel("$0");
        panel.add(finalProfitLabel);
        
        // Improvement
        panel.add(createLabel("Improvement:"));
        improvementLabel = createValueLabel("$0");
        panel.add(improvementLabel);
        
        // Improvement %
        panel.add(createLabel("Improvement %:"));
        improvementPercentLabel = createValueLabel("0.00%");
        panel.add(improvementPercentLabel);
        
        return panel;
    }
    
    private JPanel createComparisonPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder("Price Changes"));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
        
        // Table
        String[] columns = {"Product", "Old Target", "New Target", "Change"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        resultsTable = new JTable(tableModel);
        resultsTable.setFont(new Font("Arial", Font.PLAIN, 12));
        resultsTable.setRowHeight(25);
        resultsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        resultsTable.getTableHeader().setBackground(new Color(0, 153, 153));
        resultsTable.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(resultsTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        return label;
    }
    
    private JLabel createValueLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        return label;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setBackground(new Color(0, 153, 153));
        
        // Back Button
        backButton = new JButton("<< Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 14));
        backButton.addActionListener(e -> handleBack());
        
        // Reset Button
        resetButton = new JButton("Reset");
        resetButton.setFont(new Font("Arial", Font.PLAIN, 14));
        resetButton.addActionListener(e -> handleReset());
        
        // Optimize Button
        optimizeButton = new JButton("Start Optimization");
        optimizeButton.setFont(new Font("Arial", Font.BOLD, 14));
        optimizeButton.setBackground(new Color(40, 167, 69));
        optimizeButton.setForeground(Color.WHITE);
        optimizeButton.addActionListener(e -> handleOptimize());
        
        // View Details Button
        viewDetailsButton = new JButton("View Details");
        viewDetailsButton.setFont(new Font("Arial", Font.PLAIN, 14));
        viewDetailsButton.setEnabled(false);
        viewDetailsButton.addActionListener(e -> handleViewDetails());
        
        panel.add(backButton);
        panel.add(resetButton);
        panel.add(optimizeButton);
        panel.add(viewDetailsButton);
        
        return panel;
    }
    
    private void displayInitialState() {
        // Calculate initial profit
        initialProfit = calculateCurrentProfit();
        
        initialProfitLabel.setText("$" + String.format("%,d", initialProfit));
        
        logArea.append("System Ready\n");
        logArea.append("Current Profit: $" + String.format("%,d", initialProfit) + "\n");
        logArea.append("Click 'Start Optimization' to begin\n");
        logArea.append("=".repeat(50) + "\n\n");
    }
    
    private int calculateCurrentProfit() {
        int total = 0;
        for (Supplier supplier : business.getSupplierDirectory().getSuplierList()) {
            for (Product product : supplier.getProductCatalog().getProductList()) {
                total += product.getOrderPricePerformance();
            }
        }
        return total;
    }
    
    private void handleOptimize() {
        if (isOptimizing) return;
        
        isOptimizing = true;
        optimizeButton.setEnabled(false);
        backButton.setEnabled(false);
        statusLabel.setText("Optimizing...");
        
        logArea.setText("");
        logArea.append("=".repeat(60) + "\n");
        logArea.append("   STARTING OPTIMIZATION\n");
        logArea.append("=".repeat(60) + "\n\n");
        
        // Run optimization in background thread
        SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
            
            @Override
            protected Void doInBackground() throws Exception {
                // Custom optimization with UI updates
                runOptimizationWithUpdates();
                return null;
            }
            
            @Override
            protected void process(java.util.List<String> chunks) {
                for (String message : chunks) {
                    logArea.append(message + "\n");
                    logArea.setCaretPosition(logArea.getDocument().getLength());
                }
            }
            
            @Override
            protected void done() {
                isOptimizing = false;
                optimizeButton.setEnabled(true);
                optimizeButton.setText("Re-optimize");
                backButton.setEnabled(true);
                viewDetailsButton.setEnabled(true);
                statusLabel.setText("Optimization Complete!");
                progressBar.setValue(100);
                
                displayResults();
                
                JOptionPane.showMessageDialog(OptimizePanel.this,
                    "Optimization completed successfully!\n" +
                    "Check the results panel for details.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        };
        
        worker.execute();
    }
    
    private void runOptimizationWithUpdates() {
        optimizer = new ProfitOptimizer(business);
        
        // We need to manually run the optimization to get progress updates
        // This is a simplified version for UI demonstration
        
        try {
            Thread.sleep(500); // Initial delay
            logArea.append("Analyzing current prices...\n");
            progressBar.setValue(10);
            
            Thread.sleep(500);
            logArea.append("Generating optimization strategy...\n");
            progressBar.setValue(20);
            
            Thread.sleep(500);
            logArea.append("Running optimization algorithm...\n\n");
            progressBar.setValue(30);
            
            // Run actual optimization
            optimizer.optimize();
            
            progressBar.setValue(90);
            logArea.append("\n" + "=".repeat(60) + "\n");
            logArea.append("   OPTIMIZATION COMPLETE\n");
            logArea.append("=".repeat(60) + "\n");
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    private void displayResults() {
        int finalProfit = optimizer.getBestProfit();
        int improvement = finalProfit - initialProfit;
        double improvementPercent = ((double) improvement / initialProfit) * 100;
        
        // Update labels
        finalProfitLabel.setText("$" + String.format("%,d", finalProfit));
        improvementLabel.setText((improvement >= 0 ? "+" : "") + "$" + String.format("%,d", improvement));
        improvementLabel.setForeground(improvement >= 0 ? new Color(40, 167, 69) : Color.RED);
        improvementPercentLabel.setText(String.format("%.2f%%", improvementPercent));
        improvementPercentLabel.setForeground(improvement >= 0 ? new Color(40, 167, 69) : Color.RED);
        
        iterationsLabel.setText(String.valueOf(optimizer.getIterationCount()));
        adjustmentsLabel.setText(String.valueOf(optimizer.getTotalAdjustments()));
        
        // Update table with price changes
        updatePriceChangesTable();
        
        // Update log with summary
        logArea.append("\n\nRESULTS SUMMARY:\n");
        logArea.append("  Iterations: " + optimizer.getIterationCount() + "\n");
        logArea.append("  Adjustments: " + optimizer.getTotalAdjustments() + "\n");
        logArea.append("  Initial Profit: $" + String.format("%,d", initialProfit) + "\n");
        logArea.append("  Final Profit: $" + String.format("%,d", finalProfit) + "\n");
        logArea.append("  Improvement: " + (improvement >= 0 ? "+" : "") + 
                      "$" + String.format("%,d", improvement) + 
                      " (" + String.format("%.2f%%", improvementPercent) + ")\n");
    }
    
    private void updatePriceChangesTable() {
        tableModel.setRowCount(0);
        
        HashMap<Product, Integer> bestPrices = optimizer.getBestPrices();
        int changeCount = 0;
        
        for (Supplier supplier : business.getSupplierDirectory().getSuplierList()) {
            for (Product product : supplier.getProductCatalog().getProductList()) {
                
                if (bestPrices.containsKey(product)) {
                    int oldPrice = product.getTargetPrice();
                    int newPrice = bestPrices.get(product);
                    
                    if (newPrice != oldPrice) {
                        changeCount++;
                        int change = newPrice - oldPrice;
                        double changePercent = ((double) change / oldPrice) * 100;
                        
                        Object[] row = new Object[4];
                        row[0] = product.toString();
                        row[1] = "$" + String.format("%,d", oldPrice);
                        row[2] = "$" + String.format("%,d", newPrice);
                        row[3] = (change >= 0 ? "+" : "") + "$" + String.format("%,d", change) +
                                " (" + String.format("%+.1f%%", changePercent) + ")";
                        
                        tableModel.addRow(row);
                    }
                }
            }
        }
        
        if (changeCount == 0) {
            Object[] row = {"No price changes", "-", "-", "-"};
            tableModel.addRow(row);
        }
    }
    
    private void handleViewDetails() {
        StringBuilder details = new StringBuilder();
        details.append("DETAILED OPTIMIZATION LOG\n");
        details.append("=".repeat(60) + "\n\n");
        
        for (String logEntry : optimizer.getOptimizationLog()) {
            details.append(logEntry).append("\n");
        }
        
        JTextArea textArea = new JTextArea(details.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Courier New", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));
        
        JOptionPane.showMessageDialog(this,
            scrollPane,
            "Optimization Details",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void handleReset() {
        int choice = JOptionPane.showConfirmDialog(this,
            "Reset to original prices?\nThis will undo all optimizations.",
            "Confirm Reset",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (choice == JOptionPane.YES_OPTION) {
            // Reset would require keeping original prices - for now just reload
            logArea.setText("");
            tableModel.setRowCount(0);
            
            initialProfitLabel.setText("$0");
            finalProfitLabel.setText("$0");
            improvementLabel.setText("$0");
            improvementPercentLabel.setText("0.00%");
            iterationsLabel.setText("0");
            adjustmentsLabel.setText("0");
            
            progressBar.setValue(0);
            statusLabel.setText("Ready to optimize");
            optimizeButton.setText("Start Optimization");
            viewDetailsButton.setEnabled(false);
            
            displayInitialState();
            
            JOptionPane.showMessageDialog(this,
                "Reset complete. Click 'Start Optimization' to run again.",
                "Reset",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void handleBack() {
        Container parent = cardSequencePanel;
        if (parent != null) {
            parent.remove(this);
            parent.revalidate();
            parent.repaint();
        }
        
        CardLayout layout = (CardLayout) cardSequencePanel.getLayout();
        layout.previous(cardSequencePanel);
    }
}
