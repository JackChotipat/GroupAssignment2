package TheBusiness;

import TheBusiness.CustomerManagement.CustomerDirectory;
import TheBusiness.CustomerManagement.CustomerProfile;
import TheBusiness.OrderManagement.Order;
import TheBusiness.OrderManagement.OrderItem;
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
    private static final int MAX_ITEMS_PER_ORDER1 = 100;
    private static final int MAX_ITEMS_PER_ORDER2 = 1000;
    
    Faker faker = new Faker();
    Random random = new Random();

    public void generateData(
            SupplierDirectory supplierDirectory,
            CustomerDirectory customerDirectory) {

        List<Product> allProducts = new ArrayList<>();

        // 1️⃣ 生成 Supplier + Product
        for (int i = 1; i <= NUM_SUPPLIERS; i++) {
            Supplier sup = supplierDirectory.newSupplier("SUP" + i);
            
            for (int j = 1; j <= PRODUCTS_PER_SUPPLIER; j++) {

                String productName = faker.commerce().productName();

                int floor = random.nextInt(50) + 10;
                int ceiling = floor + random.nextInt(200) + 20;
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

        // 2️⃣ 生成 CustomerProfile
        List<CustomerProfile> customerProfiles = new ArrayList<>();

        for (int i = 1; i <= NUM_CUSTOMERS; i++) {

    String name = faker.name().fullName();

    Person person = new Person(name);  // ⭐ 直接自己造 Person，不改 Person 类

    CustomerProfile cp = customerDirectory.newCustomerProfile(person);

    customerProfiles.add(cp);
}

        // 3️⃣ 为每个 Customer 生成 Orders
        for (CustomerProfile cp : customerProfiles) {

            int numOrders = random.nextInt(MAX_ORDERS_PER_CUSTOMER - MIN_ORDERS_PER_CUSTOMER + 1)
                    + MIN_ORDERS_PER_CUSTOMER;

            for (int k = 1; k <= numOrders; k++) {

                Order order = new Order(cp); // ✔ 完全匹配模板构造器

                int numItems = random.nextInt(MAX_ITEMS_PER_ORDER) + 1;

                for (int m = 0; m < numItems; m++) {

                    Product product = allProducts.get(random.nextInt(allProducts.size()));

                    int target = product.getTargetPrice();
                    int paid = (int) (target * (0.8 + (random.nextDouble() * 0.4))); // 80%–120%

                    int qty = random.nextInt(5) + 1;

                    order.newOrderItem(product, paid, qty);
                }
            }
        }

        System.out.println("✅ Data generation completed!");
        System.out.println("Suppliers: " + NUM_SUPPLIERS);
        System.out.println("Products: " + allProducts.size());
        System.out.println("Customers: " + NUM_CUSTOMERS);
    }
}
