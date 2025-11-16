/*
 * ProductPerformanceReport - Generates comprehensive product performance reports
 * Integrates with Simulation class to track before/after price adjustments
 * Task 6: Generate Final Product Performance Report
 * 
 * @author [Your Name]
 * @date 2025-11-15
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
            this.productName = product.toString();
            this.supplierName = supplierName;
            this.targetPrice = product.getTargetPrice();
            this.salesAbove = product.getNumberOfProductSalesAboveTarget();
            this.salesBelow = product.getNumberOfProductSalesBelowTarget();
            this.revenue = product.getSalesVolume();
            this.profit = product.getOrderPricePerformance();
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
        this.business = business;
        this.beforeState = new HashMap<>();
        this.afterState = new HashMap<>();
    }
    
    /**
     * Capture BEFORE state - call this before running simulation/optimization
     */
    public void captureBeforeState() {
        beforeState.clear();
        
        for (Supplier supplier : business.getSupplierDirectory().getSuplierList()) {
            String supplierName = supplier.getName();
            for (Product product : supplier.getProductCatalog().getProductList()) {
                beforeState.put(product, new ProductSnapshot(product, supplierName));
            }
        }
        
        hasBeforeState = true;
        System.out.println("✓ Captured BEFORE state for " + beforeState.size() + " products");
    }
    
    /**
     * Capture AFTER state - call this after running simulation/optimization
     */
    public void captureAfterState() {
        afterState.clear();
        
        for (Supplier supplier : business.getSupplierDirectory().getSuplierList()) {
            String supplierName = supplier.getName();
            for (Product product : supplier.getProductCatalog().getProductList()) {
                afterState.put(product, new ProductSnapshot(product, supplierName));
            }
        }
        
        hasAfterState = true;
        System.out.println("✓ Captured AFTER state for " + afterState.size() + " products");
    }
    
    /**
     * Get all product comparison data
     */
    public List<ProductComparisonData> getAllProductData() {
        List<ProductComparisonData> dataList = new ArrayList<>();
        
        if (!hasBeforeState || !hasAfterState) {
            System.err.println("WARNING: Need both before and after states!");
            return dataList;
        }
        
        for (Product product : beforeState.keySet()) {
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
     */
    public List<ProductComparisonData> getChangedProducts() {
        List<ProductComparisonData> changed = new ArrayList<>();
        
        for (ProductComparisonData data : getAllProductData()) {
            if (data.getTargetPriceChange() != 0) {
                changed.add(data);
            }
        }
        
        return changed;
    }
    
    /**
     * Get summary statistics
     */
    public ReportSummary getSummary() {
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
            totalProducts = allData.size();
            
            for (ProductComparisonData data : allData) {
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
     */
    public String generateTextReport() {
        StringBuilder report = new StringBuilder();
        
        report.append("=".repeat(120)).append("\n");
        report.append("                    FINAL PRODUCT PERFORMANCE REPORT\n");
        report.append("=".repeat(120)).append("\n\n");
        
        // Summary section
        ReportSummary summary = getSummary();
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
        if (!changedProducts.isEmpty()) {
            report.append("PRODUCTS WITH TARGET PRICE ADJUSTMENTS:\n");
            report.append("-".repeat(120)).append("\n");
            report.append(String.format("%-20s %-30s %12s %12s %10s\n",
                "Supplier", "Product", "Target Before", "Target After", "Change %"));
            report.append("-".repeat(120)).append("\n");
            
            for (ProductComparisonData data : changedProducts) {
                report.append(String.format("%-20s %-30s $%,10d $%,10d %9.1f%%\n",
                    truncate(data.supplierName, 20),
                    truncate(data.productName, 30),
                    data.targetPriceBefore,
                    data.targetPriceAfter,
                    data.getTargetPriceChangePercent()
                ));
            }
        }
        
        report.append("=".repeat(120)).append("\n");
        return report.toString();
    }
    
    private String truncate(String str, int length) {
        if (str.length() <= length) return str;
        return str.substring(0, length - 3) + "...";
    }
    
    public boolean hasBeforeState() { return hasBeforeState; }
    public boolean hasAfterState() { return hasAfterState; }
}