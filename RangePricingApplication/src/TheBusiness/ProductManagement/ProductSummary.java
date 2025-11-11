package TheBusiness.ProductManagement;

import TheBusiness.OrderManagement.OrderItem;

/**
 * Summarizes product performance based directly on the product's orderitems list.
 * This supports ManageProductPerformanceDetail.java for browsing product price performance.
 * @author revised
 */
public class ProductSummary {

    private Product product;
    private double totalRevenue;
    private int numberAboveTarget;
    private int numberBelowTarget;
    private double pricePerformance; // % difference around target

    public ProductSummary(Product p) {
        this.product = p;
        analyzeProductPerformance();
    }

    private void analyzeProductPerformance() {
        totalRevenue = 0;
        numberAboveTarget = 0;
        numberBelowTarget = 0;

        for (OrderItem item : product.orderitems) {  // 直接從 product 拿
            totalRevenue += item.getActualPrice() * item.getQuantity();

            if (item.getActualPrice() > product.getTargetPrice()) {
                numberAboveTarget++;
            } else if (item.getActualPrice() < product.getTargetPrice()) {
                numberBelowTarget++;
            }
        }

        int total = numberAboveTarget + numberBelowTarget;
        if (total == 0) {
            pricePerformance = 0;
        } else {
            pricePerformance = ((double)(numberAboveTarget - numberBelowTarget) / total) * 100.0;
        }
    }

    public double getSalesRevenues() {
        return totalRevenue;
    }

    public int getNumberAboveTarget() {
        return numberAboveTarget;
    }

    public int getNumberBelowTarget() {
        return numberBelowTarget;
    }

    public double getProductPricePerformance() {
        return pricePerformance;
    }

    public boolean isProductAlwaysAboveTarget() {
        return numberBelowTarget == 0 && numberAboveTarget > 0;
    }
}
