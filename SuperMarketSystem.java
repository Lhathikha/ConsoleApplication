import java.util.*;

class User {
    String email;
    String password;
    String role;
    double credit = 1000;
    int loyaltyPoints = 0;

    public User(String email, String password, String role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }
}

class Product {
    int id;
    String name;
    double price;
    int quantity;
    int soldCount = 0;

    public Product(int id, String name, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }
}

class Bill {
    int billId;
    String customerEmail;
    double amount;
    Date date;

    public Bill(int billId, String customerEmail, double amount) {
        this.billId = billId;
        this.customerEmail = customerEmail;
        this.amount = amount;
        this.date = new Date();
    }
}

public class SuperMarketSystem {

    static Scanner sc = new Scanner(System.in);

    static List<User> users = new ArrayList<>();
    static List<Product> products = new ArrayList<>();
    static List<Bill> bills = new ArrayList<>();

    static int productIdCounter = 1;
    static int billIdCounter = 1;

    public static void main(String[] args) {

        users.add(new User("admin@gmail.com", "123", "admin"));

        while (true) {
            System.out.println("\n--- LOGIN ---");
            System.out.print("Email: ");
            String email = sc.next();
            System.out.print("Password: ");
            String password = sc.next();

            User loggedUser = authenticate(email, password);

            if (loggedUser == null) {
                System.out.println("Invalid login!");
                continue;
            }

            if (loggedUser.role.equals("admin")) {
                adminMenu(loggedUser);
            } else {
                customerMenu(loggedUser);
            }
        }
    }

    static User authenticate(String email, String password) {
        for (User u : users) {
            if (u.email.equals(email) && u.password.equals(password)) {
                return u;
            }
        }
        return null;
    }

    static void adminMenu(User admin) {
        while (true) {
            System.out.println("\n--- ADMIN MENU ---");
            System.out.println("1.Add Product  2.Modify Product  3.Delete Product");
            System.out.println("4.View Products 5.Search Product 6.Add User");
            System.out.println("7.Reports 8.Logout");

            int ch = sc.nextInt();

            switch (ch) {
                case 1: addProduct(); break;
                case 2: modifyProduct(); break;
                case 3: deleteProduct(); break;
                case 4: viewProducts(); break;
                case 5: searchProduct(); break;
                case 6: addUser(); break;
                case 7: reports(); break;
                case 8: return;
            }
        }
    }

    static void customerMenu(User customer) {

        Map<Integer, Integer> cart = new HashMap<>();

        while (true) {
            System.out.println("\n--- CUSTOMER MENU ---");
            System.out.println("1.View Products 2.Add to Cart 3.Edit Cart");
            System.out.println("4.Payment 5.Purchase History 6.Logout");

            int ch = sc.nextInt();

            switch (ch) {
                case 1: viewProducts(); break;
                case 2: addToCart(cart); break;
                case 3: editCart(cart); break;
                case 4: payment(customer, cart); break;
                case 5: viewHistory(customer); break;
                case 6: return;
            }
        }
    }

    static void addProduct() {
        System.out.print("Name: ");
        String name = sc.next();
        System.out.print("Price: ");
        double price = sc.nextDouble();
        System.out.print("Quantity: ");
        int qty = sc.nextInt();

        products.add(new Product(productIdCounter++, name, price, qty));
    }

    static void modifyProduct() {
        System.out.print("Enter Product ID: ");
        int id = sc.nextInt();

        for (Product p : products) {
            if (p.id == id) {
                System.out.print("New Price: ");
                p.price = sc.nextDouble();
                System.out.print("New Quantity: ");
                p.quantity = sc.nextInt();
                return;
            }
        }
    }

    static void deleteProduct() {
        System.out.print("Enter Product ID: ");
        int id = sc.nextInt();

        products.removeIf(p -> p.id == id);
    }

    static void viewProducts() {
        for (Product p : products) {
            System.out.println(p.id + " " + p.name + " Rs." + p.price + " Qty:" + p.quantity);
        }
    }

    static void searchProduct() {
        System.out.print("Enter name: ");
        String name = sc.next();

        for (Product p : products) {
            if (p.name.equalsIgnoreCase(name)) {
                System.out.println(p.name + " Rs." + p.price);
            }
        }
    }

    static void addUser() {
        System.out.print("Email: ");
        String email = sc.next();
        System.out.print("Password: ");
        String pass = sc.next();
        System.out.print("Role (admin/customer): ");
        String role = sc.next();

        users.add(new User(email, pass, role));
    }

    static void addToCart(Map<Integer, Integer> cart) {
        System.out.print("Product ID: ");
        int id = sc.nextInt();
        System.out.print("Quantity: ");
        int qty = sc.nextInt();

        cart.put(id, cart.getOrDefault(id, 0) + qty);
    }

    static void editCart(Map<Integer, Integer> cart) {
        System.out.print("Product ID: ");
        int id = sc.nextInt();
        System.out.print("New Quantity: ");
        int qty = sc.nextInt();

        if (qty == 0) cart.remove(id);
        else cart.put(id, qty);
    }

    static void payment(User customer, Map<Integer, Integer> cart) {

        double total = 0;

        for (int id : cart.keySet()) {
            for (Product p : products) {
                if (p.id == id) {
                    int qty = cart.get(id);
                    total += p.price * qty;
                }
            }
        }

        if (total > customer.credit) {
            System.out.println("Not enough credit!");
            return;
        }

        customer.credit -= total;

        if (total >= 5000) {
            customer.credit += 100;
        } else {
            customer.loyaltyPoints += total / 100;
            if (customer.loyaltyPoints >= 50) {
                customer.credit += 100;
                customer.loyaltyPoints = 0;
            }
        }

        for (int id : cart.keySet()) {
            for (Product p : products) {
                if (p.id == id) {
                    int qty = cart.get(id);
                    p.quantity -= qty;
                    p.soldCount += qty;
                }
            }
        }

        bills.add(new Bill(billIdCounter++, customer.email, total));

        cart.clear();

        System.out.println("Payment Successful!");
    }

    static void viewHistory(User customer) {
        for (Bill b : bills) {
            if (b.customerEmail.equals(customer.email)) {
                System.out.println("BillID: " + b.billId + " Amount: " + b.amount + " Date: " + b.date);
            }
        }
    }

    static void reports() {

        System.out.println("\nLow Stock:");
        for (Product p : products) {
            if (p.quantity < 5) {
                System.out.println(p.name);
            }
        }

        System.out.println("\nNot Sold:");
        for (Product p : products) {
            if (p.soldCount == 0) {
                System.out.println(p.name);
            }
        }

        System.out.println("\nTop Customers:");
        Map<String, Double> map = new HashMap<>();

        for (Bill b : bills) {
            map.put(b.customerEmail, map.getOrDefault(b.customerEmail, 0.0) + b.amount);
        }

        for (String email : map.keySet()) {
            System.out.println(email + " -> " + map.get(email));
        }
    }
}