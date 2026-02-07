/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author k
 */
public class config {
    
    private static final String DB_PATH = "user.db";
    
    public static Connection connectDB() {
        try {
            Class.forName("org.sqlite.JDBC");
            String path = new File(DB_PATH).getAbsolutePath();
            return DriverManager.getConnection("jdbc:sqlite:" + path);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Initialize database and create tbl_register if it doesn't exist.
     */
    public static void initDB() {
        String sql = "CREATE TABLE IF NOT EXISTS tbl_register ("
                + "r_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "f_name TEXT NOT NULL,"
                + "l_name TEXT NOT NULL,"
                + "email TEXT NOT NULL UNIQUE,"
                + "username TEXT NOT NULL,"
                + "password TEXT NOT NULL,"
                + "user_type TEXT NOT NULL DEFAULT 'user',"
                + "status TEXT NOT NULL DEFAULT 'pending')";
        try (Connection conn = connectDB();
             Statement stmt = conn.createStatement()) {
            if (conn != null) {
                stmt.execute(sql);
                // Create default admin if no admin exists
                try (PreparedStatement check = conn.prepareStatement(
                        "SELECT COUNT(*) FROM tbl_register WHERE user_type = 'admin'");
                     ResultSet rs = check.executeQuery()) {
                    if (rs.next() && rs.getInt(1) == 0) {
                        try (PreparedStatement ins = conn.prepareStatement(
                                "INSERT INTO tbl_register(f_name, l_name, email, username, password, user_type, status) VALUES (?,?,?,?,?,?,?)")) {
                            ins.setString(1, "Admin");
                            ins.setString(2, "User");
                            ins.setString(3, "admin@pasok.com");
                            ins.setString(4, "admin");
                            ins.setString(5, "admin123");
                            ins.setString(6, "admin");
                            ins.setString(7, "active");
                            ins.executeUpdate();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void addRecord(String sql, Object... values) {
    try (Connection conn = connectDB();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        for (int i = 0; i < values.length; i++) {
            pstmt.setObject(i + 1, values[i]);
        }

        pstmt.executeUpdate();
        System.out.println("Record added successfully!");
    } catch (SQLException e) {
        System.out.println("Error adding record: " + e.getMessage());
    }
}
    
    public String authenticate(String sql, Object... values) {
    try (Connection conn = connectDB();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        for (int i = 0; i < values.length; i++) {
            pstmt.setObject(i + 1, values[i]);
        }

        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getString("type");
            }
        }
    } catch (SQLException e) {
        System.out.println("Login Error: " + e.getMessage());
    }
    return null;
}
    
    
public void displayData(String sql, javax.swing.JTable table) {
    try (Connection conn = connectDB();
         PreparedStatement pstmt = conn.prepareStatement(sql);
         ResultSet rs = pstmt.executeQuery()) {
        
        if (conn != null) {
            table.setModel(resultSetToTableModel(rs));
        }
        
    } catch (SQLException e) {
        System.out.println("Error displaying data: " + e.getMessage());
    }
}

    /**
     * Convert ResultSet to DefaultTableModel for JTable display.
     */
    public static TableModel resultSetToTableModel(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        
        // Build column names
        String[] columnNames = new String[columnCount];
        for (int i = 0; i < columnCount; i++) {
            columnNames[i] = metaData.getColumnLabel(i + 1);
        }
        
        // Build rows
        java.util.List<Object[]> rows = new java.util.ArrayList<>();
        while (rs.next()) {
            Object[] row = new Object[columnCount];
            for (int i = 0; i < columnCount; i++) {
                row[i] = rs.getObject(i + 1);
            }
            rows.add(row);
        }
        
        // Create DefaultTableModel (non-editable for display)
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        for (Object[] row : rows) {
            model.addRow(row);
        }
        return model;
    }
}
