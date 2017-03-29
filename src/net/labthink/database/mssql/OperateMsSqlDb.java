/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.labthink.database.mssql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.labthink.utils.BytePlus;
import net.labthink.utils.Randomer;

/**
 *
 * @author Mosliu
 */
public class OperateMsSqlDb {

    public static Randomer r = new Randomer(0, 200);

    public static Connection createConnection(String connstr) {
        Connection con = null;
        String _connstr = "jdbc:sqlserver://LABTHINK-207CDE;user=sa;password=p@ssword;database=Test1;integratedSecurity=true;";
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String connectionUrl = connstr;
            con = DriverManager.getConnection(connectionUrl);
        } catch (SQLException ex) {
            Logger.getLogger(OperateMsSqlDb.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(OperateMsSqlDb.class.getName()).log(Level.SEVERE, null, ex);
        }
        return con;
    }

    public static void executeSingleStatement(PreparedStatement stmt) {
        try {
            stmt.execute();
//            stmt.executeQuery();

//            while (rs.next()) {
//                System.out.println(rs.getString("LastName") + ", " + rs.getString("FirstName"));
//            }
//            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createBatchSxtatment() throws SQLException {
        String _connstr = "jdbc:sqlserver://LABTHINK-207CDE;user=sa;password=p@ssword;database=Test1;integratedSecurity=true;";
        Connection conn = createConnection(_connstr);
        Statement stmt = conn.createStatement();
        long start = System.currentTimeMillis();
        int count = 31500;
        int batchnum = 300;
        int cycle = count / batchnum + (count % batchnum == 0 ? 0 : 1);
        for (int icount = 0; icount < cycle; icount++) {
            for (int i = 0; i < batchnum; i++) {
                StringBuilder sb = new StringBuilder();
                sb.append("INSERT INTO [dbo].[Experi_Data]( [ExperiID], [GetType], [DataCount], [DataType] , [Data").append(
                        r.nextInt(0, 10)).append("] , [Data").append(
                        r.nextInt(12, 40)).append("] , [Data").append(
                        r.nextInt(41, 81)).append("] , [Data").append(
                        r.nextInt(82, 130)).append("] , [Data").append(
                        r.nextInt(131, 161)).append("] , [Data").append(
                        r.nextInt(162, 192)).append("] , [Data").append(
                        r.nextInt(193, 206)).append("])  VALUES( 0, 0, 0, 0, 'aaaaaaaaaaaaaaa','bbbbbbbbbbbbbbb','cccccccccccccccccccc','dddddddddddddddddddddddddddddddddddddddddddddddd','eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee','fffffffffffffffffffffffffffffffffffffffffffffffff','ggggggggggggggggggggggggggggggggggggggggggg')");
//            System.out.println(sb.toString());
                stmt.addBatch(sb.toString());
            }
                int[] succ = stmt.executeBatch();
                System.out.print("写库"+batchnum*(icount+1)+"条");
                System.out.println("\r\n共耗时时间："+(System.currentTimeMillis()-start)+",平均每条时间:"+(System.currentTimeMillis()-start)/batchnum);
                start = System.currentTimeMillis();
                conn.commit();
        }


//        System.out.println("开始写库....");
        stmt.close();
        conn.close();
    }

    public static void main(String[] args) {
        try {
            createBatchSxtatment();
        } catch (SQLException ex) {
            Logger.getLogger(OperateMsSqlDb.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main1(String[] args) {
        try {
            String _connstr = "jdbc:sqlserver://LABTHINK-207CDE;user=sa;password=p@ssword;database=Test1;integratedSecurity=true;";
            Connection conn = createConnection(_connstr);
            String SQL = "INSERT INTO [dbo].[Experi_Data]( [ExperiID], [GetType], [DataCount], [DataType] , [Data109])  VALUES( 0, 0, 0, 0, 'bbb')";
            PreparedStatement pstmt = conn.prepareStatement(SQL);
            int k = 468157893;


//            pstmt.setBytes(2, BytePlus.int2bytes(k));
//            pstmt.setString(1, 9999);
            executeSingleStatement(pstmt);
            conn.commit();
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(OperateMsSqlDb.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
