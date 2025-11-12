/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TheBusiness.Business;

import TheBusiness.ProductManagement.Product;
import TheBusiness.Supplier.Supplier;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * Task 5: Profit Margin Rate Optimizer
 * Modified to optimize MARGIN RATE instead of absolute profit (per TA feedback)
 * 
 * @author 123
 */
public class ProfitOptimizer {
    private Business business;
    private Simulation simulation;
    
    // 🔧 CHANGED: Optimize margin rate (%) instead of profit ($)
    private double bestMarginRate;
    private double initialMarginRate;  // 🔧 NEW
    private HashMap<Product, Integer> bestPrices;
    
    // Optimization parameters
    private int iterationCount = 0;
    private int maxIterations = 50;
    private int noImprovementCount = 0;
    private int maxNoImprovement = 5;
    private double adjustmentRate = 0.05; // 5% adjustment per iteration
    
    // Statistics
    private int totalAdjustments = 0;
    private ArrayList<String> optimizationLog;
    
    public ProfitOptimizer(Business business) {
        this.business = business;
        this.simulation = new Simulation(business);
        this.bestPrices = new HashMap<>();
        this.optimizationLog = new ArrayList<>();
    }
    
    /**
     * Main optimization method
     * Runs iterative optimization to maximize margin rate
     */
    public void optimize() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("       TASK 5: MARGIN RATE OPTIMIZATION");  // 🔧 CHANGED
        System.out.println("       Target: Maximize Profit Margin Rate");  // 🔧 NEW
        System.out.println("=".repeat(60));
        
        // Save initial state
        simulation.saveCurrentState();
        bestMarginRate = simulation.getBeforeMarginRate();  // 🔧 CHANGED
        initialMarginRate = bestMarginRate;  // 🔧 NEW
        saveBestPrices();
        
        log("Initial margin rate: " + String.format("%.2f%%", initialMarginRate));  // 🔧 CHANGED
        log("Starting optimization with max " + maxIterations + " iterations");
        System.out.println("\nInitial Margin Rate: " + String.format("%.2f%%", initialMarginRate));  // 🔧 CHANGED
        System.out.println("Starting optimization...\n");
        
        // Main optimization loop
        while (iterationCount < maxIterations && 
               noImprovementCount < maxNoImprovement) {
            
            iterationCount++;
            System.out.println("--- Iteration " + iterationCount + " ---");
            
            // Generate price suggestions based on product performance
            HashMap<Product, Integer> suggestions = generateSmartPriceSuggestions();
            
            if (suggestions.isEmpty()) {
                log("No valid adjustments found. Stopping.");
                System.out.println("No more valid price adjustments available.");
                break;
            }
            
            // Apply suggestions and simulate
            for (Product product : suggestions.keySet()) {
                simulation.applyNewPrice(product, suggestions.get(product));
            }
            simulation.calculateNewResults();
            
            double newMarginRate = simulation.getAfterMarginRate();  // 🔧 CHANGED
            double improvement = newMarginRate - bestMarginRate;  // 🔧 CHANGED
            
            System.out.println("Adjusted " + suggestions.size() + " product(s)");
            System.out.println("New Margin Rate: " + String.format("%.2f%%", newMarginRate));  // 🔧 CHANGED
            
            if (newMarginRate > bestMarginRate) {  // 🔧 CHANGED
                // Improvement found!
                System.out.println("✓ Improvement: +" + String.format("%.2f%%", improvement));  // 🔧 CHANGED
                log("Iteration " + iterationCount + ": Margin rate improved by " + 
                    String.format("%.2f%%", improvement));  // 🔧 CHANGED
                
                bestMarginRate = newMarginRate;  // 🔧 CHANGED
                saveBestPrices();
                noImprovementCount = 0;
                totalAdjustments += suggestions.size();
                
            } else {
                // No improvement, revert
                System.out.println("✗ No improvement. Reverting changes.");
                log("Iteration " + iterationCount + ": No improvement, reverted");
                
                simulation.restore();
                applyBestPrices();
                noImprovementCount++;
            }
            
            System.out.println("No improvement count: " + noImprovementCount + "/" + maxNoImprovement);
            System.out.println();
        }
        
        // Apply final best prices
        applyBestPrices();
        
