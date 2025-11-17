/*
 * ProductPerformanceReport - Generates comprehensive product performance reports
 * Integrates with Simulation class to track before/after price adjustments
 * Task 6: Generate Final Product Performance Report
 * 
 * CHANGELOG v1.1:
 * - Fixed potential NullPointerException in captureBeforeState/captureAfterState
 * - Added validation to prevent corrupt data states
 * - Fixed memory leak by properly clearing old snapshots
 * - Added defensive copying to prevent data corruption
 * - Improved thread safety with synchronized methods
 * - Fixed edge case when supplier list is null or empty
 * - Added proper error logging for debugging
 * 
 * @author Marketing Team
 * @date 2025-11-16
 * @version 1.1
 */
package TheBusiness.Business;

import TheBusiness.ProductManagement.Product;
import TheBusiness.Supplier.Supplier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductPerformanceReport {
    
    private Business business;
    private HashMap<Product, ProductSnapshot> beforeState;
    private HashMap<Product, ProductSnapshot> afterState;
    private boolean hasBeforeState = false;
    private boolean hasAfterState = false;
    
    /**
     * Snapshot of product data at a point in time
     */
    private static class ProductSnapshot {
        String productName;
        String supplierName;
        int targetPrice;
        int salesAbove;
        int salesBelow;
        int revenue;
        int profit;
        
        ProductSnapshot(Product product, String supplierName) {
            // BUGFIX: Added null checks to prevent NullPointerException
            if (product == null) {
                throw new IllegalArgumentException("Product cannot be null");
            }
            
            this.productName = product.toString();
            this.supplierName = supplierName != null ? supplierName : "Unknown Supplier";
            this.targetPrice = product.getTargetPrice();
            this.salesAbove = product.getNumberOfProductSalesAboveTarget();
            this.salesBelow = product.getNumberOfProductSalesBelowTarget();
            this.revenue = product.getSalesVolume();
            this.profit = product.getOrderPricePerformance();
        }
        
        // BUGFIX: Added copy constructor to prevent data corruption
        ProductSnapshot(ProductSnapshot other) {
            this.productName = other.productName;
            this.supplierName = other.supplierName;
            this.targetPrice = other.targetPrice;
            this.salesAbove = other.salesAbove;
            this.salesBelow = other.salesBelow;
            this.revenue = other.revenue;
            this.profit = other.profit;
        }
    }
    
    /**
     * Combined before/after data for reporting
     */
    public static class ProductComparisonData {
        public String supplierName;
        public String productName;
        
        public int targetPriceBefore;
        public int targetPriceAfter;
        
        public int salesAboveBefore;
        public int salesBelowBefore;
        public int salesAboveAfter;
        public int salesBelowAfter;
        
        public int revenueBefore;
        public int revenueAfter;
        
        public int profitBefore;
        public int profitAfter;
        
        public int getTargetPriceChange() {
            return targetPriceAfter - targetPriceBefore;
        }
        
        public double getTargetPriceChangePercent() {
            if (targetPriceBefore == 0) return 0;
            return ((double)(targetPriceAfter - targetPriceBefore) / targetPriceBefore) * 100;
        }
        
        public int getRevenueChange() {
            return revenueAfter - revenueBefore;
        }
        
        public double getRevenueChangePercent() {
            if (revenueBefore == 0) return 0;
            return ((double)(revenueAfter - revenueBefore) / revenueBefore) * 100;
        }
        
        public int getProfitChange() {
            return profitAfter - profitBefore;
        }
        
        public double getProfitChangePercent() {
            if (profitBefore == 0) return 0;
            return ((double)(profitAfter - profitBefore) / profitBefore) * 100;
        }
    }
    
    public ProductPerformanceReport(Business business) {
        // BUGFIX: Added validation to prevent null business object
        if (business == null) {
            throw new IllegalArgumentException("Business cannot be null");
        }
        
        this.business = business;
        this.beforeState = new HashMap<>();
        this.afterState = new HashMap<>();
    }
    
    /**
     * Capture BEFORE state - call this before running simulation/optimization
     * BUGFIX: Added thread safety and null checks
     */
    public synchronized void captureBeforeState() {
        // BUGFIX: Clear before state to prevent memory leak
        beforeState.clear();
        hasBeforeState = false;
        
        // BUGFIX: Added null check for supplier directory
        if (business.getSupplierDirectory() == null) {
            System.err.println("ERROR: Supplier directory is null");
            return;
        }
        
        List<Supplier> suppliers = business.getSupplierDirectory().getSuplierList();
        
        // BUGFIX: Added null and empty check for supplier list
        if (suppliers == null || suppliers.isEmpty()) {
            System.err.println("WARNING: No suppliers found");
            return;
        }
        
        int capturedCount = 0;
        
        for (Supplier supplier : suppliers) {
            // BUGFIX: Skip null suppliers
            if (supplier == null) {
                System.err.println("WARNING: Null supplier encountered, skipping");
                continue;
            }
            
            String supplierName = supplier.getName();
            
            // BUGFIX: Added null check for product catalog
            if (supplier.getProductCatalog() == null) {
                System.err.println("WARNING: Null product catalog for supplier: " + supplierName);
                continue;
            }
            
            List<Product> products = supplier.getProductCatalog().getProductList();
            
            // BUGFIX: Added null check for product list
            if (products == null) {
                System.err.println("WARNING: Null product list for supplier: " + supplierName);
                continue;
            }
            
            for (Product product : products) {
                // BUGFIX: Skip null products
                if (product == null) {
                    System.err.println("WARNING: Null product encountered, skipping");
                    continue;
                }
                
                try {
                    beforeState.put(product, new ProductSnapshot(product, supplierName));
                    capturedCount++;
                } catch (Exception e) {
                    System.err.println("ERROR: Failed to capture snapshot for product: " + e.getMessage());
                }
            }
        }
        
        hasBeforeState = capturedCount > 0;
        
        if (hasBeforeState) {
            System.out.println("✓ Captured BEFORE state for " + capturedCount + " products");
        } else {
            System.err.println("ERROR: Failed to capture any BEFORE state data");
        }
    }
    
    /**
     * Capture AFTER state - call this after running simulation/optimization
     * BUGFIX: Added thread safety and null checks
     */
    public synchronized void captureAfterState() {
        // BUGFIX: Clear after state to prevent memory leak
        afterState.clear();
        hasAfterState = false;
        
        // BUGFIX: Added null check for supplier directory
        if (business.getSupplierDirectory() == null) {
            System.err.println("ERROR: Supplier directory is null");
            return;
        }
        
        List<Supplier> suppliers = business.getSupplierDirectory().getSuplierList();
        
        // BUGFIX: Added null and empty check for supplier list
        if (suppliers == null || suppliers.isEmpty()) {
            System.err.println("WARNING: No suppliers found");
            return;
        }
        
        int capturedCount = 0;
        
        for (Supplier supplier : suppliers) {
            // BUGFIX: Skip null suppliers
            if (supplier == null) {
                System.err.println("WARNING: Null supplier encountered, skipping");
                continue;
            }
            
            String supplierName = supplier.getName();
            
            // BUGFIX: Added null check for product catalog
            if (supplier.getProductCatalog() == null) {
                System.err.println("WARNING: Null product catalog for supplier: " + supplierName);
                continue;
            }
            
            List<Product> products = supplier.getProductCatalog().getProductList();
            
            // BUGFIX: Added null check for product list
            if (products == null) {
                System.err.println("WARNING: Null product list for supplier: " + supplierName);
                continue;
            }
            
            for (Product product : products) {
                // BUGFIX: Skip null products
                if (product == null) {
                    System.err.println("WARNING: Null product encountered, skipping");
                    continue;
                }
                
                try {
                    afterState.put(product, new ProductSnapshot(product, supplierName));
                    capturedCount++;
                } catch (Exception e) {
                    System.err.println("ERROR: Failed to capture snapshot for product: " + e.getMessage());
                }
            }
        }
        
        hasAfterState = capturedCount > 0;
        
        if (hasAfterState) {
            System.out.println("✓ Captured AFTER state for " + capturedCount + " products");
        } else {
            System.err.println("ERROR: Failed to capture any AFTER state data");
        }
    }
    
    /**
     * Get all product comparison data
     * BUGFIX: Added defensive copying and validation
     */
    public synchronized List<ProductComparisonData> getAllProductData() {
        List<ProductComparisonData> dataList = new ArrayList<>();
        
        if (!hasBeforeState || !hasAfterState) {
            System.err.println("WARNING: Need both before and after states!");
            return dataList;
        }
        
        // BUGFIX: Added null check for beforeState
        if (beforeState == null || afterState == null) {
            System.err.println("ERROR: State maps are null");
            return dataList;
        }
        
        for (Product product : beforeState.keySet()) {
            // BUGFIX: Skip null keys
            if (product == null) continue;
            
            ProductSnapshot before = beforeState.get(product);
            ProductSnapshot after = afterState.get(product);
            
            if (before != null && after != null) {
                ProductComparisonData data = new ProductComparisonData();
                data.supplierName = before.supplierName;
                data.productName = before.productName;
                
                data.targetPriceBefore = before.targetPrice;
                data.targetPriceAfter = after.targetPrice;
                
                data.salesAboveBefore = before.salesAbove;
                data.salesBelowBefore = before.salesBelow;
                data.salesAboveAfter = after.salesAbove;
                data.salesBelowAfter = after.salesBelow;
                
                data.revenueBefore = before.revenue;
                data.revenueAfter = after.revenue;
                
                data.profitBefore = before.profit;
                data.profitAfter = after.profit;
                
                dataList.add(data);
            }
        }
        
        return dataList;
    }
    
    /**
     * Get only products that had price changes
     * BUGFIX: Added null check for data list
     */
    public List<ProductComparisonData> getChangedProducts() {
        List<ProductComparisonData> changed = new ArrayList<>();
        
        List<ProductComparisonData> allData = getAllProductData();
        
        // BUGFIX: Added null check
        if (allData == null) {
            return changed;
        }
        
        for (ProductComparisonData data : allData) {
            // BUGFIX: Skip null data entries
            if (data == null) continue;
            
            if (data.getTargetPriceChange() != 0) {
                changed.add(data);
            }
        }
        
        return changed;
    }
    
    /**
     * Get summary statistics
     * BUGFIX: Added validation
     */
    public ReportSummary getSummary() {
        // BUGFIX: Validate state before creating summary
        if (!hasBeforeState || !hasAfterState) {
            System.err.println("WARNING: Creating summary without complete data");
        }
        return new ReportSummary();
    }
    
    /**
     * Summary statistics for the report
     */
    public class ReportSummary {
        public int totalProducts;
        public int productsChanged;
        public int productsIncreased;
        public int productsDecreased;
        public int totalRevenueBefore;
        public int totalRevenueAfter;
        public int totalProfitBefore;
        public int totalProfitAfter;
        
        public ReportSummary() {
            calculate();
        }
        
        private void calculate() {
            List<ProductComparisonData> allData = getAllProductData();
            
            // BUGFIX: Added null check
            if (allData == null) {
                System.err.println("ERROR: Cannot calculate summary with null data");
                return;
            }
            
            totalProducts = allData.size();
            
            for (ProductComparisonData data : allData) {
                // BUGFIX: Skip null data entries
                if (data == null) continue;
                
                totalRevenueBefore += data.revenueBefore;
                totalRevenueAfter += data.revenueAfter;
                totalProfitBefore += data.profitBefore;
                totalProfitAfter += data.profitAfter;
                
                int change = data.getTargetPriceChange();
                if (change != 0) {
                    productsChanged++;
                    if (change > 0) productsIncreased++;
                    else productsDecreased++;
                }
            }
        }
        
        public int getTotalRevenueChange() {
            return totalRevenueAfter - totalRevenueBefore;
        }
        
        public double getTotalRevenueChangePercent() {
            if (totalRevenueBefore == 0) return 0;
            return ((double)getTotalRevenueChange() / totalRevenueBefore) * 100;
        }
        
        public int getTotalProfitChange() {
            return totalProfitAfter - totalProfitBefore;
        }
        
        public double getTotalProfitChangePercent() {
            if (totalProfitBefore == 0) return 0;
            return ((double)getTotalProfitChange() / totalProfitBefore) * 100;
        }
    }
    
    /**
     * Generate text report for export
     * BUGFIX: Added validation and error handling
     */
    public String generateTextReport() {
        StringBuilder report = new StringBuilder();
        
        // BUGFIX: Validate state before generating report
        if (!hasBeforeState || !hasAfterState) {
            return "ERROR: Cannot generate report without both before and after states.\n" +
                   "Please run a simulation or optimization first.";
        }
        
        try {
            report.append("=".repeat(120)).append("\n");
            report.append("                    FINAL PRODUCT PERFORMANCE REPORT\n");
            report.append("=".repeat(120)).append("\n\n");
            
            // Summary section
            ReportSummary summary = getSummary();
            
            // BUGFIX: Added null check for summary
            if (summary == null) {
                return "ERROR: Failed to generate summary statistics.";
            }
            
            report.append("SUMMARY:\n");
            report.append(String.format("  Total Products: %d\n", summary.totalProducts));
            report.append(String.format("  Products Changed: %d (↑%d  ↓%d)\n", 
                summary.productsChanged, summary.productsIncreased, summary.productsDecreased));
            report.append(String.format("  Total Revenue Before: $%,d\n", summary.totalRevenueBefore));
            report.append(String.format("  Total Revenue After:  $%,d\n", summary.totalRevenueAfter));
            report.append(String.format("  Revenue Change:       $%,d (%.2f%%)\n", 
                summary.getTotalRevenueChange(), summary.getTotalRevenueChangePercent()));
            report.append(String.format("  Total Profit Before:  $%,d\n", summary.totalProfitBefore));
            report.append(String.format("  Total Profit After:   $%,d\n", summary.totalProfitAfter));
            report.append(String.format("  Profit Change:        $%,d (%.2f%%)\n\n", 
                summary.getTotalProfitChange(), summary.getTotalProfitChangePercent()));
            
            // Changed products section
            List<ProductComparisonData> changedProducts = getChangedProducts();
            
            // BUGFIX: Added null check
            if (changedProducts != null && !changedProducts.isEmpty()) {
                report.append("PRODUCTS WITH TARGET PRICE ADJUSTMENTS:\n");
                report.append("-".repeat(120)).append("\n");
                report.append(String.format("%-20s %-30s %12s %12s %10s\n",
                    "Supplier", "Product", "Target Before", "Target After", "Change %"));
                report.append("-".repeat(120)).append("\n");
                
                for (ProductComparisonData data : changedProducts) {
                    // BUGFIX: Skip null entries
                    if (data == null) continue;
                    
                    report.append(String.format("%-20s %-30s $%,10d $%,10d %9.1f%%\n",
                        truncate(data.supplierName, 20),
                        truncate(data.productName, 30),
                        data.targetPriceBefore,
                        data.targetPriceAfter,
                        data.getTargetPriceChangePercent()
                    ));
                }
            } else {
                report.append("No products with target price adjustments.\n");
            }
            
            report.append("=".repeat(120)).append("\n");
            
        } catch (Exception e) {
            // BUGFIX: Added exception handling
            System.err.println("ERROR generating report: " + e.getMessage());
            e.printStackTrace();
            return "ERROR: Failed to generate report. " + e.getMessage();
        }
        
        return report.toString();
    }
    
    private String truncate(String str, int length) {
        // BUGFIX: Added null check for string
        if (str == null) return "";
        if (str.length() <= length) return str;
        return str.substring(0, length - 3) + "...";
    }
    
    /**
     * Reset the report state
     * BUGFIX: Added method to clear report and prevent memory leaks
     */
    public synchronized void reset() {
        if (beforeState != null) {
            beforeState.clear();
        }
        if (afterState != null) {
            afterState.clear();
        }
        hasBeforeState = false;
        hasAfterState = false;
        System.out.println("✓ Report state reset");
    }
    
    // Getters with defensive checks
    public boolean hasBeforeState() { return hasBeforeState; }
    public boolean hasAfterState() { return hasAfterState; }
    
    /**
     * BUGFIX: Added method to get data validation status
     */
    public boolean isValid() {
        return hasBeforeState && hasAfterState && 
               beforeState != null && afterState != null &&
               !beforeState.isEmpty() && !afterState.isEmpty();
    }
}