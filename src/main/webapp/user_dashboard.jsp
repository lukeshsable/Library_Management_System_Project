<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
    import="java.util.List,model.BorrowedBook,dao.BorrowedBookDAO,model.AdminBook" %>
<%@ page session="true" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>User Dashboard | Library System</title>
    
    <!-- Bootstrap 5 CDN -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=DM+Sans&display=swap" rel="stylesheet">

    <style>
        body {
            font-family: 'DM Sans', sans-serif;
            background-color: #f8f9fa;
        }

        .navbar {
            background-color: #0d6efd;
        }

        .navbar-brand, .nav-link {
            color: #fff !important;
        }

        .section-heading {
            color: #0d6efd;
            font-weight: bold;
            margin-bottom: 20px;
        }

        .form-control {
            max-width: 400px;
        }

        .book-card {
            transition: transform 0.2s ease-in-out;
        }

        .book-card:hover {
            transform: scale(0.97);
        }

		.book-card img {
		    width: 100%;
		    height: auto;
		    max-height: 250px; /* slightly reduced to fit in smaller card */
		    object-fit: contain;
		    border-radius: 6px;
		    background-color: #f8f9fa;
		    padding: 6px;
		    transition: transform 0.3s ease-in-out;
		}



        .table th {
            background-color: #0d6efd;
            color: white;
        }

        .table td {
            background-color: #ffffff;
        }

        .btn-primary, .btn-success, .btn-warning {
            min-width: 100px;
        }

        .container-section {
            padding: 40px 0;
        }
        
            .btn-logout {
        background-color: #dc3545;
        color: #ffffff;
        border: none;
        transition: all 0.3s ease;
    	}

	    .btn-logout:hover {
	        background-color: #ffffff;
	        color: #dc3545;
	        border: 1px solid #dc3545;
	    }
    </style>
</head>
<body>

<%
    String username = (String) session.getAttribute("username");

    if (username == null) {
        response.sendRedirect("login.jsp?error=Please login first");
        return;
    }

    BorrowedBookDAO borrowedBookDAO = new BorrowedBookDAO();
    List<BorrowedBook> borrowedBooks = null;
    int userId = -1;

    try {
        userId = borrowedBookDAO.getUserIdByUsername(username);  
    } catch (Exception e) {
        e.printStackTrace();
    }

    if (userId != -1) {
        try {
            borrowedBooks = borrowedBookDAO.getBorrowedBooksByUser(userId);  
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
%>

<!-- Navbar -->
<nav class="navbar navbar-expand-lg navbar-dark">
    <div class="container-fluid">
        <a class="navbar-brand" href="#">Library Dashboard</a>
        <div class="d-flex">
            <span class="navbar-text me-3 text-white">Welcome, <%= username %></span>
            <a href="login.jsp" class="btn btn-logout btn-sm">Logout</a>
        </div>
    </div>
</nav>

<!-- Main Container -->

<div class="container-sm mt-4" style="max-width: 1000px;">

    <!-- Search Books -->
    <div class="container-section text-center">
        <h3 class="section-heading">Search Library Catalog</h3>
        <form action="searchServlet" method="post" class="d-flex justify-content-center gap-2">
            <input type="text" name="search" class="form-control" placeholder="Enter title or author..." required>
            <button type="submit" class="btn btn-primary">Search</button>
        </form>
    </div>

    <!-- Search Results -->
    <%
        List<AdminBook> searchResults = (List<AdminBook>) request.getAttribute("searchResults");
        if (searchResults != null) {
    %>
    <div class="container-section">
        <h4 class="section-heading text-center">Search Results</h4>
        <div class="row g-4 justify-content-center">
            <%
                for (AdminBook book : searchResults) {
            %>
            <div class="col-md-3">
                <div class="card book-card shadow-sm">
                    <img src="adminActionServlet?imageId=<%= book.getId() %>" class="card-img-top" alt="Book Image">
                    <div class="card-body text-center">
                        <h6 class="card-title"><%= book.getTitle() %></h6>
                        <p class="card-text text-muted">by <%= book.getAuthor() %></p>
                    </div>
                </div>
            </div>
            <% } %>
        </div>
    </div>
    <% } %>

    <!-- Borrow a Book -->
    <div class="container-section text-center">
        <h4 class="section-heading">Request to Borrow a Book</h4>
        <form action="BorrowServlet" method="post" class="d-flex justify-content-center gap-2">
            <input type="text" name="bookName" class="form-control" placeholder="Enter book name" required>
            <input type="hidden" name="username" value="<%= username %>">
            <button type="submit" class="btn btn-success">Borrow</button>
        </form>
    </div>

    <!-- Return a Book -->
    <div class="container-section text-center">
        <h4 class="section-heading">Return a Borrowed Book</h4>
        <form action="ReturnServlet" method="post" class="d-flex justify-content-center gap-2">
            <input type="text" name="returnBookName" class="form-control" placeholder="Enter book name" required>
            <button type="submit" class="btn btn-warning">Return</button>
        </form>
    </div>

    <!-- Borrowed Books Table -->
    <div class="container-section">
        <h4 class="section-heading text-center">Your Borrowed Books</h4>
        <div class="table-responsive">
            <table class="table table-bordered text-center shadow-sm">
                <thead>
                    <tr>
                        <th>Book ID</th>
                        <th>Title</th>
                        <th>Due Date</th>
                    </tr>
                </thead>
                <tbody>
                <%
                    if (borrowedBooks != null && !borrowedBooks.isEmpty()) {
                        for (BorrowedBook book : borrowedBooks) {
                %>
                    <tr>
                        <td><%= book.getBookId() %></td>
                        <td><%= book.getBookTitle() %></td>
                        <td><%= book.getDueDate() != null ? book.getDueDate() : "N/A" %></td>
                    </tr>
                <%
                        }
                    } else {
                %>
                    <tr>
                        <td colspan="3">No borrowed books found.</td>
                    </tr>
                <%
                    }
                %>
                </tbody>
            </table>
        </div>
    </div>

</div>

<!-- Bootstrap JS Bundle -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>
