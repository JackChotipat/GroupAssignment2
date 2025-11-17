/*
 * FinalReportPanel - UI component for displaying product performance reports
 * Task 6: Generate Final Product Performance Report
 * 
 * Features:
 * - Before/After product performance comparison
 * - Executive summary with key metrics
 * - Filtering capabilities (all, changed, revenue increase/decrease)
 * - Export functionality to text file
 * - Color-coded visualization of changes
 * - Detailed product-level analysis
 * 
 * @author Marketing Team
 * @date 2025-11-16
 * @version 2.0
 */
package UserInterface.Main.WorkSpaceProfiles.OrderManagement;

import TheBusiness.Business.Business;
import TheBusiness.Business.ProductPerformanceReport;
import TheBusiness.Business.ProductPerformanceReport.ProductComparisonData;
import TheBusiness.Business.ProductPerformanceReport.ReportSummary;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Panel to display final product performance report with before/after comparison
 * This panel integrates with the pricing simulation and optimization workflows
 */
public class FinalReportPanel extends JPanel {
    
    // Core business components
    private Business business;
    private ProductPerformanceReport report;
    private JPanel cardSequencePanel;
    
    // UI Components
    private JTable reportTable;
    private DefaultTableModel tableModel;
    private JTextArea summaryTextArea;
    private JButton backButton;
    private JButton exportButton;
    private JButton refreshButton;
    private JButton clearButton;
    private JComboBox<String> filterComboBox;
    private JLabel statusLabel;
    
    // Formatting
    private static final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
    
    /**
     * Constructor
     * @param business The business object containing all data
     * @param cardPanel The parent panel for navigation
     */
    public FinalReportPanel(Business business, JPanel cardPanel) {
        super();
        this.business = business;
        this.cardSequencePanel = cardPanel;
        this.report = business.getProductPerformanceReport();
        
        initComponents();
        updateStatusLabel();
    }
    
