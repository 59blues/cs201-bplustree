package edu.smu.smusql;

import java.util.*;

public class Main {
    static Engine dbEngine = new Engine();
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("smuSQL version 0.5.1 2024-09-20");
        System.out.println("sample implementation for reference only");

        while (true) {
            System.out.print("smusql> ");
            String query = scanner.nextLine();
            if (query.equalsIgnoreCase("exit")) {
                break;
            } else if (query.equalsIgnoreCase("evaluate")) {
                long startTime = System.nanoTime();
                autoEvaluate();
                long stopTime = System.nanoTime();
                long elapsedTime = stopTime - startTime;
                double elapsedTimeInSecond = (double) elapsedTime / 1_000_000_000;
                System.out.println("Time elapsed: " + elapsedTimeInSecond + " seconds");
                break;
            }
            System.out.println(dbEngine.executeSQL(query));
        }
        scanner.close();
    }

    public static void autoEvaluate() {
        // int numberOfQueries = 50000;

        // dbEngine.executeSQL("CREATE TABLE users (id name age city)");
        // dbEngine.executeSQL("CREATE TABLE products (id name price category)");
        // dbEngine.executeSQL("CREATE TABLE orders (id user_id product_id quantity)");

        // Random random = new Random();
        // prepopulateTables(random);

        // for (int i = 0; i < numberOfQueries; i++) {

        //     int queryType = random.nextInt(6);

        //     switch (queryType) {
        //         case 0: insertRandomData(random); break;
        //         case 1: selectRandomData(random); break;
        //         case 2: updateRandomData(random); break;
        //         case 3: deleteRandomData(random); break;
        //         case 4: complexSelectQuery(random); break;
        //         case 5: complexUpdateQuery(random); break;
        //     }

        //     if (i % 10000 == 0) {
        //         System.out.println("Processed " + i + " queries...");
        //     }
        // }
        // System.out.println("Finished processing " + numberOfQueries + " queries.");

        //Set the number of queries to execute per operation type
        int numberOfQueries = 100000;  // You can adjust this number based on your performance testing requirements
    
        // Create tables
        dbEngine.executeSQL("CREATE TABLE users (id name age city)");
        dbEngine.executeSQL("CREATE TABLE products (id name price category)");
        dbEngine.executeSQL("CREATE TABLE orders (id user_id product_id quantity)");
    
        // Random data generator
        Random random = new Random();
        prepopulateTables(random);
    
        // Test INSERT performance
        long startTime = System.nanoTime();
        for (int i = 0; i < numberOfQueries; i++) {
            insertRandomData(random);
        }
        long endTime = System.nanoTime();
        double insertTime = (endTime - startTime) / 1_000_000_000.0;
        System.out.println("INSERT operation took: " + insertTime + " seconds");
    
        // Test SELECT performance
        startTime = System.nanoTime();
        for (int i = 0; i < numberOfQueries; i++) {
            selectRandomData(random);
        }
        endTime = System.nanoTime();
        double selectTime = (endTime - startTime) / 1_000_000_000.0;
        System.out.println("SELECT operation took: " + selectTime + " seconds");
    
        // Test UPDATE performance
        startTime = System.nanoTime();
        for (int i = 0; i < numberOfQueries; i++) {
            updateRandomData(random);
        }
        endTime = System.nanoTime();
        double updateTime = (endTime - startTime) / 1_000_000_000.0;
        System.out.println("UPDATE operation took: " + updateTime + " seconds");
    
        // Test DELETE performance
        startTime = System.nanoTime();
        for (int i = 0; i < numberOfQueries; i++) {
            deleteRandomData(random);
        }
        endTime = System.nanoTime();
        double deleteTime = (endTime - startTime) / 1_000_000_000.0;
        System.out.println("DELETE operation took: " + deleteTime + " seconds");
    
        // Report the total time
        double totalTime = insertTime + selectTime + updateTime + deleteTime;
        System.out.println("Total time for all operations: " + totalTime + " seconds");
    }

    private static void prepopulateTables(Random random) {
        System.out.println("Prepopulating users");
        for (int i = 0; i < 50; i++) {
            String name = "User" + i;
            int age = 20 + (i % 41);
            String city = getRandomCity(random);
            String insertCommand = String.format("INSERT INTO users VALUES %d %s %d %s", 
                i, name, age, city);
            dbEngine.executeSQL(insertCommand);
        }

        System.out.println("Prepopulating products");
        for (int i = 0; i < 50; i++) {
            String productName = "Product" + i;
            double price = 10 + (i % 990);
            String category = getRandomCategory(random);
            String insertCommand = String.format("INSERT INTO products VALUES %d %s %.2f %s", 
                i, productName, price, category);
            dbEngine.executeSQL(insertCommand);
        }

        System.out.println("Prepopulating orders");
        for (int i = 0; i < 50; i++) {
            int user_id = random.nextInt(9999);
            int product_id = random.nextInt(9999);
            int quantity = random.nextInt(1, 100);
            String insertCommand = String.format("INSERT INTO orders VALUES %d %d %d %d", 
                i, user_id, product_id, quantity);
            dbEngine.executeSQL(insertCommand);
        }
    }

    private static void insertRandomData(Random random) {
        int tableChoice = random.nextInt(3);
        switch (tableChoice) {
            case 0:
                int id = random.nextInt(10000) + 10000;
                String name = "User" + id;
                int age = random.nextInt(60) + 20;
                String city = getRandomCity(random);
                dbEngine.executeSQL(String.format("INSERT INTO users VALUES %d %s %d %s", 
                    id, name, age, city));
                break;
            case 1:
                int productId = random.nextInt(1000) + 10000;
                String productName = "Product" + productId;
                double price = 50 + (random.nextDouble() * 1000);
                String category = getRandomCategory(random);
                dbEngine.executeSQL(String.format("INSERT INTO products VALUES %d %s %.2f %s", 
                    productId, productName, price, category));
                break;
            case 2:
                int orderId = random.nextInt(10000) + 1;
                int userId = random.nextInt(10000) + 1;
                int productIdRef = random.nextInt(1000) + 1;
                int quantity = random.nextInt(10) + 1;
                dbEngine.executeSQL(String.format("INSERT INTO orders VALUES %d %d %d %d", 
                    orderId, userId, productIdRef, quantity));
                break;
        }
    }

    private static void selectRandomData(Random random) {
        String[] tables = {"users", "products", "orders"};
        String selectQuery = "SELECT * FROM " + tables[random.nextInt(3)];
        dbEngine.executeSQL(selectQuery);
    }

    private static void updateRandomData(Random random) {
        int tableChoice = random.nextInt(3);
        switch (tableChoice) {
            case 0:
                int id = random.nextInt(10000) + 1;
                int newAge = random.nextInt(60) + 20;
                dbEngine.executeSQL(String.format("UPDATE users age %d id %d", newAge, id));
                break;
            case 1:
                int productId = random.nextInt(1000) + 1;
                double newPrice = 50 + (random.nextDouble() * 1000);
                dbEngine.executeSQL(String.format("UPDATE products price %.2f id %d", newPrice, productId));
                break;
            case 2:
                int orderId = random.nextInt(10000) + 1;
                int newQuantity = random.nextInt(10) + 1;
                dbEngine.executeSQL(String.format("UPDATE orders quantity %d id %d", newQuantity, orderId));
                break;
        }
    }

    private static void deleteRandomData(Random random) {
        String[] tables = {"users", "products", "orders"};
        int id = random.nextInt(10000) + 1;
        String deleteQuery = String.format("DELETE FROM %s where id = %d", 
            tables[random.nextInt(3)], id);
        dbEngine.executeSQL(deleteQuery);
    }

    // private static void complexSelectQuery(Random random) {
    //     int tableChoice = random.nextInt(2);
    //     String query;
    //     switch (tableChoice) {
    //         case 0:
    //             int minAge = random.nextInt(20) + 20;
    //             int maxAge = minAge + random.nextInt(30);
    //             query = String.format("SELECT * FROM users WHERE age > %d AND age < %d", 
    //                 minAge, maxAge);
    //             break;
    //         case 1:
    //             double minPrice = 50 + (random.nextDouble() * 200);
    //             double maxPrice = minPrice + random.nextDouble() * 500;
    //             query = String.format("SELECT * FROM products WHERE price > %.2f AND price < %.2f", 
    //                 minPrice, maxPrice);
    //             break;
    //         default:
    //             query = "SELECT * FROM users";
    //     }
    //     dbEngine.executeSQL(query);
    // }

    // private static void complexUpdateQuery(Random random) {
    //     int tableChoice = random.nextInt(2);
    //     switch (tableChoice) {
    //         case 0:
    //             int newAge = random.nextInt(60) + 20;
    //             String city = getRandomCity(random);
    //             // Format: UPDATE tablename column newvalue column value
    //             dbEngine.executeSQL("UPDATE users age " + newAge + " city " + city);
    //             break;
    //         case 1:
    //             double newPrice = 50 + (random.nextDouble() * 1000);
    //             String category = getRandomCategory(random);
    //             dbEngine.executeSQL("UPDATE products price " + newPrice + " category " + category);
    //             break;
    //     }
    // }

    private static String getRandomCity(Random random) {
        String[] cities = {"NewYork", "LosAngeles", "Chicago", "Boston", "Miami", 
            "Seattle", "Austin", "Dallas", "Atlanta", "Denver"};
        return cities[random.nextInt(cities.length)];
    }

    private static String getRandomCategory(Random random) {
        String[] categories = {"Electronics", "Appliances", "Clothing", "Furniture", 
            "Toys", "Sports", "Books", "Beauty", "Garden"};
        return categories[random.nextInt(categories.length)];
    }
}
