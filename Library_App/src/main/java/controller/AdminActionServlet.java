package controller;

import dao.AdminBookDAO;
import dao.BorrowedBookDAO;
import model.AdminBook;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@WebServlet("/adminActionServlet")
@MultipartConfig(maxFileSize = 16177215)  // 16MB max image size
public class AdminActionServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // 🟢 GET: Return book image by ID
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String imageId = request.getParameter("imageId");

        if (imageId != null) {
            try {
                int bookId = Integer.parseInt(imageId);
                AdminBookDAO dao = new AdminBookDAO();
                InputStream imageStream = dao.getBookImageById(bookId);

                if (imageStream != null) {
                    response.setContentType("image/jpg");
                    OutputStream out = response.getOutputStream();
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = imageStream.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                    imageStream.close();
                    out.flush();
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing imageId");
        }
    }

    // 🟢 POST: Handle add, update, delete, approve, reject
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        AdminBookDAO bookDAO = new AdminBookDAO();

        try {
            if ("add".equals(action)) {
                // Add new book
                String title = request.getParameter("bookTitle");
                String author = request.getParameter("author");
                String isbn = request.getParameter("isbn");
                int quantity = Integer.parseInt(request.getParameter("quantity"));
                Part imagePart = request.getPart("bookImage");

                InputStream imageStream = null;
                if (imagePart != null && imagePart.getSize() > 0) {
                    imageStream = imagePart.getInputStream();
                }

                AdminBook newBook = new AdminBook();
                newBook.setTitle(title);
                newBook.setAuthor(author);
                newBook.setIsbn(isbn);
                newBook.setQuantity(quantity);
                newBook.setImageStream(imageStream);

                bookDAO.addBook(newBook);
                response.sendRedirect("admin_dashboard.jsp");

            } else if ("update".equals(action)) {
                // Update existing book
                int bookId = Integer.parseInt(request.getParameter("bookId"));
                String title = request.getParameter("bookTitle");
                String author = request.getParameter("author");
                String isbn = request.getParameter("isbn");
                int quantity = Integer.parseInt(request.getParameter("quantity"));
                Part imagePart = request.getPart("bookImage");

                AdminBook updatedBook = new AdminBook();
                updatedBook.setId(bookId);
                updatedBook.setTitle(title);
                updatedBook.setAuthor(author);
                updatedBook.setIsbn(isbn);
                updatedBook.setQuantity(quantity);

                // Only set image if a new one is uploaded
                if (imagePart != null && imagePart.getSize() > 0) {
                    InputStream imageStream = imagePart.getInputStream();
                    updatedBook.setImageStream(imageStream);
                    bookDAO.updateBookWithImage(updatedBook);
                } else {
                    bookDAO.updateBookWithoutImage(updatedBook);
                }

                response.sendRedirect("admin_dashboard.jsp");

            } else if ("delete".equals(action)) {
                // Delete book
                int bookId = Integer.parseInt(request.getParameter("bookId"));
                bookDAO.deleteBook(bookId);
                response.sendRedirect("updatebook.jsp");

            } else if ("approve".equals(action)) {
                int transactionId = Integer.parseInt(request.getParameter("transactionId"));
                BorrowedBookDAO borrowedBookDAO = new BorrowedBookDAO();
                borrowedBookDAO.approveRequest(transactionId);
                response.sendRedirect("admin_dashboard.jsp");

            } else if ("reject".equals(action)) {
                int transactionId = Integer.parseInt(request.getParameter("transactionId"));
                BorrowedBookDAO borrowedBookDAO = new BorrowedBookDAO();
                borrowedBookDAO.rejectRequest(transactionId);
                response.sendRedirect("admin_dashboard.jsp");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("Error: " + e.getMessage());
        }
    }
}
