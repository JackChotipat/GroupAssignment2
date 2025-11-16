/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TheBusiness.Business;

import TheBusiness.ProductManagement.Product;
import TheBusiness.Supplier.Supplier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author User PC
 */
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
    
}
