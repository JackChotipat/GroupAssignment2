/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TheBusiness;

import TheBusiness.Business.Business;
import TheBusiness.Business.Simulation;
import TheBusiness.ProductManagement.Product;
import TheBusiness.Supplier.Supplier;

/**
 *
 * @author 123
 */
public class MySimulationTest {
    public static void main(String[] args) {
        
        // First, let's see the data
        viewData();
        
        // Then test the simulation
        testSimulation();
    }
    
    public static void viewData() {
        Business business = ConfigureABusiness.initialize();
        
        System.out.println("\n" + "=".repeat(50));
        System.out.println("          VIEW EXISTING DATA");
        System.out.println("=".repeat(50));
        
        // Check suppliers
        int supplierCount = business.getSupplierDirectory().getSuplierList().size();
        System.out.println("\nNumber of Suppliers: " + supplierCount);
        
        // Check first supplier
        Supplier firstSupplier = business.getSupplierDirectory()
            .getSuplierList().get(0);
        System.out.println("\nFirst Supplier: " + firstSupplier.getName());
        
        int productCount = firstSupplier.getProductCatalog().getProductList().size();
        System.out.println("Number of Products: " + productCount);
        
        // Check first product details
        Product firstProduct = firstSupplier.getProductCatalog()
            .getProductList().get(0);
        System.out.println("\nFirst Product: " + firstProduct);
        System.out.println("  Floor Price: $" + firstProduct.getFloorPrice());
        System.out.println("  Target Price: $" + firstProduct.getTargetPrice());
        System.out.println("  Ceiling Price: $" + firstProduct.getCeilingPrice());
        
        // Sales performance
        System.out.println("\nSales Performance:");
        System.out.println("  Sales above target: " + 
            firstProduct.getNumberOfProductSalesAboveTarget());
        System.out.println("  Sales below target: " + 
            firstProduct.getNumberOfProductSalesBelowTarget());
        System.out.println("  Price performance (profit): $" + 
            firstProduct.getOrderPricePerformance());
        
        // Company totals
        System.out.println("\nCompany Total Sales Volume: $" + business.getSalesVolume());
        
        System.out.println("\n✓ Data loaded successfully!");
    }
    
    public static void testSimulation() {
        System.out.println("\n\n" + "=".repeat(50));
        System.out.println("          TESTING SIMULATION (TASK 4)");
        System.out.println("=".repeat(50));
        
        Business business = ConfigureABusiness.initialize();
        
        // Create Simulation object
        Simulation sim = new Simulation(business);
        
        // Get the first product (Scanner 3 - which had 3 sales above target!)
        Supplier firstSupplier = business.getSupplierDirectory()
            .getSuplierList().get(0);
        Product testProduct = firstSupplier.getProductCatalog()
            .getProductList().get(0);
        
        System.out.println("\nTest Product Selected: " + testProduct);
        System.out.println("This product had 3 sales ABOVE target price");
        System.out.println("So let's try INCREASING the target price by 10%");
        
        // STEP 1: Save current state
        sim.saveCurrentState();
        
        // STEP 2: Increase target price by 10%
        int currentTarget = testProduct.getTargetPrice();
        int newTarget = (int)(currentTarget * 1.10); // Increase by 10%
        
        System.out.println("\nCalculation: $" + currentTarget + " * 1.10 = $" + newTarget);
        
        sim.applyNewPrice(testProduct, newTarget);
        
        // STEP 3: Calculate new results
        sim.calculateNewResults();
        
        // STEP 4: Show comparison
        sim.showComparison();
        
        // STEP 5: Demonstrate restore functionality
        System.out.println("\n\nDemonstrating RESTORE functionality...");
        sim.restore();
        
        System.out.println("\n" + "=".repeat(50));
        System.out.println("✓ SIMULATION TEST COMPLETED!");
        System.out.println("=".repeat(50));
    }
}
