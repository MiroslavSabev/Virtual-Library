package Library;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User implements Serializable {
    public String uid;
    public String nickname;
    private String password;
    public LocalDateTime regDate;
    public String email;
    public double deposit;

    public User(String nick, String pass, String email) {
        this. uid = String.valueOf(Instant.now().getEpochSecond());
        this.nickname = nick;
        this.password = pass;
        this.email = email;
        this.regDate = LocalDateTime.now();
        this.deposit = 0;
    }

    // Method to validate password
    public static boolean verifyPassword(String password) {
        Pattern pattern1 = Pattern.compile("[\\d+]");
        Pattern pattern2 = Pattern.compile("[a-z]||[A-Z]");
        Pattern pattern3 = Pattern.compile("[^\\w+]");

        Matcher matcher = pattern1.matcher(password);
        boolean first = matcher.find();
        System.out.println(first);

        matcher = pattern2.matcher(password);
        boolean second = matcher.find();
        System.out.println(second);

        matcher = pattern3.matcher(password);
        boolean third = matcher.find();
        System.out.println(third);

        if (first && second && third && password.length() >= 8) {
            return true;
        }
        else {
            return false;
        }
    }

    // Method to check read pass with entered pass
    public boolean checkPass(String pass) {
        if (this.password.equals(pass)){
            return true;
        }
        else {
            return false;
        }
    }

    // Method to make a deposit
    public boolean makeDeposit() {
        Scanner s = new Scanner(System.in);
        try {
            System.out.println("Enter amount");
            double amount = s.nextDouble();
            this.deposit += amount;
            return true;
        }
        catch (Exception e){
            return false;
        }
    }
}
