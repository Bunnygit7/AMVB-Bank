import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountManager {
    Connection con;
    Scanner sc;
    public AccountManager(Connection con,Scanner sc){
        this.con=con;
        this.sc=sc;
    }
    public void credit_money(long account_number) throws SQLException {
        System.out.println();
        System.out.print("Enter money:");
        double cr_money=sc.nextDouble();
        System.out.print("Enter Security pin:");
        String pin=sc.next();
        try {
            con.setAutoCommit(false);
            if(account_number!=0){
                String sql="select * from accounts where account_number=? and security_pin=?";
                PreparedStatement pst=con.prepareStatement(sql);
                pst.setLong(1,account_number);
                pst.setString(2,pin);
                ResultSet rs=pst.executeQuery();
                if(rs.next()){
                    String cr_sql="update accounts set balance=balance+? where account_number=?";
                    PreparedStatement pst1=con.prepareStatement(cr_sql);
                    pst1.setDouble(1,cr_money);
                    pst1.setLong(2,account_number);
                    int eff=pst.executeUpdate();
                    if(eff>0){
                        System.out.println("Amount credited successfully!!");
                        con.commit();
                        con.setAutoCommit(true);
                        return;
                    }else{
                        System.out.println("Transaction failed!!");
                        con.rollback();
                        con.setAutoCommit(true);
                    }

                }else{
                    System.out.println("Invalid Security Pin!");
                }

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        con.setAutoCommit(true);


    }
    public void debit_money(long account_number) throws SQLException {
        System.out.println();
        System.out.print("Enter money:");
        double dr_money=sc.nextDouble();
        System.out.print("Enter Security pin:");
        String pin=sc.next();
        try {
            con.setAutoCommit(false);
            if (account_number != 0) {
                String sql="select * from accounts where account_number=? and security_pin=?";
                PreparedStatement pst=con.prepareStatement(sql);
                pst.setLong(1,account_number);
                pst.setString(2,pin);
                ResultSet rs=pst.executeQuery();
                if(rs.next()){
                    double current_bal=rs.getDouble("balance");
                    if(dr_money<=current_bal){
                        String dr_sql="update accounts set balance=balance-? where account_number=?";
                        PreparedStatement pst1=con.prepareStatement(dr_sql);
                        pst1.setDouble(1,dr_money);
                        pst1.setLong(2,account_number);
                        int eff=pst1.executeUpdate();
                        if(eff>0){
                            System.out.println("RS."+dr_money+" debited successfully!!");
                            con.commit();
                            con.setAutoCommit(true);
                        }else{
                            System.out.println("Transaction failed!!");
                            con.rollback();
                            con.setAutoCommit(true);
                        }

                    }else{
                        System.out.println("Insufficient balance ");
                    }
                }else {
                    System.out.println("invalid pin!!");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    con.setAutoCommit(true);
    }
    public void transfer_money(long account_number) throws SQLException {
        System.out.println();
        System.out.print("Enter Receiver's account no:");
        long receiver_acc=sc.nextLong();
        System.out.print("Enter money:");
        double money=sc.nextDouble();
        System.out.print("Enter Security pin:");
        String pin=sc.next();
        try{
            con.setAutoCommit(false);
            if(account_number!=0 && receiver_acc!=0){
                PreparedStatement pst=con.prepareStatement("select * from accounts where account_number=? and security_pin=?");
                pst.setLong(1,account_number);
                pst.setString(2,pin);
                ResultSet rs=pst.executeQuery();
                if(rs.next()){
                    double crr_bal=rs.getDouble("balance");
                    if(money<=crr_bal){
                        PreparedStatement dr_pst=con.prepareStatement("update accounts set balance=balance-? where account_number=?");
                        PreparedStatement cr_pst=con.prepareStatement("update accounts set balance=balance+? where account_number=?");
                        dr_pst.setDouble(1,money);
                        dr_pst.setLong(2,account_number);
                        cr_pst.setDouble(1,money);
                        cr_pst.setLong(2,receiver_acc);
                        int eff1=dr_pst.executeUpdate();
                        int eff2=cr_pst.executeUpdate();
                        if(eff1>0 && eff2>0){
                            System.out.println("Transaction Successful!");
                            System.out.println("Rs."+money+" Transferred Successfully");
                            con.commit();
                            con.setAutoCommit(true);
                            return;
                        }else{
                            System.out.println("Transaction failed!");
                            con.rollback();
                            con.setAutoCommit(true);
                        }

                    }else{
                        System.out.println("Insufficient balance");
                    }

                }else{
                    System.out.println("Invalid pin");
                }
            }else {
                System.out.println("invalid account number");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());;
        }
        con.setAutoCommit(true);
    }
    public void check_balance(long account_number){
        System.out.println();
        System.out.print("Enter Security pin:");
        String pin=sc.next();
        try{
            PreparedStatement pst=con.prepareStatement("select balance from accounts where account_number=? and security_pin=?");
            pst.setLong(1,account_number);
            pst.setString(2,pin);
            ResultSet rs=pst.executeQuery();
            if(rs.next()){
                System.out.println("Balance:"+rs.getDouble("balance"));
            }else{
                System.out.println("invalid pin");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
