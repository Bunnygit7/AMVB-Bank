import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {
    private Connection con;
    private Scanner sc;
    User(Connection con,Scanner sc){
        this.con=con;
        this.sc=sc;
    }
    public void register() throws SQLException {
        System.out.print("Enter your full name:");
        String full_name=sc.next();
        System.out.print("Enter your email-id:");
        String email=sc.next();
        System.out.print("Enter your password:");
        String password=sc.next();
        if(user_exists(email)){
            System.out.println("User already exist for this email address");
            return;
        }
        String sql="insert into user(full_name,email,password) values (?,?,?)";
        PreparedStatement pst=con.prepareStatement(sql);
        pst.setString(1,full_name);
        pst.setString(2,email);
        pst.setString(3,password);
        int eff=pst.executeUpdate();
        if(eff>0){
            System.out.println("Your Registration completed successfully!!");
        }
        else{
            System.out.println("Registration failed!!");
        }
    }
    public String login() throws SQLException {
        System.out.print("Enter your email-id:");
        String email=sc.next();
        System.out.print("Enter your password:");
        String password=sc.next();
//        System.out.println("aaa");
        String sql="SELECT * FROM user WHERE email= ? AND password= ?";
        try{
            PreparedStatement pst= con.prepareStatement(sql);
            pst.setString(1,email);
            pst.setString(2,password);
            ResultSet rs= pst.executeQuery();
            if(rs.next()){
                return email;
            }else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;

    }
    public boolean user_exists(String email_id) throws SQLException {
        String sql="select * from user where email=?";
        PreparedStatement pst= con.prepareStatement(sql);
        pst.setString(1,email_id);
        ResultSet rs=pst.executeQuery();
        if(rs.next()){
            return true;
        }
        return false;
    }
}