        // Print results
        printOptimizationResults();  // 🔧 CHANGED: removed parameter
    }
    
    /**
     * Generate smart price suggestions based on sales performance
     */
    private HashMap<Product, Integer> generateSmartPriceSuggestions() {
        HashMap<Product, Integer> suggestions = new HashMap<>();
        
        for (Supplier supplier : business.getSupplierDirectory().getSuplierList()) {
            for (Product product : supplier.getProductCatalog().getProductList()) {
                
                int aboveCount = product.getNumberOfProductSalesAboveTarget();
                int belowCount = product.getNumberOfProductSalesBelowTarget();
                int totalSales = aboveCount + belowCount;
                
                // Skip products with no sales history
                if (totalSales == 0) continue;
                
                int currentTarget = product.getTargetPrice();
                int floor = product.getFloorPrice();
                int ceiling = product.getCeilingPrice();
                
                // Calculate performance ratio
                double aboveRatio = (double) aboveCount / totalSales;
                
                int newTarget = currentTarget;
                
                // Decision logic based on sales performance
                if (aboveRatio >= 0.7) {
                    // 70%+ sales above target → increase price significantly
                    newTarget = (int) (currentTarget * (1 + adjustmentRate * 2));
                    
                } else if (aboveRatio >= 0.5) {
                    // 50-70% sales above target → increase price moderately
                    newTarget = (int) (currentTarget * (1 + adjustmentRate));
                    
                } else if (aboveRatio <= 0.3) {
                    // 30% or less above target → decrease price significantly
                    newTarget = (int) (currentTarget * (1 - adjustmentRate * 2));
                    
                } else if (aboveRatio < 0.5) {
                    // 30-50% above target → decrease price moderately
                    newTarget = (int) (currentTarget * (1 - adjustmentRate));
                }
                
                // Ensure new price is within valid range
                if (newTarget < floor) newTarget = floor;
                if (newTarget > ceiling) newTarget = ceiling;
                
                // Only suggest if price actually changed
                if (newTarget != currentTarget) {
                    suggestions.put(product, newTarget);
                }
            }
        }
        
        return suggestions;
    }
    
    /**
     * Save current prices as best prices
     */
    private void saveBestPrices() {
        bestPrices.clear();
        for (Supplier supplier : business.getSupplierDirectory().getSuplierList()) {
            for (Product product : supplier.getProductCatalog().getProductList()) {
                bestPrices.put(product, product.getTargetPrice());
            }
        }
    }
    
    /**
     * Apply the best prices found during optimization
     */
    private void applyBestPrices() {
        for (Product product : bestPrices.keySet()) {
            int bestPrice = bestPrices.get(product);
            product.updateProduct(
                product.getFloorPrice(),
                product.getCeilingPrice(),
                bestPrice
            );
        }
    }
    
    /**
     * 🔧 MODIFIED: Print final optimization results (now shows margin rate)
     */
    private void printOptimizationResults() {
        double finalMarginRate = bestMarginRate;  // 🔧 CHANGED
        double totalImprovement = finalMarginRate - initialMarginRate;  // 🔧 CHANGED
        double improvementPercent = 0.0;
        
        if (initialMarginRate != 0) {
            improvementPercent = (totalImprovement / initialMarginRate) * 100;  // 🔧 CHANGED
        }
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("       OPTIMIZATION COMPLETE!");
        System.out.println("=".repeat(60));
        
        System.out.println("\nRESULTS:");
        System.out.println("  Total Iterations: " + iterationCount);
        System.out.println("  Total Price Adjustments: " + totalAdjustments);
        System.out.println();
        
        System.out.println("MARGIN RATE:");  // 🔧 CHANGED from PROFIT
        System.out.println("  Initial:  " + String.format("%.2f%%", initialMarginRate));  // 🔧 CHANGED
        System.out.println("  Final:    " + String.format("%.2f%%", finalMarginRate));  // 🔧 CHANGED
        System.out.println("  Change:   " + (totalImprovement >= 0 ? "+" : "") + 
                          String.format("%.2f%%", totalImprovement));  // 🔧 CHANGED
        System.out.println("  Improvement: " + String.format("%.2f%%", improvementPercent));
        System.out.println();
        
        if (totalImprovement > 0) {
            System.out.println("✓ SUCCESS: Margin rate optimized by " + 
                             String.format("%.2f%%", totalImprovement));  // 🔧 CHANGED
        } else if (totalImprovement == 0) {
            System.out.println("→ Current prices are already optimal");
        } else {
            System.out.println("! Note: Optimization found no improvements");
        }
        
        System.out.println("\n" + "=".repeat(60));
        
        log("Optimization complete. Final margin rate: " + 
            String.format("%.2f%%", finalMarginRate));  // 🔧 CHANGED
    }
    
    /**
     * Get detailed product adjustments report
     */
    public String getDetailedReport() {
        StringBuilder report = new StringBuilder();
        report.append("\nDETAILED PRICE ADJUSTMENTS:\n");
        report.append("=".repeat(70)).append("\n");
        
        int changedCount = 0;
        
        for (Supplier supplier : business.getSupplierDirectory().getSuplierList()) {
            for (Product product : supplier.getProductCatalog().getProductList()) {
                
                if (bestPrices.containsKey(product)) {
                    int bestPrice = bestPrices.get(product);
                    int originalPrice = product.getTargetPrice();
                    
                    if (bestPrice != originalPrice) {
                        changedCount++;
                        int change = bestPrice - originalPrice;
                        double changePercent = ((double) change / originalPrice) * 100;
                        
                        report.append(String.format("%-25s: $%,7d -> $%,7d (%+.1f%%)\n",
                            product.toString(),
                            originalPrice,
                            bestPrice,
                            changePercent
                        ));
                    }
                }
            }
        }
        
        if (changedCount == 0) {
            report.append("No price changes recommended.\n");
        } else {
            report.append("\nTotal products with price changes: ").append(changedCount).append("\n");
        }
        
        return report.toString();
    }
    
    /**
     * Add entry to optimization log
     */
    private void log(String message) {
        optimizationLog.add(message);
    }
    
    /**
     * Get complete optimization log
     */
    public ArrayList<String> getOptimizationLog() {
        return new ArrayList<>(optimizationLog);
    }
    
    // 🔧 MODIFIED: Getters now return margin rate instead of profit
    public double getBestMarginRate() { return bestMarginRate; }  // 🔧 CHANGED
    public double getInitialMarginRate() { return initialMarginRate; }  // 🔧 NEW
    public int getIterationCount() { return iterationCount; }
    public int getTotalAdjustments() { return totalAdjustments; }
    public HashMap<Product, Integer> getBestPrices() { return new HashMap<>(bestPrices); }
    
    // Setters for tuning optimization parameters
    public void setMaxIterations(int max) { this.maxIterations = max; }
    public void setMaxNoImprovement(int max) { this.maxNoImprovement = max; }
    public void setAdjustmentRate(double rate) { this.adjustmentRate = rate; }
}