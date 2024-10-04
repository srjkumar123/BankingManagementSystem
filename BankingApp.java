import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;

public class BankingApp {
    private static final String url = "jdbc:mysql://localhost:3306/banking_system";
    private static final String username = "root";
    private static final String password = "suraj@123";

    public static void main(String[] args) {
        //load druver
        try{
            Class.forName("com.mysql.jdbc.Driver");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        //Connection establish
        try{
            Connection con = DriverManager.getConnection(url, username, password);
            Scanner sc = new Scanner(System.in);

        }catch (Exception e){

        }
    }
}
