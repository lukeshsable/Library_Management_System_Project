package dao;

import model.AdminBook;

import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminBookDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/library_db";
    private static final String USER = "root";
    private static final String PASSWORD = "Archer@123";
    
    public AdminBook getBookById(int id) {
        AdminBook book = null;
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            String sql = "SELECT * FROM books WHERE book_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                book = new AdminBook();
                book.setId(rs.getInt("book_id"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setIsbn(rs.getString("isbn"));
                book.setQuantity(rs.getInt("quantity")); // ✅ Add this line
                book.setImage(rs.getString("image"));  // Correct for image URL/path
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return book;
    }



    public int getTotalBooks() throws SQLException, ClassNotFoundException {
        String query = "SELECT COUNT(*) FROM books";
        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement ps = conn.createStatement();
             ResultSet rs = ps.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public int getBorrowedCount() throws SQLException, ClassNotFoundException {
        String query = "SELECT COUNT(*) FROM transactions WHERE status = 'borrowed'";
        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement ps = conn.createStatement();
             ResultSet rs = ps.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public int getOverdueCount() throws SQLException, ClassNotFoundException {
        String query = "SELECT COUNT(*) FROM transactions WHERE status = 'overdue'";
        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement ps = conn.createStatement();
             ResultSet rs = ps.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
    
    public List<AdminBook> getAllBooks() throws Exception {
        List<AdminBook> books = new ArrayList<>();

        String sql = "SELECT book_id, title, isbn, author, quantity FROM books"; 

        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                AdminBook book = new AdminBook();
                book.setId(rs.getInt("book_id"));
                book.setTitle(rs.getString("title"));
                book.setIsbn(rs.getString("isbn"));
                book.setAuthor(rs.getString("author"));
                book.setQuantity(rs.getInt("quantity"));
                books.add(book);
            }
        }
        return books;
    }

    public InputStream getBookImageById(int bookId) throws Exception {
        String sql = "SELECT image FROM books WHERE book_id = ?";
        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bookId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getBinaryStream("image");
            }
        }
        return null;
    }

    // 🟢 Updated method to handle image upload (including setting MIME type)
    public void addBook(AdminBook book) throws SQLException, ClassNotFoundException {
        
        String sql = "INSERT INTO books (title, author, isbn, image, quantity) VALUES (?, ?, ?, ?, ?)"; 
        Class.forName("com.mysql.cj.jdbc.Driver");

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getIsbn());

            // Check if there is an image to upload
            if (book.getImageStream() != null) {
                ps.setBlob(4, book.getImageStream()); // Store image as BLOB
            } else {
                ps.setNull(4, Types.BLOB); // Null if no image provided
            }
            ps.setInt(5, book.getQuantity()); 

            ps.executeUpdate();
        }
    }
    
    // ✅ Update book with image
    public void updateBookWithImage(AdminBook book) throws SQLException, ClassNotFoundException {
    	String sql = "UPDATE books SET title = ?, author = ?, isbn = ?, image = ?, quantity = ? WHERE book_id = ?"; 
        Class.forName("com.mysql.cj.jdbc.Driver");

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getIsbn());
            ps.setBlob(4, book.getImageStream());
            ps.setInt(5, book.getQuantity());  
            ps.setInt(6, book.getId());        

            ps.executeUpdate();
        }
    }

    // ✅ Update book without image
    public void updateBookWithoutImage(AdminBook book) throws SQLException, ClassNotFoundException {
    	String sql = "UPDATE books SET title = ?, author = ?, isbn = ?, quantity = ? WHERE book_id = ?"; 
        Class.forName("com.mysql.cj.jdbc.Driver");

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getIsbn());
            ps.setInt(4, book.getQuantity()); 
            ps.setInt(5, book.getId());      


            ps.executeUpdate();
        }
    }

    // ✅ Delete book
    public void deleteBook(int bookId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM books WHERE book_id = ?";
        Class.forName("com.mysql.cj.jdbc.Driver");

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bookId);
            ps.executeUpdate();
        }
    }
    
    //--------------------- Method for user ------------------------//
    
    public List<AdminBook> searchBooksByTitleOrAuthor(String keyword) throws Exception {
        List<AdminBook> books = new ArrayList<>();

        String sql = "SELECT book_id, title, author, image FROM books WHERE title LIKE ? OR author LIKE ?";

        
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            String query = "%" + keyword + "%";
            ps.setString(1, query);
            ps.setString(2, query);
            
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                AdminBook book = new AdminBook();
                book.setId(rs.getInt("book_id"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setImage(rs.getString("image"));
                books.add(book);
            }
        }

        return books;
    }
    
}
