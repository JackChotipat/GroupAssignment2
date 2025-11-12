/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TheBusiness.Business;

import TheBusiness.ProductManagement.Product;
import TheBusiness.Supplier.Supplier;
import java.util.HashMap;

/**
 * Task 4: Price Simulation Engine
 * Modified to include Margin Rate calculation (per TA feedback)
 * 
 * @author 123
 */
public class Simulation {
    private Business business;
    
    // Store original target prices
    private HashMap<Product, Integer> originalPrices;
    
    // Store simulation results
    private int beforeRevenue;
    private int afterRevenue;
    private int beforeProfit;
    private int afterProfit;
    
    // 🔧 NEW: Margin Rate metrics (as per TA feedback)
    private double beforeMarginRate;
    private double afterMarginRate;
    
    public Simulation(Business business) {
        this.business = business;
        this.originalPrices = new HashMap<>();
    }
    
    // Step 1: Save current state
    public void saveCurrentState() {
        originalPrices.clear();
        
        // Loop through all products and save their target prices
        for (Supplier supplier : business.getSupplierDirectory().getSuplierList()) {
            for (Product product : supplier.getProductCatalog().getProductList()) {
                originalPrices.put(product, product.getTargetPrice());
            }
        }
        
        // Calculate current revenue and profit
        beforeRevenue = business.getSalesVolume();
        beforeProfit = calculateTotalProfit();
        beforeMarginRate = calculateAverageMarginRate();  // 🔧 NEW
        
        System.out.println("=== STEP 1: Current State Saved ===");
        System.out.println("Current Revenue: $" + beforeRevenue);
        System.out.println("Current Profit: $" + beforeProfit);
        System.out.println("Current Margin Rate: " + String.format("%.2f%%", beforeMarginRate));  // 🔧 NEW
    }
    
    // Step 2: Apply new price to a product
    public void applyNewPrice(Product product, int newTargetPrice) {
        System.out.println("\n=== STEP 2: Adjusting Price ===");
        System.out.println("Product: " + product);
        System.out.println("Old Target Price: $" + product.getTargetPrice());
        System.out.println("New Target Price: $" + newTargetPrice);
        
        // Update product's target price
        product.updateProduct(
            product.getFloorPrice(),
            product.getCeilingPrice(),
            newTargetPrice
        );
        
        System.out.println("✓ Price adjusted successfully");
    }
    
    // Step 3: Calculate new results after price change
    public void calculateNewResults() {
        afterRevenue = business.getSalesVolume();
        afterProfit = calculateTotalProfit();
        afterMarginRate = calculateAverageMarginRate();  // 🔧 NEW
        
        System.out.println("\n=== STEP 3: New Results Calculated ===");
        System.out.println("New Revenue: $" + afterRevenue);
        System.out.println("New Profit: $" + afterProfit);
        System.out.println("New Margin Rate: " + String.format("%.2f%%", afterMarginRate));  // 🔧 NEW
    }
    
    // Step 4: Show detailed comparison
    public void showComparison() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("       SIMULATION RESULTS COMPARISON");
        System.out.println("=".repeat(50));
        
        // Revenue comparison
        int revenueDiff = afterRevenue - beforeRevenue;
        System.out.println("\nREVENUE:");
        System.out.println("  Before: $" + beforeRevenue);
        System.out.println("  After:  $" + afterRevenue);
        System.out.println("  Change: " + (revenueDiff >= 0 ? "+" : "") + "$" + revenueDiff);
        
        // Profit comparison
        int profitDiff = afterProfit - beforeProfit;
        System.out.println("\nPROFIT:");
        System.out.println("  Before: $" + beforeProfit);
        System.out.println("  After:  $" + afterProfit);
        System.out.println("  Change: " + (profitDiff >= 0 ? "+" : "") + "$" + profitDiff);
        
        // 🔧 NEW: Margin Rate comparison (KEY METRIC per TA feedback)
        double marginRateDiff = afterMarginRate - beforeMarginRate;
        System.out.println("\nMARGIN RATE (Key Optimization Target):");
        System.out.println("  Before: " + String.format("%.2f%%", beforeMarginRate));
        System.out.println("  After:  " + String.format("%.2f%%", afterMarginRate));
        System.out.println("  Change: " + (marginRateDiff >= 0 ? "+" : "") + 
                         String.format("%.2f%%", marginRateDiff));
        
