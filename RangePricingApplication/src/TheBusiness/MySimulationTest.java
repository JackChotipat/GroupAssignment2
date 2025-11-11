/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TheBusiness;

import TheBusiness.Business.Business;
import TheBusiness.ProductManagement.Product;
import TheBusiness.Supplier.Supplier;

/**
 *
 * @author 123
 */
public class MySimulationTest {
    public static void main(String[] args) {
        // Use hardcode data from skeleton
        Business business = ConfigureABusiness.initialize();
        
        System.out.println("========== View Existing Data ==========");
        
        // Check how many suppliers
        int supplierCount = business.getSupplierDirectory().getSuplierList().size();
        System.out.println("Number of Suppliers: " + supplierCount);
        
        // Check first supplier's products
        Supplier firstSupplier = business.getSupplierDirectory()
            .getSuplierList().get(0);
        System.out.println("\nFirst Supplier: " + firstSupplier.getName());
        
        int productCount = firstSupplier.getProductCatalog().getProductList().size();
        System.out.println("Number of Products: " + productCount);
        
        // Check first product info
        Product firstProduct = firstSupplier.getProductCatalog()
            .getProductList().get(0);
        System.out.println("\nFirst Product: " + firstProduct);
        System.out.println("Floor Price: $" + firstProduct.getFloorPrice());
        System.out.println("Target Price: $" + firstProduct.getTargetPrice());
        System.out.println("Ceiling Price: $" + firstProduct.getCeilingPrice());
        
        // Check product sales performance
        System.out.println("\nSales Performance:");
        System.out.println("Sales above target: " + 
            firstProduct.getNumberOfProductSalesAboveTarget());
        System.out.println("Sales below target: " + 
            firstProduct.getNumberOfProductSalesBelowTarget());
        System.out.println("Price performance (profit): $" + 
            firstProduct.getOrderPricePerformance());
        
        // Check company total revenue
        System.out.println("\n========== Company Overall Data ==========");
        System.out.println("Total Sales Volume: $" + business.getSalesVolume());
        
        System.out.println("\n✓ Data loaded successfully! Ready to develop!");
    }
}
