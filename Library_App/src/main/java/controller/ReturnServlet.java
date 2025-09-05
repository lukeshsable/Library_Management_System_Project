package controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

@WebServlet("/ReturnServlet")
public class ReturnServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String URL = "jdbc:mysql://localhost:3306/library_db";
    private static final String USER = "root";
    private static final String PASS = "Archer@123";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String returnBookTitle = request.getParameter("returnBookName");
        String username = (String) request.getSession().getAttribute("username");

        if (returnBookTitle == null || username == null) {
            response.sendRedirect("user_dashboard.jsp?error=Invalid+book+name+or+user+session");
            return;
        }

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            conn.setAutoCommit(false); // Start transaction

            // Get user ID
            int userId = getUserId(conn, username);
            if (userId == -1) {
                response.sendRedirect("user_dashboard.jsp?error=User+not+found");
                return;
            }

            // Get book ID using title
            int bookId = getBookIdByTitle(conn, returnBookTitle);
            if (bookId == -1) {
                response.sendRedirect("user_dashboard.jsp?error=Book+not+found");
                return;
            }

            // Check if the book is borrowed by the user
            if (!checkIfBookBorrowedByUser(conn, userId, bookId)) {
                response.sendRedirect("user_dashboard.jsp?error=Book+not+borrowed+by+user");
                return;
            }

            // Update transaction to 'returned'
            if (!updateTransactionStatusToReturned(conn, userId, bookId)) {
                conn.rollback();
                response.sendRedirect("user_dashboard.jsp?error=Failed+to+return+book");
                return;
            }

            // Increase book quantity by 1
            updateBookQuantity(conn, bookId);

            conn.commit(); // Commit transaction
            response.sendRedirect("user_dashboard.jsp?message=Book+returned+successfully");

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("user_dashboard.jsp?error=Database+error");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("user_dashboard.jsp?error=An+unexpected+error+occurred");
        }
    }

    private int getUserId(Connection conn, String username) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("SELECT user_id FROM users WHERE username = ?")) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt("user_id") : -1;
        }
    }

    private int getBookIdByTitle(Connection conn, String title) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("SELECT book_id FROM books WHERE title = ?")) {
            ps.setString(1, title);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt("book_id") : -1;
        }
    }

    private boolean checkIfBookBorrowedByUser(Connection conn, int userId, int bookId) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT COUNT(*) FROM transactions WHERE user_id = ? AND book_id = ? AND status = 'approved'")) {
            ps.setInt(1, userId);
            ps.setInt(2, bookId);
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }

    private boolean updateTransactionStatusToReturned(Connection conn, int userId, int bookId) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "UPDATE transactions SET status = 'returned', return_date = ? WHERE user_id = ? AND book_id = ? AND status = 'approved'")) {
            ps.setDate(1, Date.valueOf(LocalDate.now()));
            ps.setInt(2, userId);
            ps.setInt(3, bookId);
            return ps.executeUpdate() > 0;
        }
    }

    private void updateBookQuantity(Connection conn, int bookId) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("UPDATE books SET quantity = quantity + 1 WHERE book_id = ?")) {
            ps.setInt(1, bookId);
            ps.executeUpdate();
        }
    }
}
