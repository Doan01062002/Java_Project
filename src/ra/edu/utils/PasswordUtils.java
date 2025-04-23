package ra.edu.utils;

import java.io.Console;
import java.util.Scanner;

public class PasswordUtils {
    public static String readPassword(Scanner scanner, String prompt) {
        Console console = System.console();
        String password;
        if (console == null) {
            System.out.print(prompt);
            password = scanner.nextLine();
            System.err.println("Chạy trong IDE, mật khẩu không được che giấu.");
        } else {
            System.out.print(prompt);
            char[] passwordArray = console.readPassword();
            password = new String(passwordArray);
        }
        return password;
    }
}