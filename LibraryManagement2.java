/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package vu.librarymanagement2;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

/**
 *
 * @author ABDULLAHI
 */


public class LibraryManagement2 extends javax.swing.JFrame {
    private JTextField bookIDField, titleField, authorField, yearField;
    private JTable bookTable;
    private DefaultTableModel tableModel;
    private JButton addButton, deleteButton, refreshButton;
    private Connection connection;

    public LibraryManagement2() {
        initComponents();
        connectToDatabase();
        loadBookData();
    }

    private void initComponents() {
        // Initialize components 
    }

    private void connectToDatabase() {
        try {
            String url = "jdbc:ucanaccess://C:\\Users\\ABDULLAHI\\OneDrive\\Documents\\NetBeansProjects\\LibraryManagement2\\Library1.accdb";
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection failed!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadBookData() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Books");
            tableModel.setRowCount(0);
            while (resultSet.next()) {
                String bookID = resultSet.getString("BookID");
                String title = resultSet.getString("Title");
                String author = resultSet.getString("Author");
                int year = resultSet.getInt("Year");
                tableModel.addRow(new Object[]{bookID, title, author, year});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addBook() {
        String bookID = bookIDField.getText();
        String title = titleField.getText();
        String author = authorField.getText();
        int year = Integer.parseInt(yearField.getText());

        try {
            String sql = "INSERT INTO Books (BookID, Title, Author, Year) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, bookID);
            statement.setString(2, title);
            statement.setString(3, author);
            statement.setInt(4, year);
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Book added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadBookData(); 
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding book!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to delete", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String bookID = (String) tableModel.getValueAt(selectedRow, 0);

        try {
            String sql = "DELETE FROM Books WHERE BookID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, bookID);
            statement.executeUpdate();
            loadBookData();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting book!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LibraryManagement().setVisible(true);
            }
        });
    }
}