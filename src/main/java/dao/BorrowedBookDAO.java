package dao;

import model.BorrowedBook;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BorrowedBookDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/library_db";
    private static final String USER = "root";
    private static final String PASSWORD = "Archer@123";

    public List<BorrowedBook> getBorrowedBooksByUser(int userId) throws Exception {
        List<BorrowedBook> books = new ArrayList<>();

        String sql = "SELECT t.book_id, b.title, t.due_date " +
                     "FROM transactions t " +
                     "JOIN books b ON t.book_id = b.book_id " +
                     "WHERE t.user_id = ? AND t.status = 'approved'";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                BorrowedBook book = new BorrowedBook();
                book.setBookId(rs.getInt("book_id"));
                book.setBookTitle(rs.getString("title"));  
                Date dueDate = rs.getDate("due_date");
                book.setDueDate(dueDate != null ? dueDate.toLocalDate() : null);

                books.add(book);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return books;
    }

    
    public List<BorrowedBook> getPendingRequests() throws Exception {
        List<BorrowedBook> list = new ArrayList<>();
        Connection conn =DriverManager.getConnection(URL, USER, PASSWORD);; 
        String sql = "SELECT transaction_id, user_id, book_id, status FROM transactions WHERE status = 'pending'";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            BorrowedBook book = new BorrowedBook();
            book.setTransactionId(rs.getInt("transaction_Id"));
            book.setUserId(rs.getInt("user_id"));
            book.setBookId(rs.getInt("book_id"));        
            book.setStatus(rs.getString("status"));
            list.add(book);
        }

        return list;
    }
    
    
    public void approveRequest(int requestId) throws Exception {
        String sql = "UPDATE transactions SET status='approved' WHERE transaction_id=?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        	PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, requestId);
            ps.executeUpdate();
        }
    }

    public void rejectRequest(int requestId) throws Exception {
        String sql = "UPDATE transactions SET status='rejected' WHERE transaction_id=?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        	PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, requestId);
            ps.executeUpdate();
        }
    }
    
    public int getUserIdByUsername(String username) throws Exception {
        int userId = -1;
        String sql = "SELECT user_id FROM users WHERE username = ? AND role != 'librarian'";  // Ensure user is not an librarian

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);  
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                userId = rs.getInt("user_id");  
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return userId; 
    }

   

}