import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class User {
    private Connection con;
    private Scanner sc;

    public User(Connection con, Scanner sc){
        this.con = con;
        this.sc = sc;
    }


    //User register
    public void register(){
        sc.nextLine();
        System.out.print("Full Name: ");
        String full_name = sc.nextLine();

        System.out.print("Email: ");
        String email = sc.nextLine();

        System.out.print("Password: ");
        String password = sc.nextLine();

        //check email if it already exist or not
        if (user_exist(email)){
            System.out.println("User already exists for this email. Try login or enter another email address.");
            return;
        }

        String register_query = "INSERT INTO User(full_name, email, password) VALUES(?,?,?)";
        try{
            PreparedStatement ps = con.prepareStatement(register_query);
            ps.setString(1, full_name);
            ps.setString(2,email);
            ps.setString(3, password);

            int affectedRows = ps.executeUpdate();
            if (affectedRows>0){
                System.out.println("User registration successful");
            }else {
                System.out.println("User registration successful");
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }

    //User login
    public String login(){
        sc.nextLine();
        System.out.print("Emai: ");
        String email = sc.nextLine();

        System.out.print("Password: ");
        String password = sc.nextLine();
        String login_query = "SELECT * FROM User WHERE email=? AND password=?";

        try {
            PreparedStatement ps = con.prepareStatement(login_query);
            ps.setString(1,email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                return email;
            }
            else {
                return null;
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }



    //check user if already exist or not

    public boolean user_exist(String email){
        String query = "SELECT * FROM User WHERE email = ?";

        try {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1,email);

            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                return true;
            }else {
                return false;
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

        return false;
    }
}
