package controller;

import dao.AdminBookDAO;
import model.AdminBook;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/searchServlet")
public class searchServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String searchKeyword = request.getParameter("search");

        if (searchKeyword == null || searchKeyword.trim().isEmpty()) {
            response.sendRedirect("user_dashboard.jsp?error=Please+enter+a+search+term");
            return;
        }

        try {
            AdminBookDAO bookDAO = new AdminBookDAO();


            List<AdminBook> searchResults = bookDAO.searchBooksByTitleOrAuthor(searchKeyword.trim());

            if (searchResults == null || searchResults.isEmpty()) {
                request.setAttribute("searchMessage", "No books found for: " + searchKeyword);
            } else {
                request.setAttribute("searchResults", searchResults);
            }

            request.getRequestDispatcher("user_dashboard.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("user_dashboard.jsp?error=Something+went+wrong");
        }
    }
}