    /**
     * Initialize all UI components
     */
    private void initComponents() {
        setLayout(new BorderLayout(0, 5));
        setBackground(new Color(0, 153, 153));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Title Panel
        JPanel titlePanel = createTitlePanel();
        add(titlePanel, BorderLayout.NORTH);
        
        // Main Content
        JPanel mainPanel = createMainPanel();
        add(mainPanel, BorderLayout.CENTER);
        
        // Control Panel at bottom
        JPanel controlPanel = createControlPanel();
        add(controlPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Create the title panel with header information
     */
    private JPanel createTitlePanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(0, 153, 153));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 15, 10));
        
        // Main title
        JLabel titleLabel = new JLabel("FINAL PRODUCT PERFORMANCE REPORT");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);
        
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("Task 6: Comprehensive Before/After Analysis");
        subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        subtitleLabel.setForeground(Color.WHITE);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(subtitleLabel);
        
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        
        // Status label
        statusLabel = new JLabel("Ready");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statusLabel.setForeground(new Color(200, 255, 200));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(statusLabel);
        
        return panel;
    }
    
    /**
     * Create the main panel containing summary and table
     */
    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 5));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        // Summary at top
        summaryTextArea = new JTextArea(8, 50);
        summaryTextArea.setEditable(false);
        summaryTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        summaryTextArea.setBackground(new Color(240, 248, 255));
        summaryTextArea.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        JScrollPane summaryScrollPane = new JScrollPane(summaryTextArea);
        summaryScrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
            "Executive Summary",
            javax.swing.border.TitledBorder.LEFT,
            javax.swing.border.TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 13),
            new Color(70, 130, 180)
        ));
        panel.add(summaryScrollPane, BorderLayout.NORTH);
        
        // Table in center
        JPanel tablePanel = createTablePanel();
        panel.add(tablePanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Create the table panel with filtering controls
     */
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 5));
        panel.setBackground(Color.WHITE);
        
        // Filter controls
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterPanel.setBackground(Color.WHITE);
        
        JLabel filterLabel = new JLabel("Filter:");
        filterLabel.setFont(new Font("Arial", Font.BOLD, 12));
        
        filterComboBox = new JComboBox<>(new String[]{
            "Show All Products",
            "Show Only Changed Products",
            "Show Revenue Increase",
            "Show Revenue Decrease"
        });
        filterComboBox.setPreferredSize(new Dimension(200, 25));
        filterComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applyFilter();
            }
        });
        
        filterPanel.add(filterLabel);
        filterPanel.add(filterComboBox);
        
        panel.add(filterPanel, BorderLayout.NORTH);
        
        // Table
        String[] columnNames = {
            "Supplier",
            "Product",
            "Revenue Before",
            "Revenue After",
            "Revenue Δ%",
            "Target Before",
            "Target After",
            "Target Δ%",
            "Sales Above (B)",
            "Sales Below (B)",
            "Sales Above (A)",
            "Sales Below (A)"
        };
        
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        reportTable = new JTable(tableModel);
        styleTable();
        
        JScrollPane scrollPane = new JScrollPane(reportTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Apply styling to the report table
     */
    private void styleTable() {
        reportTable.setFont(new Font("Arial", Font.PLAIN, 11));
        reportTable.setRowHeight(25);
        reportTable.setGridColor(Color.LIGHT_GRAY);
        reportTable.setShowGrid(true);
        reportTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        // Header styling
        JTableHeader header = reportTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 11));
        header.setBackground(new Color(70, 130, 180));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));
        header.setReorderingAllowed(false);
        
        // Column widths
        reportTable.getColumnModel().getColumn(0).setPreferredWidth(120);  // Supplier
        reportTable.getColumnModel().getColumn(1).setPreferredWidth(180);  // Product
        reportTable.getColumnModel().getColumn(2).setPreferredWidth(100);  // Revenue Before
        reportTable.getColumnModel().getColumn(3).setPreferredWidth(100);  // Revenue After
        reportTable.getColumnModel().getColumn(4).setPreferredWidth(80);   // Revenue Δ%
        reportTable.getColumnModel().getColumn(5).setPreferredWidth(100);  // Target Before
        reportTable.getColumnModel().getColumn(6).setPreferredWidth(100);  // Target After
        reportTable.getColumnModel().getColumn(7).setPreferredWidth(80);   // Target Δ%
        reportTable.getColumnModel().getColumn(8).setPreferredWidth(90);   // Sales Above (B)
        reportTable.getColumnModel().getColumn(9).setPreferredWidth(90);   // Sales Below (B)
        reportTable.getColumnModel().getColumn(10).setPreferredWidth(90);  // Sales Above (A)
        reportTable.getColumnModel().getColumn(11).setPreferredWidth(90);  // Sales Below (A)
        
        // Custom renderer for alternating rows and colored percentages
        reportTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    // Alternating row colors
                    if (row % 2 == 0) {
                        c.setBackground(Color.WHITE);
                    } else {
                        c.setBackground(new Color(245, 245, 245));
                    }
                    c.setForeground(Color.BLACK);
                    
                    // Color code percentage changes
                    if ((column == 4 || column == 7) && value instanceof String) {
                        String strValue = (String) value;
                        if (strValue.startsWith("+")) {
                            c.setForeground(new Color(0, 128, 0)); // Green for positive
                            c.setFont(c.getFont().deriveFont(Font.BOLD));
                        } else if (strValue.startsWith("-")) {
                            c.setForeground(new Color(200, 0, 0)); // Red for negative
                            c.setFont(c.getFont().deriveFont(Font.BOLD));
                        }
                    }
                }
                
                // Right align numeric columns
                if (column >= 2) {
                    ((JLabel) c).setHorizontalAlignment(SwingConstants.RIGHT);
                } else {
                    ((JLabel) c).setHorizontalAlignment(SwingConstants.LEFT);
                }
                
                return c;
            }
        });
    }
    
    /**
     * Create the control panel with action buttons
     */
    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setBackground(new Color(0, 153, 153));
        
        // Load/Refresh button
        refreshButton = new JButton("Load Report Data");
        refreshButton.setPreferredSize(new Dimension(150, 35));
        refreshButton.setBackground(new Color(34, 139, 34));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFont(new Font("Arial", Font.BOLD, 12));
        refreshButton.setFocusPainted(false);
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadReportData();
            }
        });
        
        // Export button
        exportButton = new JButton("Export to Text");
        exportButton.setPreferredSize(new Dimension(150, 35));
        exportButton.setBackground(new Color(102, 153, 255));
        exportButton.setForeground(Color.WHITE);
        exportButton.setFont(new Font("Arial", Font.BOLD, 12));
        exportButton.setFocusPainted(false);
        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportReport();
            }
        });
        
        // Clear button
        clearButton = new JButton("Clear Report");
        clearButton.setPreferredSize(new Dimension(120, 35));
        clearButton.setBackground(new Color(220, 100, 50));
        clearButton.setForeground(Color.WHITE);
        clearButton.setFont(new Font("Arial", Font.BOLD, 12));
        clearButton.setFocusPainted(false);
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearReport();
            }
        });
        
        // Back button
        backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(100, 35));
        backButton.setBackground(new Color(150, 150, 150));
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Arial", Font.BOLD, 12));
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> handleBack());
        
        panel.add(refreshButton);
        panel.add(exportButton);
        panel.add(clearButton);
        panel.add(backButton);
        
        return panel;
    }
    
    /**
     * Handle back button click - navigates to previous panel
     */
    private void handleBack() {
        try {
            CardLayout layout = (CardLayout) cardSequencePanel.getLayout();
            layout.previous(cardSequencePanel);
            cardSequencePanel.revalidate();
            cardSequencePanel.repaint();
        } catch (Exception e) {
            System.err.println("Error in handleBack: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Load report data from the shared ProductPerformanceReport
     * Validates data availability before loading
     */
    public void loadReportData() {
        // Validate that data exists
        if (!report.hasBeforeState() || !report.hasAfterState()) {
            showNoDataDialog();
            updateStatusLabel();
            return;
        }
        
        try {
            // Clear existing data
            tableModel.setRowCount(0);
            
            // Load summary
            ReportSummary summary = report.getSummary();
            summaryTextArea.setText(generateSummaryText(summary));
            
            // Load product data based on current filter
            applyFilter();
            
            // Update status
            updateStatusLabel();
            
            // Show success message
            JOptionPane.showMessageDialog(this,
                "Report loaded successfully!\n\n" +
                "Total products: " + summary.totalProducts + "\n" +
                "Products changed: " + summary.productsChanged + "\n" +
                "Revenue change: " + currencyFormat.format(summary.getTotalRevenueChange()),
                "Report Loaded",
                JOptionPane.INFORMATION_MESSAGE);
                
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error loading report:\n" + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    /**
     * Display dialog when no data is available
     */
    private void showNoDataDialog() {
        JOptionPane.showMessageDialog(this,
            "No report data available.\n\n" +
            "To generate a report:\n" +
            "1. Go to 'Manage Prices'\n" +
            "2. Select 'Task 4: Run Price Simulation' OR 'Task 5: Optimize Profit Margins'\n" +
            "3. Enter new target prices and run the simulation\n" +
            "4. Return here to view the generated report\n\n" +
            "The report will show before/after comparison of:\n" +
            "• Product revenues\n" +
            "• Target prices\n" +
            "• Sales performance\n" +
            "• Overall profit impact",
            "No Data Available",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Generate formatted summary text
     */
    private String generateSummaryText(ReportSummary summary) {
        StringBuilder sb = new StringBuilder();
        sb.append("═══════════════════════════════════════════════════════════════════\n");
        sb.append("                   EXECUTIVE SUMMARY\n");
        sb.append("═══════════════════════════════════════════════════════════════════\n\n");
        
        sb.append(String.format("Total Products Analyzed:      %,d\n", summary.totalProducts));
        sb.append(String.format("Products with Price Changes:  %,d  (↑ %d  ↓ %d)\n", 
            summary.productsChanged, 
            summary.productsIncreased, 
            summary.productsDecreased));
        sb.append("\n───────────────────────────────────────────────────────────────────\n");
        sb.append("REVENUE ANALYSIS\n");
        sb.append("───────────────────────────────────────────────────────────────────\n");
        sb.append(String.format("Total Revenue Before:         %s\n", 
            currencyFormat.format(summary.totalRevenueBefore)));
        sb.append(String.format("Total Revenue After:          %s\n", 
            currencyFormat.format(summary.totalRevenueAfter)));
        sb.append(String.format("Revenue Change:               %s  (%s%.2f%%)\n", 
            currencyFormat.format(summary.getTotalRevenueChange()),
            summary.getTotalRevenueChange() >= 0 ? "+" : "",
            summary.getTotalRevenueChangePercent()));
        
        sb.append("\n───────────────────────────────────────────────────────────────────\n");
        sb.append("PROFIT ANALYSIS\n");
        sb.append("───────────────────────────────────────────────────────────────────\n");
        sb.append(String.format("Total Profit Before:          %s\n", 
            currencyFormat.format(summary.totalProfitBefore)));
        sb.append(String.format("Total Profit After:           %s\n", 
            currencyFormat.format(summary.totalProfitAfter)));
        sb.append(String.format("Profit Change:                %s  (%s%.2f%%)\n", 
            currencyFormat.format(summary.getTotalProfitChange()),
            summary.getTotalProfitChange() >= 0 ? "+" : "",
            summary.getTotalProfitChangePercent()));
        
        return sb.toString();
    }
    
    /**
     * Apply the selected filter to the table data
     */
    private void applyFilter() {
        if (!report.hasBeforeState() || !report.hasAfterState()) {
            return;
        }
        
        tableModel.setRowCount(0);
        
        int filterIndex = filterComboBox.getSelectedIndex();
        List<ProductComparisonData> dataList = report.getAllProductData();
        
        int displayedCount = 0;
        for (ProductComparisonData data : dataList) {
            boolean include = false;
            
            switch (filterIndex) {
                case 0: // Show All
                    include = true;
                    break;
                case 1: // Show Only Changed
                    include = (data.getTargetPriceChange() != 0);
                    break;
                case 2: // Show Revenue Increase
                    include = (data.getRevenueChange() > 0);
                    break;
                case 3: // Show Revenue Decrease
                    include = (data.getRevenueChange() < 0);
                    break;
            }
            
            if (include) {
                addProductRow(data);
                displayedCount++;
            }
        }
        
        // Update status with filter info
        updateStatusLabel();
    }
    
    /**
     * Add a product row to the table
     */
    private void addProductRow(ProductComparisonData data) {
        Object[] rowData = new Object[12];
        
        rowData[0] = data.supplierName;
        rowData[1] = data.productName;
        rowData[2] = currencyFormat.format(data.revenueBefore);
        rowData[3] = currencyFormat.format(data.revenueAfter);
        rowData[4] = formatPercent(data.getRevenueChangePercent());
        rowData[5] = currencyFormat.format(data.targetPriceBefore);
        rowData[6] = currencyFormat.format(data.targetPriceAfter);
        rowData[7] = formatPercent(data.getTargetPriceChangePercent());
        rowData[8] = data.salesAboveBefore;
        rowData[9] = data.salesBelowBefore;
        rowData[10] = data.salesAboveAfter;
        rowData[11] = data.salesBelowAfter;
        
        tableModel.addRow(rowData);
    }
    
    /**
     * Format percentage with proper sign
     */
    private String formatPercent(double percent) {
        if (percent > 0) {
            return String.format("+%.2f%%", percent);
        } else if (percent < 0) {
            return String.format("%.2f%%", percent);
        } else {
            return "0.00%";
        }
    }
    
    /**
     * Export report to text file
     */
    private void exportReport() {
        if (!report.hasBeforeState() || !report.hasAfterState()) {
            JOptionPane.showMessageDialog(this,
                "No data to export. Please load report data first.",
                "No Data",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Report As");
        fileChooser.setSelectedFile(new java.io.File("ProductPerformanceReport.txt"));
        
        int userSelection = fileChooser.showSaveDialog(this);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();
            
            try (java.io.PrintWriter writer = new java.io.PrintWriter(fileToSave)) {
                writer.println(report.generateTextReport());
                JOptionPane.showMessageDialog(this, 
                    "Report exported successfully to:\n" + fileToSave.getAbsolutePath(),
                    "Export Successful", 
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Error exporting report:\n" + e.getMessage(),
                    "Export Error", 
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Clear the current report data
     */
    private void clearReport() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to clear the current report?\n" +
            "This will remove all displayed data from the view.",
            "Confirm Clear",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            tableModel.setRowCount(0);
            summaryTextArea.setText("Report cleared. Click 'Load Report Data' to reload.");
            updateStatusLabel();
            JOptionPane.showMessageDialog(this,
                "Report cleared successfully.",
                "Cleared",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Update the status label with current report state
     */
    private void updateStatusLabel() {
        if (!report.hasBeforeState() || !report.hasAfterState()) {
            statusLabel.setText("Status: No data available - Please run a simulation first");
            statusLabel.setForeground(new Color(255, 200, 150));
        } else {
            int rowCount = tableModel.getRowCount();
            String filterName = (String) filterComboBox.getSelectedItem();
            statusLabel.setText(String.format("Status: Showing %d products | Filter: %s", 
                rowCount, filterName));
            statusLabel.setForeground(new Color(200, 255, 200));
        }
    }
    
    /**
     * Set the report object (called after simulation/optimization)
     * @param newReport The new report to display
     */
    public void setReport(ProductPerformanceReport newReport) {
        this.report = newReport;
        loadReportData();
    }
}