        // 🔧 MODIFIED: Recommendation based on Margin Rate (per TA feedback)
        System.out.println("\n" + "=".repeat(50));
        if (marginRateDiff > 0) {
            System.out.println("✓ RECOMMENDATION: APPLY NEW PRICES");
            System.out.println("  Margin rate increased by " + 
                             String.format("%.2f%%", marginRateDiff));
        } else if (marginRateDiff < 0) {
            System.out.println("✗ RECOMMENDATION: KEEP ORIGINAL PRICES");
            System.out.println("  Margin rate decreased by " + 
                             String.format("%.2f%%", Math.abs(marginRateDiff)));
        } else {
            System.out.println("→ RECOMMENDATION: NO CHANGE");
            System.out.println("  Margin rate remains the same");
        }
        System.out.println("=".repeat(50));
    }
    
    // Helper method: Calculate total profit across all products
    private int calculateTotalProfit() {
        int total = 0;
        for (Supplier supplier : business.getSupplierDirectory().getSuplierList()) {
            for (Product product : supplier.getProductCatalog().getProductList()) {
                total += product.getOrderPricePerformance();
            }
        }
        return total;
    }
    
    // 🔧 NEW: Calculate average margin rate across all orders
    /**
     * Margin Rate = (Revenue - Cost) / Revenue * 100
     * 
     * Note: We approximate cost using floor price since actual cost
     * data is not directly available in the current data model.
     */
    private double calculateAverageMarginRate() {
        int totalRevenue = 0;
        int totalCost = 0;
        
        // Calculate total revenue and cost from all products
        for (Supplier supplier : business.getSupplierDirectory().getSuplierList()) {
            for (Product product : supplier.getProductCatalog().getProductList()) {
                // Get product's contribution to revenue
                int productRevenue = product.getSalesVolume();
                
                // Approximate cost using floor price as cost basis
                // This is a simplification: actual cost = floor price
                int targetPrice = product.getTargetPrice();
                if (targetPrice > 0) {
                    int estimatedQuantity = productRevenue / targetPrice;
                    int productCost = product.getFloorPrice() * estimatedQuantity;
                    
                    totalRevenue += productRevenue;
                    totalCost += productCost;
                }
            }
        }
        
        // Avoid division by zero
        if (totalRevenue == 0) {
            return 0.0;
        }
        
        // Calculate margin rate
        int margin = totalRevenue - totalCost;
        double marginRate = ((double) margin / totalRevenue) * 100.0;
        
        return marginRate;
    }
    
    // 🔧 NEW: Calculate margin rate for a specific product
    /**
     * Used by ProfitOptimizer to make smart pricing decisions
     */
    public double calculateProductMarginRate(Product product) {
        int targetPrice = product.getTargetPrice();
        int floor = product.getFloorPrice();
        
        // Avoid division by zero
        if (targetPrice == 0) {
            return 0.0;
        }
        
        // Calculate margin rate
        int margin = targetPrice - floor;
        double marginRate = ((double) margin / targetPrice) * 100.0;
        
        return marginRate;
    }
    
    // Step 5: Restore original prices if needed
    public void restore() {
        System.out.println("\n=== STEP 5: Restoring Original Prices ===");
        for (Product product : originalPrices.keySet()) {
            int originalPrice = originalPrices.get(product);
            product.updateProduct(
                product.getFloorPrice(),
                product.getCeilingPrice(),
                originalPrice
            );
        }
        System.out.println("✓ All prices restored to original values");
    }
    
    // Getters for UI (will be used later)
    public int getBeforeRevenue() { return beforeRevenue; }
    public int getAfterRevenue() { return afterRevenue; }
    public int getBeforeProfit() { return beforeProfit; }
    public int getAfterProfit() { return afterProfit; }
    public int getRevenueChange() { return afterRevenue - beforeRevenue; }
    public int getProfitChange() { return afterProfit - beforeProfit; }
    
    // 🔧 NEW: Margin Rate getters (KEY METRICS)
    public double getBeforeMarginRate() { return beforeMarginRate; }
    public double getAfterMarginRate() { return afterMarginRate; }
    public double getMarginRateChange() { return afterMarginRate - beforeMarginRate; }
}