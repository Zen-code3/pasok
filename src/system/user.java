/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package system;

import config.config;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

/**
 * User management screen - lists registered users, admin can approve pending users.
 */
public class user extends JFrame {

    public int adminId;
    private JTable table;

    public user(int adminId) {
        this.adminId = adminId;
        setTitle("User Management");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 500);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(Color.WHITE);

        table = new JTable();
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton backBtn = new JButton("Back to Dashboard");
        backBtn.setBackground(new Color(51, 204, 0));
        backBtn.setForeground(Color.WHITE);
        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new dashboard(adminId).setVisible(true);
                dispose();
            }
        });

        JButton approveBtn = new JButton("Approve Selected");
        approveBtn.setBackground(new Color(255, 204, 204));
        approveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                approveSelectedUser();
            }
        });

        buttonPanel.add(approveBtn);
        buttonPanel.add(backBtn);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        loadUsers();
    }

    private void loadUsers() {
        config cfg = new config();
        cfg.displayData("SELECT r_id, f_name, l_name, email, username, user_type, status FROM tbl_register", table);
    }

    private void approveSelectedUser() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a user to approve.");
            return;
        }
        Object idObj = table.getValueAt(row, 0);
        if (idObj == null) return;
        int userId = Integer.parseInt(idObj.toString());

        int choice = JOptionPane.showConfirmDialog(this,
                "Approve this user?",
                "Confirm",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            try (Connection conn = config.connectDB();
                 PreparedStatement pst = conn.prepareStatement(
                         "UPDATE tbl_register SET status = 'active' WHERE r_id = ?")) {
                if (conn != null) {
                    pst.setInt(1, userId);
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(this, "User approved successfully.");
                    loadUsers();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }
}
