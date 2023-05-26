package Library;

import java.io.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Administration {
    // main menu
    public static void mainMenu() {

        System.out.println("Welcome to the main menu");
        System.out.println("Choose your option");
        System.out.println(" (R)egister | (L)ogin | (A)dmin | (E)xit");
        
        Scanner s = new Scanner(System.in);
        char choice = s.next().charAt(0);

        switch(choice) {
            // login
            case 'l':
                User user=logUser();
                if (user!=null) {
                    userMenu(user);
                }
                else {
                    mainMenu();
                }
                break;
            // admin login
            case 'a':
                if (logAdmin()){
                    adminMenu();
                }
                else {
                    mainMenu();
                }
                ;break;
            // register new user
            case 'r':
                regUser();
                mainMenu();
                break;
            // exit
            case 'e': System.exit(0);
                break;
        }
    }

    // Admin menu
    public static void adminMenu() {
        System.out.println("Welcome to the admin panel");
        System.out.println("Choose your option");
        System.out.println(" (R)egister book | (D) elete book | (E)xit");
        Scanner s = new Scanner(System.in);
        char choice = s.next().charAt(0);

        switch(choice){
            case 'r': regBook();
                adminMenu();
                break;
            case 'd':
                if (deleteBook()) {
                    System.out.println("book deleted");
                }
                adminMenu();
            case 'e': System.exit(0);
            break;
        }
    }

    // User Menu
    public static void userMenu(User user){
        System.out.println("(S)earch book | (M)ake deposit | (E)xit");
        Scanner s = new Scanner(System.in);
        char choice = s.next().charAt(0);
        switch(choice){
            case 's':
                System.out.println("Enter title:");
                String book_title=s.nextLine();
                Book book = searchBook(book_title);
                if (book!=null) {
                    rentMenu(user, book);
                }
                userMenu(user);
                break;
            case 'm':
                if (user.makeDeposit()) {
                    System.out.println("Deposit made successfuly!");
                    System.out.println("Current amount:" + user.deposit);
                }
                userMenu(user);
                break;
            case 'e': System.exit(0);
            break;
        }
    }

    // RentMenu
    public static void rentMenu(User user, Book book) {
        System.out.println("(R)ent | (M)ain menu |(E)xit");
        Scanner s = new Scanner(System.in);
        char choice = s.next().charAt(0);

        switch(choice){
            case 'r':
                rentBook(user, book);
                userMenu(user);
                break;
            case 'm': userMenu(user);
            case 'e': System.exit(0);
            break;
        }
    }

    // Methodd to register user
    public static boolean regUser() {
        try {
            Scanner s = new Scanner(System.in);
            System.out.println("Enter nickname:");

            String nickname = s.nextLine();
            System.out.println("Enter password:");

            String password = s.nextLine();
            System.out.println("Enter email");

            String email = s.nextLine();
            if (User.verifyPassword(password)) {
                User user = new User(nickname, password, email);
                BufferedWriter fw = new BufferedWriter(new FileWriter("users/users.txt", true));
                fw.append(user.uid + "," + user.nickname + "\n");
                fw.close();

                FileOutputStream userfile = new FileOutputStream("users/" + String.valueOf(user.uid) + ".ser");
                ObjectOutputStream out = new ObjectOutputStream(userfile);
                out.writeObject(user);
                out.close();

                System.out.println("User registered successfuly!");
                return true;
            }
            else{System.out.println("Password must contain 8 characters, 1 digit, 1 special symbol !");
                return regUser();
            }
        }
        catch(Exception e) {
            System.out.println("Something went wrong!");
            return regUser();
        }
    }

    // Method to log in user
    public static User logUser() {
        try {
            File user_list = new File("users/users.txt");
            Scanner s1 = new Scanner(user_list);
            Scanner s2 = new Scanner(System.in);
            System.out.println("Enter username:");
            String nickname = s2.nextLine();
            System.out.println("Enter password:");
            String password = s2.nextLine();

            Pattern pattern1 = Pattern.compile(nickname);
            boolean isthere = false;
            String row = "";
            while (s1.hasNext()) {
                row = s1.nextLine();
                Matcher matcher = pattern1.matcher(row);

                if (matcher.find()) {
                    isthere = true;
                    break;
                }
            }
            if (isthere) {
                String[] splitted = row.split(",",2);
                String uid = splitted[0];
                FileInputStream fileIn = new FileInputStream("users/" + uid + ".ser");
                ObjectInputStream in = new ObjectInputStream(fileIn);
                User user = (User) in.readObject();
                System.out.println("Here we have a user" + user.uid);

                in.close();
                fileIn.close();
                boolean success = user.checkPass(password);
                if (success) {
                    System.out.println("Login successful!");
                    return user;
                }
                else {
                    System.out.println("Wrong password!");
                    return null;
                }
            }
            else {
                System.out.println("No such user!");
                return null;
            }
        }
        catch (Exception e) {
            System.out.println("Something went wrong!");
            return null;
        }
    }

    // Method to login administrator
    public static boolean logAdmin() {
        try {
            File user_list = new File("admins.txt");
            Scanner s1 = new Scanner(user_list);
            Scanner s2 = new Scanner(System.in);
            System.out.println("Enter username:");
            String nickname = s2.nextLine();
            System.out.println("Enter password:");
            String password = s2.nextLine();

            Pattern pattern1 = Pattern.compile(nickname);
            boolean isthere = false;
            String row = "";
            while (s1.hasNext()){
                row = s1.nextLine();
                Matcher matcher = pattern1.matcher(row);

                if (matcher.find()) {
                    isthere = true;
                    break;
                }
            }
            if (isthere) {
                String[] splitted = row.split(",");
                String readpass = splitted[1];

                if (password.equals(readpass)) {
                    System.out.println("Login successful!");
                    return true;
                }
                else {
                    System.out.println("Wrong password!");
                    return false;
                }
            }
            else {
                System.out.println("No such user!");
                return false;
            }
        }
        catch (Exception e) {
            System.out.println("Something went wrong!");
            return false;
        }
    }

    //Method to register new book
    public static void regBook() {
        String isbn, title, author, content;
        Scanner s = new Scanner(System.in);

        System.out.println("Enter ISBN:");
        isbn = s.nextLine();

        System.out.println("Enter Title:");
        title = s.nextLine();

        System.out.println("Enter author:");
        author = s.nextLine();

        System.out.println("Enter content:");
        content = s.nextLine();

        Book book = new Book(isbn, title, author, content);

        try {
            BufferedWriter catalogue = new BufferedWriter(new FileWriter("books/catalogue.txt", true));
            catalogue.append(book.isbn + "," + book.title + "\n");
            FileOutputStream fileOut = new FileOutputStream("books/" + book.isbn + ".ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(book);
            out.close();
            fileOut.close();
            catalogue.close();
            System.out.printf("New book is saved");
        } catch (IOException i) {
            System.out.printf("Can't save book!");
        }
    }

    public static boolean deleteBook() {
        try {
            File catalogue = new File("books/catalogue.txt");
            File tempFile = new File("books/TempFile.txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
            Scanner s1 = new Scanner(catalogue);
            Scanner s2 = new Scanner(System.in);
            System.out.println("Enter ISBN:");
            String isbn=s2.nextLine();
            Pattern pattern1 = Pattern.compile(isbn);
            boolean isthere = false;
            String row = "";
            while (s1.hasNext()){
                row = s1.nextLine();
                Matcher matcher = pattern1.matcher(row);
                if (matcher.find()) {
                    isthere = true;
                    continue;
                }
                writer.write(row + "\n");
            }
            writer.close();
            tempFile.renameTo(catalogue);

            if (isthere) {
                File myObj = new File("books/" + isbn + ".ser");
                try {
                    myObj.delete();
                    return true;
                }
                catch (Exception e) {
                    System.out.println("Something went wrong!");
                    return false;
                }
            }
            else{
                System.out.println("Book is not in catalogue!");
                return false;
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            return false;
        }


    }

    // Method to search wether a book is in catalogue
    public static Book searchBook(String title) {

        File catalogue = new File("books/catalogue.txt");
        try {
            Scanner s1 = new Scanner(catalogue);
            Scanner s2 = new Scanner(System.in);
            Pattern pattern1 = Pattern.compile(s2.nextLine());
            boolean isthere = false;
            String row = "";
            while (s1.hasNext()){
                row = s1.nextLine();
                Matcher matcher = pattern1.matcher(row);
                if (matcher.find()){
                    isthere = true;
                    break;
                }
            }
            if (isthere){
                String[] split = row.split(",");
                String isbn = split[0];
                FileInputStream fileIn = new FileInputStream("books/" + isbn + ".ser");
                ObjectInputStream in = new ObjectInputStream(fileIn);
                Book book = (Book) in.readObject();
                in.close();
                fileIn.close();
                System.out.println("Book is in catalogue");
                return book;
            }
            else {
                System.out.println("Book is not in catalogue");
                return null;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    // Method to read book info
    public static Book getBook(String isbn){
        try {
            FileInputStream fileIn = new FileInputStream("books/" + isbn + ".ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            Book book = (Book) in.readObject();
            return book;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to rent a book
    public static void rentBook(User user, Book book) {
        if (user.deposit >= 2){
            user.deposit -= 2;
            System.out.println(book.content);
        }
        else{
            System.out.println("Not enough money in deposit");
        }
    }
}
