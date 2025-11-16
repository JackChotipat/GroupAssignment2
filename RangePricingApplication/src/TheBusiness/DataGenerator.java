/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TheBusiness;

/**
 *
 * @author barryzhu
 */

import java.util.*;
public class DataGenerator {
    private static final int NUM_SUPPLIERS = 50;
    private static final int PRODUCTS_PER_SUPPLIER = 50;
    private static final int NUM_CUSTOMERS = 300;
    private static final int MIN_ORDERS_PER_CUSTOMER = 1;
    private static final int MAX_ORDERS_PER_CUSTOMER = 3;
    private static final int MAX_ITEMS_PER_ORDER = 10;

    private final Faker faker = new Faker();
    private final Random random = new Random();
    
    public void generateData() {
        List<Supplier> suppliers = new ArrayList<>();
        List<Product> products = new ArrayList<>();
        List<Customer> customers = new ArrayList<>();
        List<Order> orders = new ArrayList<>();
    }
    
    
}
