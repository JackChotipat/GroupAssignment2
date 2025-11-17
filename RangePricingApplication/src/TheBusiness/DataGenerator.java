package TheBusiness;

import TheBusiness.CustomerManagement.CustomerDirectory;
import TheBusiness.CustomerManagement.CustomerProfile;
import TheBusiness.OrderManagement.Order;
import TheBusiness.Personnel.Person;
import TheBusiness.ProductManagement.Product;
import TheBusiness.Supplier.Supplier;
import TheBusiness.Supplier.SupplierDirectory;

import com.github.javafaker.Faker;
import java.util.*;

public class DataGenerator {

    private static final int NUM_SUPPLIERS = 50;
    private static final int PRODUCTS_PER_SUPPLIER = 50;
    private static final int NUM_CUSTOMERS = 300;
    private static final int MIN_ORDERS_PER_CUSTOMER = 1;
    private static final int MAX_ORDERS_PER_CUSTOMER = 3;
    private static final int MAX_ITEMS_PER_ORDER = 10;

    Faker faker = new Faker();

    public void generateData(
            SupplierDirectory supplierDirectory,
            CustomerDirectory customerDirectory) {

        List<Product> allProducts = new ArrayList<>();

        // -----------------------------
        // 1. 生成 Supplier + Products
        // -----------------------------
        for (int i = 1; i <= NUM_SUPPLIERS; i++) {

            // Supplier  faker
            String supplierName = faker.company().name() + " SUP-" + i;
            Supplier sup = supplierDirectory.newSupplier(supplierName);

            for (int j = 1; j <= PRODUCTS_PER_SUPPLIER; j++) {

                String productName = faker.commerce().productName();

                // 使用 Math.random()
                int floor = 10 + (int)(Math.random() * 50);              // 10–59
                int ceiling = floor + 20 + (int)(Math.random() * 200);   // floor+20 ~ floor+220
                int target = (floor + ceiling) / 2;

                Product p = sup.getProductCatalog().newProduct(
                        productName,
                        floor,
                        ceiling,
                        target
                );

                allProducts.add(p);
            }
        }

        // -----------------------------
        // 2.  Customers
        // -----------------------------
        List<CustomerProfile> customers = new ArrayList<>();

        for (int i = 1; i <= NUM_CUSTOMERS; i++) {

            String name = faker.name().fullName();
            Person person = new Person(name);
            CustomerProfile cp = customerDirectory.newCustomerProfile(person);

            customers.add(cp);
        }

        // -----------------------------
        // 3.  Orders + Items
        // -----------------------------
        for (CustomerProfile cp : customers) {

            // 1–3 orders
            int numOrders = MIN_ORDERS_PER_CUSTOMER +
                    (int)(Math.random() * (MAX_ORDERS_PER_CUSTOMER - MIN_ORDERS_PER_CUSTOMER + 1));

            for (int k = 0; k < numOrders; k++) {

                Order order = new Order(cp);

                //  order 1–10 items
                int numItems = 1 + (int)(Math.random() * MAX_ITEMS_PER_ORDER);

                for (int m = 0; m < numItems; m++) {

                    Product product = allProducts.get(
                            (int)(Math.random() * allProducts.size()));

                    int target = product.getTargetPrice();

                    // actual price 80%–120%
                    int paid = (int)(target * (0.8 + Math.random() * 0.4));

                    // quantity 1–5
                    int qty = 1 + (int)(Math.random() * 5);

                    order.newOrderItem(product, paid, qty);
                }
            }
        }

        System.out.println("✔ Data Generation Completed!");
        System.out.println("Generated Products: " + allProducts.size());
    }
}
