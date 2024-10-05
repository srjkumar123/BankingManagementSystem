import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;

public class BankingApp {
    private static final String url = "jdbc:mysql://localhost:3306/bank_system";
    private static final String username = "root";
    private static final String password = "suraj@123";


    public static void main(String[] args) {
//        //load druver
//        try{
//            Class.forName("com.mysql.cj.jdbc.Driver");
//        }catch (Exception e){
//            System.out.println(e.getMessage());
//        }

        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
            return;
        }



        //Connection establish
        try{
            Connection con = DriverManager.getConnection(url, username, password);
            Scanner sc = new Scanner(System.in);
            User user = new User(con, sc);
            Accounts accounts = new Accounts(con, sc);
            AccountManager accountManager = new AccountManager(con, sc);

            String email;
            long account_number;

            while (true){
                System.out.println("***WELCOME TO BANKING SYSTEM***");
                System.out.println();
                System.out.println("1.Register");
                System.out.println("2.Login");
                System.out.println("3.Exit");
                System.out.println("Enter your choice: ");
                int choice1 = sc.nextInt();

                switch (choice1){
                    case 1:
                        user.register();
                        break;
                    case 2:
                        email = user.login();
                        if (email!=null){
                            System.out.println();
                            System.out.println("User logged in.");
                            if (!accounts.account_exist(email)){
                                System.out.println();
                                System.out.println("1.Open a New Bank Acount");
                                System.out.println("2.Exit");
                                if (sc.nextInt()==1){
                                    account_number = accounts.open_account(email);
                                    System.out.println("Acccount created successfully");
                                    System.out.println("Your Account Number is: "+ account_number);

                                }else {
                                    break;
                                }
                            }
                            account_number = accounts.get_account_number(email);
                            int choice2 = 0;
                            while (choice1!=5){
                                System.out.println();
                                System.out.println("1.Debit Money");
                                System.out.println("2.Credit Money");
                                System.out.println("3.Transfer Money");
                                System.out.println("4.Bank Balance");
                                System.out.println("5.Log Out");
                                System.out.println("Enter your choice: ");
                                choice2 = sc.nextInt();

                                switch (choice2){
                                    case 1:
                                        accountManager.debit_money(account_number);
                                        break;
                                    case 2:
                                        accountManager.credit_money(account_number);
                                        break;
                                    case 3:
                                        accountManager.transfer_money(account_number);
                                        break;
                                    case 4:
                                        accountManager.getBalance(account_number);
                                        break;
                                    case 5:
                                        break;
                                    default:
                                        System.out.println("Enter a valid choie");
                                        break;

                                }
                            }
                            break;
                        }
                        else {
                            System.out.println("Incorrect Email or Password");
                        }
                    case 3:
                        System.out.println("THANK YOU FOR USING BANKING SYSTEM");
                        System.out.println("Exiting the system!");
                        return;
                    default:
                        System.out.println("Enter valid choice ");
                        break;

                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
