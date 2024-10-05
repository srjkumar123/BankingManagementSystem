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

    public void debit_money(long account_number)throws SQLException{
        sc.nextLine();
        System.out.print("Enter Amount: ");
        double amount = sc.nextDouble();
        sc.nextLine();
        System.out.print("Enter your PIN");
        String security_pin = sc.nextLine();

        try {
            con.setAutoCommit(false);
            if (account_number!=0){
                PreparedStatement ps = con.prepareStatement("SELECT * FROM Accounts WHERE account_number=? and " +
                        "security_pin=?");
                ps.setLong(1,account_number);
                ps.setString(2,security_pin);
                ResultSet rs = ps.executeQuery();

                if (rs.next()){
                    double current_balance = rs.getDouble("balance");
                    if (amount<=current_balance){
                        String debit_query = "UPDATE Accounts SET balance = balance -? WHERE account_number =?";
                        PreparedStatement ps1 = con.prepareStatement(debit_query);
                        ps1.setDouble(1,amount);
                        ps1.setLong(2,account_number);
                        int rowsAffeted = ps1.executeUpdate();

                        if (rowsAffeted>0){
                            System.out.println("Rs."+amount+" debited successfully..");
                            con.commit();
                            con.setAutoCommit(true);
                            return;
                        }else {
                            System.out.println("Transaction failed");
                            con.rollback();
                            con.setAutoCommit(true);
                        }
                    }else {
                        System.out.println("Insufficient funds");
                    }
                }
            }
            else {
                System.out.println("Invalid PIN");
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        con.setAutoCommit(true);
    }




    //transfer money
    public void transfer_money(long sender_account_number){
        sc.nextLine();
        System.out.print("Enter Receiver account number: ");
        long receiver_account_number = sc.nextLong();

        System.out.print("Enter Amount: ");
        double amount = sc.nextDouble();
        sc.nextLine();

        System.out.print("Enter Security PIN: ");
        String security_pin = sc.nextLine();

        try {
            con.setAutoCommit(false);
            if (sender_account_number !=0 && receiver_account_number !=0){
                PreparedStatement ps = con.prepareStatement("SELECT * FROM Accounts WHERE account_number=? AND " +
                        "security_pin = ?");
                ps.setLong(1,sender_account_number);
                ps.setString(2,security_pin);

                ResultSet rs = ps.executeQuery();
                if (rs.next()){
                    double current_balance = rs.getDouble("balance");
                    if (amount>=current_balance){
                        //write credit and debit queries
                        String debit_query = "UPDATE Accounts SET balance = balance-? WHERE account_number =?";
                        String credit_query = "UPDATE Accounts SET balance = balance +? WHERE accpunt_number=?";

                        //debit and credit Prepared statements
                        PreparedStatement ps_debit = con.prepareStatement(debit_query);
                        PreparedStatement ps_credit = con.prepareStatement(credit_query);

                        ps_debit.setDouble(1,amount);
                        ps_debit.setLong(2,sender_account_number);
                        ps_credit.setDouble(1,amount);
                        ps_credit.setLong(2, receiver_account_number);

                        int rowsAffected1 = ps_debit.executeUpdate();
                        int rowsAffected2 = ps_credit.executeUpdate();

                        if (rowsAffected1>0 && rowsAffected2>0){
                            System.out.println("Transaction successful");
                            con.commit();
                            con.setAutoCommit(true);
                        }else {
                            System.out.println("Transaction Failed");
                            con.rollback();
                            con.setAutoCommit(true);
                        }
                    }else {
                        System.out.println("Inssuffiient funds");

                    }
                }
                else {
                    System.out.println("Incorrect PIN");
                }
            }else {
                System.out.println("Invalid acount number");
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void getBalance(long account_number){
        sc.nextLine();
        System.out.print("Enter Security PIN: ");
        String security_pin = sc.nextLine();

        try{
            PreparedStatement ps = con.prepareStatement("SELECT balance FROM Accounts WHERE account_number =? AND " +
                    "security_pin=?");
            ps.setLong(1, account_number);
            ps.setString(2,security_pin);

            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                double balance = rs.getDouble("balance");
                System.out.println("Balance: "+balance);

            }else {
                System.out.println("Invalid");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

    }
}
