import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Accounts {

    Connection con;
    Scanner sc;
    public Accounts(Connection con,Scanner sc){
        this.con=con;
        this.sc=sc;
    }
    public long open_account(String email) throws SQLException {
        if(!account_exists(email)){
            System.out.println();
            System.out.print("Enter full name:");
            String full_name=sc.next();
            System.out.print("Enter Initial amount:");
            double balance=sc.nextDouble();
            System.out.print("Enter security pin:");
            String security_pin=sc.next();
            String sql="insert into accounts values(?,?,?,?,?)";
            PreparedStatement pst=con.prepareStatement(sql);
            long account_number=generate_account_number();
            pst.setLong(1,account_number);
            pst.setString(2,full_name);
            pst.setString(3,email);
            pst.setDouble(4,balance);
            pst.setString(5,security_pin);
            int eff=pst.executeUpdate();
            if(eff>0){
//                System.out.println("Account opened successfully");
                return account_number;
            }else {
                throw new RuntimeException("Account Creation failed!!");
            }

        }else{
            throw new RuntimeException("Account already exist");
        }

    }
    public long get_account_number(String email) throws SQLException {
        String sql="select account_number from accounts where email=?";
        PreparedStatement pst=con.prepareStatement(sql);
        pst.setString(1,email);
        ResultSet rs= pst.executeQuery();
        if(rs.next()){
            return rs.getLong("account_number");
        }else{
            throw new RuntimeException("Account does not exist");
        }
    }
    public long generate_account_number() throws SQLException {
        String sql="select account_number from accounts order by account_number desc limit 1";
        PreparedStatement pst=con.prepareStatement(sql);
        ResultSet rs=pst.executeQuery();
        if(rs.next()){
            long last_account_numberrs=rs.getLong("account_number");
            return last_account_numberrs+1;
        }else{
            return 1000100;
        }
    }
    public boolean account_exists(String email) throws SQLException {
        String sql="select * from accounts where email=?";
        PreparedStatement pst=con.prepareStatement(sql);
        pst.setString(1,email);
        ResultSet rs=pst.executeQuery();
        if(rs.next()){
            return true;
        }
        return false;

    }
}
