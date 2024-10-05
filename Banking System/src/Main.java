import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    private static final String url="jdbc:mysql://localhost:3306/banking_sys";
    private static final String username="root";
    private static final String password="0000";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
//            System.out.println("drivers loaded");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        try {
            Connection con= DriverManager.getConnection(url,username,password);
            Scanner sc=new Scanner(System.in);
            User user=new User(con,sc);
            Accounts accounts=new Accounts(con,sc);
            AccountManager accountManager=new AccountManager(con,sc);
            String email;
            long account_number;


            while(true){
                System.out.println(">>>>WELCOME TO AMVB BANK<<<<");
                System.out.println();
                System.out.println("1.Register");
                System.out.println("2.Login");
                System.out.println("3.Exit");
                System.out.print("Enter your Choice:");
                int Choice1=sc.nextInt();
                switch (Choice1){
                    case 1:
                        user.register();
                        break;
                    case 2:
                        email=user.login();
                        if(email!=null){
                            System.out.println();
                            System.out.println("@@USER LOGGED IN@@");
                            if(!accounts.account_exists(email)){
                                System.out.println();
                                System.out.println("1.Open new account");
                                System.out.println("2.Exit");
                                System.out.print("Enter your Choice:");
                                if(sc.nextInt()==1){
                                    account_number=accounts.open_account(email);
                                    System.out.println("Account Created Successfully");
                                    System.out.println("Your Account Number is: " + account_number);
                                }else{
                                    break;
                                }
                            }
                            account_number=accounts.get_account_number(email);
                            int choice2=0;
                            while(choice2!=5){
                                System.out.println();
                                System.out.println("1. Debit Money");
                                System.out.println("2. Credit Money");
                                System.out.println("3. Transfer Money");
                                System.out.println("4. Check Balance");
                                System.out.println("5. Log Out");
                                System.out.print("Enter your choice: ");
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
                                        accountManager.check_balance(account_number);
                                        break;
                                    case 5:
                                        break;
                                    default:
                                        System.out.println("Enter valid choice");
                                        break;

                                }
                            }
                        }else{
                            System.out.println("Incorrect email or password");
                        }
                        break;
                    case 3:
                        System.out.println("THANK YOU FOR VISITING");
                        System.out.println("Exiting System!!");
                        return;
                    default:
                        System.out.println("Enter valid choice");
                        break;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
}