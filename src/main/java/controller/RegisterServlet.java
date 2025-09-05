package controller;

import model.User;
import dao.UserDAO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
        // Set encoding to UTF-8
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        // Retrieve form data
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        // Create User object
        User user = new model.User(username, email, password, role);

        // Use DAO to check if user exists
        UserDAO userDAO = new dao.UserDAO();
        boolean userExists = false;
        try {
            // Check if username or email already exists
            userExists = userDAO.checkUserExists(username, email);
            if (userExists) {
                // User already registered, redirect to login.jsp
                response.sendRedirect("login.jsp");
                return;
            }

            // If user doesn't exist, attempt registration
            boolean registered = userDAO.registerUser(user);
            if (registered) {
                // Success: Redirect to login.jsp
                response.sendRedirect("login.jsp");
            } else {
                // Failure: Forward to register.jsp with error
                request.setAttribute("error", "Registration failed. Please try again.");
                getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
            }
        } catch (ClassNotFoundException | SQLException e) {
            // Handle database errors
            request.setAttribute("error", "Database error: " + e.getMessage());
            getServletContext().getRequestDispatcher("/register.jsp").forward(request, response);
        }
    }
}