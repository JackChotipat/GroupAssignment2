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
    
}
