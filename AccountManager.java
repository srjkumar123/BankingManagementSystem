import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.TreeSet;

public class AccountManager {
    private Connection con;
    private Scanner sc;

    AccountManager(Connection con , Scanner sc){
        this.con = con;
        this.sc = sc;
    }

    //credit money
    public void credit_money(long account_number) throws SQLException {
        sc.nextLine();
        System.out.print("Enter Amount: ");
        double amount = sc.nextDouble();
        sc.nextLine();
        System.out.print("Enter your PIN");
        String security_pin = sc.nextLine();

        try {
            con.setAutoCommit(false);
            if (account_number!=0){
                PreparedStatement ps = con.prepareStatement("SELECT * FROM Accounts WHERE account_number=? AND " +
                        "security_pin=?");
                ps.setLong(1, account_number);
                ps.setString(2,security_pin);
                ResultSet rs = ps.executeQuery();
                if (rs.next()){
                    String credit_query = "UPDATE Accounts SET balance = balance+? WHERE account_number =?";
                    PreparedStatement ps1  = con.prepareStatement(credit_query);
                    ps1.setDouble(1, amount);
                    ps1.setLong(2, account_number);
                    int rowsAffected = ps1.executeUpdate();
                    if(rowsAffected>0){
                        System.out.println("Rs."+amount+" credited successfully");
                        con.commit();
                        con.rollback();
                        con.setAutoCommit(true);
                    }else {
                        System.out.println("Transaction failed");
                        con.setAutoCommit(true);
                    }
                }
                else {
                    System.out.println("Invalid Security Pin");
                }
            }

        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        con.setAutoCommit(true);
    }
}
