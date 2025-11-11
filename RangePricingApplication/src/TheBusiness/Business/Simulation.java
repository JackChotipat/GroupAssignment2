/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TheBusiness.Business;

import TheBusiness.ProductManagement.Product;
import TheBusiness.Supplier.Supplier;
import java.util.HashMap;

/**
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
        
        System.out.println("=== STEP 1: Current State Saved ===");
        System.out.println("Current Revenue: $" + beforeRevenue);
        System.out.println("Current Profit: $" + beforeProfit);
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
        
        System.out.println("\n=== STEP 3: New Results Calculated ===");
        System.out.println("New Revenue: $" + afterRevenue);
        System.out.println("New Profit: $" + afterProfit);
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
        
        // Recommendation
        System.out.println("\n" + "=".repeat(50));
        if (profitDiff > 0) {
            System.out.println("✓ RECOMMENDATION: APPLY NEW PRICES");
            System.out.println("  Profit increased by $" + profitDiff);
        } else if (profitDiff < 0) {
            System.out.println("✗ RECOMMENDATION: KEEP ORIGINAL PRICES");
            System.out.println("  Profit decreased by $" + Math.abs(profitDiff));
        } else {
            System.out.println("→ RECOMMENDATION: NO CHANGE");
            System.out.println("  Profit remains the same");
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
}
