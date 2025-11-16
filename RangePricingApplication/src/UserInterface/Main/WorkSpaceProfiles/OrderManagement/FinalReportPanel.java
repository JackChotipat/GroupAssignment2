/*
 * FinalReportPanel - UI component for displaying product performance reports
 * Task 6: Generate Final Product Performance Report
 * 
 * @author [Your Name]
 * @date 2025-11-15
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
 */
public class FinalReportPanel extends JPanel {
    
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
    private JComboBox<String> filterComboBox;
    
    private static final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
    
    public FinalReportPanel(Business business, JPanel cardPanel) {
        super();
        this.business = business;
        this.cardSequencePanel = cardPanel;
        this.report = new ProductPerformanceReport(business);
        
        initComponents();
    }
    
    /**
     * Initialize all UI components
     */
    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(0, 153, 153));
        
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
    
    private JPanel createTitlePanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(0, 153, 153));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        JLabel titleLabel = new JLabel("FINAL PRODUCT PERFORMANCE REPORT");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);
        
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        JLabel subtitleLabel = new JLabel("Task 6: Comprehensive Before/After Analysis");
        subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        subtitleLabel.setForeground(Color.WHITE);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(subtitleLabel);
        
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        return panel;
    }
    
    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Summary at top
        summaryTextArea = new JTextArea(8, 50);
        summaryTextArea.setEditable(false);
        summaryTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        summaryTextArea.setBackground(new Color(240, 248, 255));
        
        JScrollPane summaryScrollPane = new JScrollPane(summaryTextArea);
        summaryScrollPane.setBorder(BorderFactory.createTitledBorder("Executive Summary"));
        panel.add(summaryScrollPane, BorderLayout.NORTH);
        
        // Table in center
        JPanel tablePanel = createTablePanel();
        panel.add(tablePanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Filter controls
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBackground(Color.WHITE);
        
        filterComboBox = new JComboBox<>(new String[]{
            "Show All Products",
            "Show Only Changed Products",
            "Show Revenue Increase",
            "Show Revenue Decrease"
        });
        filterComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applyFilter();
            }
        });
        
        filterPanel.add(new JLabel("Filter:"));
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
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void styleTable() {
        reportTable.setFont(new Font("Arial", Font.PLAIN, 11));
        reportTable.setRowHeight(25);
        reportTable.setGridColor(Color.LIGHT_GRAY);
        reportTable.setShowGrid(true);
        
        // Header styling
        JTableHeader header = reportTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 11));
        header.setBackground(new Color(70, 130, 180));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));
        
        // Column widths
        reportTable.getColumnModel().getColumn(0).setPreferredWidth(120);  // Supplier
        reportTable.getColumnModel().getColumn(1).setPreferredWidth(180);  // Product
        reportTable.getColumnModel().getColumn(2).setPreferredWidth(100);  // Revenue Before
        reportTable.getColumnModel().getColumn(3).setPreferredWidth(100);  // Revenue After
        reportTable.getColumnModel().getColumn(4).setPreferredWidth(80);   // Revenue Δ%
        reportTable.getColumnModel().getColumn(5).setPreferredWidth(100);  // Target Before
        reportTable.getColumnModel().getColumn(6).setPreferredWidth(100);  // Target After
        reportTable.getColumnModel().getColumn(7).setPreferredWidth(80);   // Target Δ%
        
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
                        } else if (strValue.startsWith("-")) {
                            c.setForeground(new Color(200, 0, 0)); // Red for negative
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
    
    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.setBackground(new Color(0, 153, 153));
        
        // Refresh button
        refreshButton = new JButton("Load Report Data");
        refreshButton.setPreferredSize(new Dimension(150, 35));
        refreshButton.setBackground(new Color(102, 153, 255));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFont(new Font("Arial", Font.BOLD, 12));
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
        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportReport();
            }
        });
        
        // Back button
        backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(100, 35));
        backButton.setBackground(new Color(150, 150, 150));
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Arial", Font.BOLD, 12));
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardSequencePanel.removeAll();
                ((java.awt.CardLayout) cardSequencePanel.getLayout()).previous(cardSequencePanel);
            }
        });
        
        panel.add(refreshButton);
        panel.add(exportButton);
        panel.add(backButton);
        
        return panel;
    }
    
    /**
     * Load report data - checks if before/after states exist
     */
    public void loadReportData() {
        if (!report.hasBeforeState() || !report.hasAfterState()) {
            JOptionPane.showMessageDialog(this,
                "No report data available.\n\n" +
                "To generate a report:\n" +
                "1. Go to 'Manage Prices'\n" +
                "2. Run 'Task 4: Run Price Simulation' or 'Task 5: Optimize Profit Margins'\n" +
                "3. The report will be automatically generated after simulation/optimization",
                "No Data Available",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Clear existing data
        tableModel.setRowCount(0);
        
        // Load summary
        ReportSummary summary = report.getSummary();
        summaryTextArea.setText(generateSummaryText(summary));
        
        // Load product data based on current filter
        applyFilter();
        
        JOptionPane.showMessageDialog(this,
            "Report loaded successfully!\n" +
            "Total products: " + summary.totalProducts + "\n" +
            "Products changed: " + summary.productsChanged,
            "Success",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private String generateSummaryText(ReportSummary summary) {
        StringBuilder sb = new StringBuilder();
        sb.append("═══════════════════════════════════════════════════════════════════\n");
        sb.append("                   EXECUTIVE SUMMARY\n");
        sb.append("═══════════════════════════════════════════════════════════════════\n");
        sb.append(String.format("Total Products Analyzed:      %,d\n", summary.totalProducts));
        sb.append(String.format("Products with Price Changes:  %,d  (↑ %d  ↓ %d)\n\n", 
            summary.productsChanged, 
            summary.productsIncreased, 
            summary.productsDecreased));
        sb.append(String.format("Total Revenue Before:         %s\n", 
            currencyFormat.format(summary.totalRevenueBefore)));
        sb.append(String.format("Total Revenue After:          %s\n", 
            currencyFormat.format(summary.totalRevenueAfter)));
        sb.append(String.format("Revenue Change:               %s  (%+.2f%%)\n\n", 
            currencyFormat.format(summary.getTotalRevenueChange()),
            summary.getTotalRevenueChangePercent()));
        sb.append(String.format("Total Profit Before:          %s\n", 
            currencyFormat.format(summary.totalProfitBefore)));
        sb.append(String.format("Total Profit After:           %s\n", 
            currencyFormat.format(summary.totalProfitAfter)));
        sb.append(String.format("Profit Change:                %s  (%+.2f%%)\n", 
            currencyFormat.format(summary.getTotalProfitChange()),
            summary.getTotalProfitChangePercent()));
        
        return sb.toString();
    }
    
    private void applyFilter() {
        tableModel.setRowCount(0);
        
        int filterIndex = filterComboBox.getSelectedIndex();
        List<ProductComparisonData> dataList = report.getAllProductData();
        
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
            }
        }
    }
    
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
    
    private String formatPercent(double percent) {
        if (percent > 0) {
            return String.format("+%.2f%%", percent);
        } else if (percent < 0) {
            return String.format("%.2f%%", percent);
        } else {
            return "0.00%";
        }
    }
    
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
            }
        }
    }
    
    /**
     * Set the report object (called after simulation/optimization)
     */
    public void setReport(ProductPerformanceReport report) {
        this.report = report;
        loadReportData();
    }
}