/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TheBusiness;

import TheBusiness.Business.Business;
import TheBusiness.Business.ProfitOptimizer;
/**
 *
 * @author 123
 */
public class TestOptimizer {
    public static void main(String[] args) {
        System.out.println("Loading business data...");
        Business business = ConfigureABusiness.initialize();
        
        System.out.println("Business loaded successfully!");
        System.out.println("Suppliers: " + business.getSupplierDirectory().getSuplierList().size());
        System.out.println("Initial Sales Volume: $" + 
            String.format("%,d", business.getSalesVolume()));
        
        // Create and run optimizer
        ProfitOptimizer optimizer = new ProfitOptimizer(business);
        
        // Optional: Adjust parameters
        // optimizer.setMaxIterations(30);
        // optimizer.setAdjustmentRate(0.10); // 10% adjustment
        
        // Run optimization
        optimizer.optimize();
        
        // Print detailed report
        System.out.println(optimizer.getDetailedReport());
        
        // Print optimization log
        System.out.println("\nOPTIMIZATION LOG:");
        System.out.println("=".repeat(60));
        for (String logEntry : optimizer.getOptimizationLog()) {
            System.out.println(logEntry);
        }
    }
}
