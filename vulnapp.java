import java.io.*;
import java.sql.*;

public class VulnerableApp {
    
    // Hardcoded credentials (Bad Practice)
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "password123";

    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        
        System.out.print("Enter username: ");
        String userInput = reader.readLine();
        System.out.print("Enter password: ");
        String passInput = reader.readLine();
        authenticate(userInput, passInput);
        
        System.out.print("Enter SQL query: ");
        String sqlQuery = reader.readLine();
        sqlInjectionVulnerable(sqlQuery);
        
        System.out.print("Enter command: ");
        String cmd = reader.readLine();
        commandInjectionVulnerable(cmd);
        
        System.out.print("Enter serialized object file: ");
        String fileName = reader.readLine();
        insecureDeserialization(fileName);
    }
    
    private static void authenticate(String user, String password) {
        if (USERNAME.equals(user) && PASSWORD.equals(password)) {
            System.out.println("Authentication successful!");
        } else {
            System.out.println("Authentication failed!");
        }
    }
    
    private static void sqlInjectionVulnerable(String query) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "root", "root");
             Statement stmt = conn.createStatement()) {
            
            // Vulnerable SQL query (No prepared statement, direct concatenation)
            ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE name = '" + query + "'");
            while (rs.next()) {
                System.out.println("User: " + rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private static void commandInjectionVulnerable(String command) {
        try {
            // Directly executing user input (Vulnerable to command injection)
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static void insecureDeserialization(String fileName) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            // Insecure deserialization of untrusted data
            Object obj = ois.readObject();
            System.out.println("Deserialized object: " + obj.toString());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
