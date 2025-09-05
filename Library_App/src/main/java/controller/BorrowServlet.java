package controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

@WebServlet("/BorrowServlet")
public class BorrowServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // Database config
    private static final String URL = "jdbc:mysql://localhost:3306/library_db";
    private static final String USER = "root";
    private static final String PASS = "Archer@123";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String bookTitle = request.getParameter("bookName"); // 'bookName' is the input name from the form
        String username = (String) request.getSession().getAttribute("username");

        if (bookTitle == null || username == null) {
            response.sendRedirect("user_dashboard.jsp?error=Invalid+book+title+or+user+session");
            return;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {

                // Step 1: Get user ID from username
                int userId = -1;
                String userSql = "SELECT user_id FROM users WHERE username = ?";
                try (PreparedStatement ps = conn.prepareStatement(userSql)) {
                    ps.setString(1, username);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        userId = rs.getInt("user_id");
                    }
                }

                // Step 2: Get book ID and quantity using book title
                int bookId = -1;
                int quantity = 0;
                String bookSql = "SELECT book_id, quantity FROM books WHERE title = ?";
                try (PreparedStatement ps = conn.prepareStatement(bookSql)) {
                    ps.setString(1, bookTitle);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        bookId = rs.getInt("book_id");
                        quantity = rs.getInt("quantity");
                    }
                }

                if (userId != -1 && bookId != -1) {
                    if (quantity > 0) {
                        // Step 3: Update book quantity
                        String updateQuantitySql = "UPDATE books SET quantity = quantity - 1 WHERE book_id = ?";
                        try (PreparedStatement ps = conn.prepareStatement(updateQuantitySql)) {
                            ps.setInt(1, bookId);
                            ps.executeUpdate();
                        }

                        // Step 4: Insert borrow request
                        LocalDate borrowDate = LocalDate.now();
                        LocalDate dueDate = borrowDate.plusDays(14);

                        String insertSql = "INSERT INTO transactions (user_id, book_id, borrow_date, due_date, status) VALUES (?, ?, ?, ?, 'pending')";
                        try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                            ps.setInt(1, userId);
                            ps.setInt(2, bookId);
                            ps.setDate(3, Date.valueOf(borrowDate));
                            ps.setDate(4, Date.valueOf(dueDate));

                            int rows = ps.executeUpdate();
                            if (rows > 0) {
                                response.sendRedirect("user_dashboard.jsp?message=Borrow+request+submitted");
                            } else {
                                response.sendRedirect("user_dashboard.jsp?error=Borrow+request+failed");
                            }
                        }

                    } else {
                        response.sendRedirect("user_dashboard.jsp?error=Book+not+available");
                    }
                } else {
                    response.sendRedirect("user_dashboard.jsp?error=Invalid+user+or+book");
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("user_dashboard.jsp?error=An+error+occurred");
        }
    }
}
