/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.labthink.instrument.device.G2131.simulator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
import javax.swing.JFileChooser;
import org.apache.log4j.Level;
import org.apache.log4j.Priority;

/**
 *
 * @author Moses
 */
public class dbwriter {

    private static final Logger logger = Logger.getLogger(dbwriter.class);
    private static Connection conn = null;
    String url = null;

    public static void main(String[] args) {
        dbwriter dw = new dbwriter();
        dw.run();
    }

    public void run() {
        String path = "E:\\test\\PERME-G2-131\\Code\\WinPermeG2_131\\Release\\WinPerme_G2_131.mdb";
        conn = getConnection(path);
        String sql = "INSERT INTO OPERATELOG ( LOGID, OPERATELOG,OPERATETIME,OPERATOR,CELLID,ADDITION1,ADDITION2) VALUES (?,'delete user',date(),'',0,'',0) ";

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            for (int i = 38; i < 100000; i++) {
                stmt.setInt(1, i);
                stmt.execute();
            }
            stmt.close();
        } catch (SQLException ex) {
            logger.error(ex);
        }
        CloseConnection();
    }

    public void runrun() {
        String path = "E:\\test\\PERME-G2-131\\Code\\WinPermeG2_131\\Release\\WinPerme_G2_131.mdb";
        conn = getConnection(path);
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select max(LOGID) from OPERATELOG");
            if (rs.next()) {
                System.out.println(rs.getString(1));

            }
        } catch (SQLException ex) {
            logger.error(ex);
        }
        CloseConnection();
    }

    private Connection getConnection(String path) {
        if (url == null) {
            url = "jdbc:odbc:Driver={MicroSoft Access Driver (*.mdb)};DBQ=" + path;
        }
        try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            conn = DriverManager.getConnection(url, "admin", "qqqaaa");
        } catch (SQLException ex) {
            logger.error(ex);
        } catch (ClassNotFoundException ex) {
            logger.error(ex);
        }

        return conn;
    }

    private void CloseConnection() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
                logger.error(ex);
            }
        }
    }
}
