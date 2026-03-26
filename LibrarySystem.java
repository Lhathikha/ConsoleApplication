import java.util.*;
class Book {
    String name;
    String isbn;
    int quantity;
    double price;

    Book(String name, String isbn, int quantity, double price) {
        this.name = name;
        this.isbn = isbn;
        this.quantity = quantity;
        this.price = price;
    }
}
class User {
    String email;
    String password;
    String role;
    double deposit = 1500;
    List<Book> borrowed = new ArrayList<>();

    User(String email, String password, String role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }
}

public class LibrarySystem {
    static List<Book> books = new ArrayList<>();
    static List<User> users = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        users.add(new User("admin@gmail.com", "admin", "admin"));
        users.add(new User("user@gmail.com", "user", "borrower"));

        while (true) {
            System.out.println("1.Login 2.Exit");
            int ch = sc.nextInt();
            if (ch == 1) login();
            else break;
        }
    }

    static void login() {
        System.out.print("Email: ");
        String email = sc.next();
        System.out.print("Password: ");
        String pass = sc.next();

        for (User u : users) {
            if (u.email.equals(email) && u.password.equals(pass)) {
                if (u.role.equals("admin")) adminMenu(u);
                else borrowerMenu(u);
                return;
            }
        }
        System.out.println("Invalid login");
    }

    static void adminMenu(User admin) {
        while (true) {
            System.out.println("1.Add Book 2.View Books 3.Delete Book 4.Logout");
            int ch = sc.nextInt();

            switch (ch) {
                case 1:
                    System.out.print("Name ISBN Qty Price: ");
                    books.add(new Book(sc.next(), sc.next(), sc.nextInt(), sc.nextDouble()));
                    break;

                case 2:
                    for (Book b : books)
                        System.out.println(b.name + " " + b.isbn + " " + b.quantity);
                    break;

                case 3:
                    System.out.print("Enter ISBN: ");
                    String isbn = sc.next();
                    books.removeIf(b -> b.isbn.equals(isbn));
                    break;

                case 4:
                    return;
            }
        }
    }

    static void borrowerMenu(User user) {
        while (true) {
            System.out.println("1.View Books 2.Borrow 3.Return 4.Logout");
            int ch = sc.nextInt();

            switch (ch) {
                case 1:
                    for (Book b : books)
                        System.out.println(b.name + " " + b.isbn + " " + b.quantity);
                    break;

                case 2:
                    if (user.borrowed.size() >= 3) {
                        System.out.println("Max 3 books only");
                        break;
                    }

                    System.out.print("Enter ISBN: ");
                    String isbn = sc.next();

                    for (Book b : books) {
                        if (b.isbn.equals(isbn) && b.quantity > 0) {
                            if (user.deposit < 500) {
                                System.out.println("Maintain minimum deposit");
                                return;
                            }
                            if (user.borrowed.contains(b)) {
                                System.out.println("Already borrowed");
                                return;
                            }
                            user.borrowed.add(b);
                            b.quantity--;
                            System.out.println("Borrowed");
                            return;
                        }
                    }
                    System.out.println("Book not available");
                    break;

                case 3:
                    System.out.print("Enter ISBN: ");
                    String rIsbn = sc.next();

                    Iterator<Book> it = user.borrowed.iterator();
                    while (it.hasNext()) {
                        Book b = it.next();
                        if (b.isbn.equals(rIsbn)) {
                            b.quantity++;
                            it.remove();
                            System.out.println("Returned");
                            return;
                        }
                    }
                    System.out.println("Not found");
                    break;

                case 4:
                    return;
            }
        }
    }
}